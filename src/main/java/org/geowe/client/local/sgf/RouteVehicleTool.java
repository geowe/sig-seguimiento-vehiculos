
package org.geowe.client.local.sgf;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.layermanager.tool.LayerTool;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayer;
import org.geowe.client.local.model.vector.VectorLayerConfig;
import org.geowe.client.local.model.vector.VectorLayerFactory;
import org.geowe.client.local.sgf.messages.UISgfMessages;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.geowe.client.local.ui.ProgressBarDialog;
import org.geowe.client.shared.rest.sgf.SGFRegisteredPointService;
import org.geowe.client.shared.rest.sgf.model.jso.PointRegisterJSO;
import org.geowe.client.shared.rest.sgf.model.jso.PointRegisterListResponseJSO;
import org.geowe.client.shared.rest.sgf.model.jso.SessionJSO;
import org.geowe.client.shared.rest.sgf.model.jso.VehicleJSO;
import org.gwtopenmaps.openlayers.client.Projection;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.format.WKT;
import org.gwtopenmaps.openlayers.client.geometry.Geometry;
import org.gwtopenmaps.openlayers.client.geometry.LineString;
import org.gwtopenmaps.openlayers.client.geometry.Point;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.common.client.api.tasks.ClientTaskManager;
import org.jboss.errai.enterprise.client.jaxrs.api.RestClient;
import org.jboss.errai.enterprise.client.jaxrs.api.RestErrorCallback;

import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.DateField;

/**
 * Herramienta para trazar en el mapa la ruta de puntos GPS de un vehículo
 * 
 * @author jose@geowe.org
 * 
 */
@ApplicationScoped
public class RouteVehicleTool extends LayerTool implements VehicleButtonTool {
	
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final Projection DEFAULT_PROJECTION = new Projection(GeoMap.INTERNAL_EPSG);
	private static final String PREFIX_LAYER = "r_";
	private static final String GPS_DEFAULT_PROJECTION = "EPSG:4326";
	private static final String PLATE = UISgfMessages.INSTANCE.plateColumn();
	private static final String DATE = UISgfMessages.INSTANCE.dateColumn();
	private static final String TIME = UISgfMessages.INSTANCE.timeColumn();
	private static final String SPEED = UISgfMessages.INSTANCE.speedColumn();
	private static final String POSITION = UISgfMessages.INSTANCE.positionColumn();
	private static final String DISTANCE = UISgfMessages.INSTANCE.distanceColumn();
	private static final String ACCUMULATED_DISTANCE = UISgfMessages.INSTANCE.acummulatedDistanceColumn();
	private static final String STREET = UISgfMessages.INSTANCE.streetColumn();
	private static final String NUMBER = UISgfMessages.INSTANCE.numberColumn();
	private static final String LOCALITY = UISgfMessages.INSTANCE.localityColumn();
	private static final String PROVINCE = UISgfMessages.INSTANCE.provinceColumn();
	private static final String POSTAL_CODE = UISgfMessages.INSTANCE.postalCodeColumn();
	private static final String COUNTRY = UISgfMessages.INSTANCE.countryColumn();
	private static final String NAME = UISgfMessages.INSTANCE.nameColumn();
	private static final String STATUS = UISgfMessages.INSTANCE.statusColumn();
	private static final int REGISTERED_POINTS_TO_LOAD = 500;
	
	private SessionJSO session;
	private DateField field = new DateField();
	private List<VehicleJSO> vehicles;
	private ProgressBarDialog autoMessageBox;
	
	@Inject
	private SGFServiceInfo SGFServiceInfo;
	
	@Inject
	private MessageDialogBuilder messageDialogBuilder;

	@Inject
	private ClientTaskManager taskManager;

	@Inject
	public RouteVehicleTool(LayerManagerWidget layerTreeWidget, GeoMap geoMap) {
		super(layerTreeWidget, geoMap);
		setEnabled(false);
	}

	@Override
	public String getName() {
		return UISgfMessages.INSTANCE.drawRoute();
	}

	@Override
	public ImageResource getIcon() {
		return ImageProvider.INSTANCE.lineString();
	}

	@Override
	public void onClick() {
		
		field.setEditable(false);		
		final DateTimeFormat fmt = DateTimeFormat.getFormat(DATE_FORMAT);
		final Date today = new Date();
		
		field.setText(fmt.format(today));
		
		final Dialog box = new Dialog();
		box.setHeadingText(UISgfMessages.INSTANCE.selectDate());
		box.add(field);
		box.setModal(true);
		box.setResizable(false);
		box.setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
		box.setHideOnButtonClick(true);
		box.getButton(PredefinedButton.OK).addSelectHandler(
				new SelectHandler() {
					@Override
					public void onSelect(final SelectEvent event) {

						final String startDate = field.getText();
						Date nextDayDate= fmt.parse(startDate);
						
						CalendarUtil.addDaysToDate(nextDayDate, 1);
						final String endDate = fmt.format(nextDayDate);						

						autoMessageBox = new ProgressBarDialog(false,
								UIMessages.INSTANCE.processing());
												
						autoMessageBox.show();
						taskManager.execute(new Runnable() {

							@Override
							public void run() {
								for (VehicleJSO vehicle : vehicles) {									
									//getSamplePoints(vehicle, startDate, endDate);
									getRequestRegisteredPoint(vehicle, startDate, endDate);

								}
							}
						});
					}
				});
		box.show();
	}	
	
		
	private void getRequestRegisteredPoint(final VehicleJSO vehicle, final String startDate, final String endDate) {
		autoMessageBox.setProgressStatusMessage(UISgfMessages.INSTANCE.getGPSData());
		
		RestClient.create(SGFRegisteredPointService.class, SGFServiceInfo.getURL(),
				new RemoteCallback<String>() {

					@Override
					public void callback(String pointRegisterListResponseJson) {	
						
						
						PointRegisterListResponseJSO pointRegisterResponse = JsonUtils
								.safeEval(pointRegisterListResponseJson);
						PointRegisterJSO[] pointRegisters = pointRegisterResponse
								.getPointRegisterListEmbededJSO().getPointRegister();
						List<PointRegisterJSO> points = Arrays.asList(pointRegisters);
						
						if(points.isEmpty()) {
							messageDialogBuilder.createInfo(UIMessages.INSTANCE.edtAlertDialogTitle(),  UISgfMessages.INSTANCE.gpsDataNotFound()).show();
							return;
						}
						
						createRouteLayer(vehicle, startDate, points);
						autoMessageBox.hide();

					}
				},

				new RestErrorCallback() {
					
					@Override
					public boolean error(Request message, Throwable throwable) {
						autoMessageBox.hide();
						messageDialogBuilder.createInfo(UISgfMessages.INSTANCE.errorDetected(),  throwable.getMessage()).show();
						
						return false;
					}
				}, Response.SC_OK).getRegisteredPoints(session.getToken(), vehicle.getId(), startDate, endDate, REGISTERED_POINTS_TO_LOAD, "date,desc");			
	}
	

	public void setVehicles(List<VehicleJSO> vehicles) {
		setEnabled(true);
		this.vehicles = vehicles;
	}
	
	public void setSession(SessionJSO session) {
		this.session = session;
	}
		

	private void createRouteLayer(VehicleJSO vehicleJSO, String dateToSearch, List<PointRegisterJSO> points) {
		
		VectorLayerConfig layerConfig = createVectorLayerConfig(vehicleJSO, dateToSearch);
		VectorLayer routeLayer = createEmptyRouteLayer(layerConfig);

		WKT reader = new WKT();
		List<Point> pointList = new ArrayList<Point>();

		int totalSpeed = 0;
		float accumulatedDistance = 0f;
		String date = getDateAsString(points.get(0).getDate());

		Point previusPoint = null;
		for (PointRegisterJSO point : points) {

			VectorFeature f = reader.read(point.getPosition())[0];

			Geometry g = f.getGeometry();
			Point currentPoint = Point.narrowToPoint(g.getJSObject());
			String position = "lon: " + currentPoint.getX() + " lat: "
					+ currentPoint.getY();

			f.getGeometry().transform(layerConfig.getProjection(),
					layerConfig.getDefaultProjection());

			routeLayer.addFeature(f);

			f.getAttributes().setAttribute(NAME, vehicleJSO.getName());
			
			f.getAttributes().setAttribute(PLATE, vehicleJSO.getPlate());
			f.getAttributes().setAttribute(DATE,
					getDateAsString(point.getDate()));
			f.getAttributes().setAttribute(TIME,
					getTimeAsString(point.getDate()));
			int speed = Double.valueOf(point.getSpeed()).intValue();

			f.getAttributes().setAttribute(SPEED, speed);
			
			f.getAttributes().setAttribute(STATUS,
					getPointStatus(point.getDatos())); // PARADA/MARCHA
			f.getAttributes().setAttribute(POSITION, position);

			g = f.getGeometry();
			currentPoint = Point.narrowToPoint(g.getJSObject());
			pointList.add(currentPoint);

			if (previusPoint == null) {
				f.getAttributes().setAttribute(DISTANCE, 0);
			} else {
				List<Point> segment = new ArrayList<Point>();
				segment.add(previusPoint);
				segment.add(currentPoint);

				LineString line = new LineString(
						segment.toArray(new Point[] {}));
				
				accumulatedDistance = accumulatedDistance + setDistance(f, line);
				f.getAttributes().setAttribute(ACCUMULATED_DISTANCE, getReoundedMeasure(accumulatedDistance, 2));
			}
			
			f.getAttributes().setAttribute(STREET, point.getStreet());
			f.getAttributes().setAttribute(NUMBER, point.getNumber());
			f.getAttributes().setAttribute(LOCALITY, point.getLocality());
			f.getAttributes().setAttribute(PROVINCE, point.getProvince());
			f.getAttributes().setAttribute(POSTAL_CODE, point.getPostalCode());
			f.getAttributes().setAttribute(COUNTRY, point.getCountry());
			
			previusPoint = currentPoint;
			totalSpeed = totalSpeed + speed;
		}

		LineString line = new LineString(pointList.toArray(new Point[] {}));

		final VectorFeature lineFeature = new VectorFeature(line);
		routeLayer.addFeature(lineFeature);
		
		lineFeature.getAttributes().setAttribute(NAME, vehicleJSO.getName());
		lineFeature.getAttributes().setAttribute(PLATE, vehicleJSO.getPlate());
		lineFeature.getAttributes().setAttribute(DATE, date);
		lineFeature.getAttributes().setAttribute(SPEED,
				(totalSpeed / points.size()));
		
		setDistance(lineFeature, line);

		String color = routeLayer.getVectorStyle().getFill().getNormalColor();
		routeLayer.getVectorStyle().getLine().setNormalColor(color);
		routeLayer.getVectorStyle().getLine().setThickness(3);

		layerManagerWidget.addVector(routeLayer);
		routeLayer.redraw();
		layerManagerWidget.setSelectedLayer(LayerManagerWidget.VECTOR_TAB,
				routeLayer);
		geoMap.getMap().zoomToExtent(routeLayer.getDataExtent());
	}
	
	private String getPointStatus(String datos) {
		String status = "undefined";
		if (datos.contains("1=1")) {
			status = UISgfMessages.INSTANCE.marchingValue();
		} else if (datos.contains("1=0")) {
			status = UISgfMessages.INSTANCE.stoppedValue();
		}
		return status;
	}

	private float setDistance(VectorFeature feature, LineString line) {
		float distance = getReoundedMeasure(line.getGeodesicLength(DEFAULT_PROJECTION), 2);
		feature.getAttributes().setAttribute(DISTANCE, distance);
		return distance;
	}

	private VectorLayerConfig createVectorLayerConfig(VehicleJSO vehicleJSO, String date) {

		VectorLayerConfig layerConfig = new VectorLayerConfig();
		layerConfig.setEpsg(GPS_DEFAULT_PROJECTION);
		layerConfig.setLayerName(PREFIX_LAYER + vehicleJSO.getPlate() + "_" + date);

		return layerConfig;
	}

	private VectorLayer createEmptyRouteLayer(VectorLayerConfig layerConfig) {

		VectorLayer routeLayer = null;

		try {
			routeLayer = VectorLayerFactory.createEmptyVectorLayer(layerConfig);
			routeLayer.addAttribute(NAME, false);
			routeLayer.addAttribute(PLATE, false);
			routeLayer.addAttribute(STATUS, false);			
			routeLayer.addAttribute(DATE, false);
			routeLayer.addAttribute(TIME, false);
			routeLayer.addAttribute(STREET, false);
			routeLayer.addAttribute(NUMBER, false);
			routeLayer.addAttribute(LOCALITY, false);
			routeLayer.addAttribute(PROVINCE, false);
			routeLayer.addAttribute(POSTAL_CODE, false);
			routeLayer.addAttribute(COUNTRY, false);			
			routeLayer.addAttribute(SPEED, false);
			routeLayer.addAttribute(DISTANCE, false);
			routeLayer.addAttribute(ACCUMULATED_DISTANCE, false);
			routeLayer.addAttribute(POSITION, false);

		} catch (Exception e) {
			messageDialogBuilder.createInfo("Error", e.getMessage()).show();
		}

		return routeLayer;
	}

	private void getSamplePoints(VehicleJSO vehicleJSO, String startDate, String endDate) {
		String plate = "";

		if ("29085257".equals(vehicleJSO.getPlate())) {
			plate = SampleDataProvider.INSTANCE.list20RegisterPoint().getText();
		} else if ("39213844".equals(vehicleJSO.getPlate())) {
			plate = SampleDataProvider.INSTANCE.list500RegisterPoint()
					.getText();
		}

		PointRegisterListResponseJSO pointRegisterResponse = JsonUtils
				.safeEval(plate);
		PointRegisterJSO[] pointRegisters = pointRegisterResponse
				.getPointRegisterListEmbededJSO().getPointRegister();
		List<PointRegisterJSO> points = Arrays.asList(pointRegisters);
		
		
		if (points.size() == 0) {
			messageDialogBuilder
					.createInfo("Atención",
							"No se encuentran datos registrados para la fecha especificada")
					.show();
			return;
		}
		
		createRouteLayer(vehicleJSO, startDate, points);
	}

	public String getDateAsString(int[] date) {
		String dateAsString = "";
		if (date != null && date.length >= 3) {
			dateAsString = date[2] + "/" + date[1] + "/" + date[0];
		}
		return dateAsString;
	}

	public String getTimeAsString(int[] date) {
		StringBuilder time = new StringBuilder("");
		if (date != null && date.length > 3) {
			for (int i = 3; i < date.length; i++) {
				time.append(date[i] + ":");
			}
		}
		return time.toString().substring(0, time.length() - 1);
	}

	private float getReoundedMeasure(double measure, int decimal) {
		BigDecimal bd = new BigDecimal(Double.toString(measure));
		bd = bd.setScale(decimal, BigDecimal.ROUND_HALF_UP);
		return bd.floatValue();
	}

}
