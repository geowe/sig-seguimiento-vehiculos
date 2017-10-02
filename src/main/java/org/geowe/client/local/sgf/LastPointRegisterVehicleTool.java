/*
 * #%L
 * GeoWE Project
 * %%
 * Copyright (C) 2015 - 2016 GeoWE.org
 * %%
 * This file is part of GeoWE.org.
 * 
 * GeoWE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * GeoWE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with GeoWE.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */
package org.geowe.client.local.sgf;

import java.util.Arrays;
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
import org.gwtopenmaps.openlayers.client.LonLat;
import org.gwtopenmaps.openlayers.client.Style;
import org.gwtopenmaps.openlayers.client.StyleMap;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.format.WKT;
import org.gwtopenmaps.openlayers.client.geometry.Geometry;
import org.gwtopenmaps.openlayers.client.geometry.Point;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.common.client.api.tasks.ClientTaskManager;
import org.jboss.errai.enterprise.client.jaxrs.api.RestClient;
import org.jboss.errai.enterprise.client.jaxrs.api.RestErrorCallback;

import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.google.gwt.resources.client.ImageResource;

/**
 * Herramienta para localizar en el mapa la última posición GPS registrada de un vehículo
 * 
 * @author jose@geowe.org
 * 
 */
@ApplicationScoped
public class LastPointRegisterVehicleTool extends LayerTool implements
		VehicleButtonTool {
		
	private static final String GPS_DEFAULT_PROJECTION = "EPSG:4326";
	private static final String LAYER_NAME = UISgfMessages.INSTANCE.vehicleLayerName();
	private static final String NAME = UISgfMessages.INSTANCE.nameColumn();
	private static final String DATE = UISgfMessages.INSTANCE.dateColumn();
	private static final String TIME = UISgfMessages.INSTANCE.timeColumn();
	private static final String SPEED = UISgfMessages.INSTANCE.speedColumn();
	private static final String DATA = UISgfMessages.INSTANCE.dataColumn();
	private static final String POSITION = UISgfMessages.INSTANCE.positionColumn();
	private static final String DISTANCE = UISgfMessages.INSTANCE.distanceColumn();
	private static final String STREET = UISgfMessages.INSTANCE.streetColumn();
	private static final String NUMBER = UISgfMessages.INSTANCE.numberColumn();
	private static final String LOCALITY = UISgfMessages.INSTANCE.localityColumn();
	private static final String PROVINCE = UISgfMessages.INSTANCE.provinceColumn();
	private static final String POSTAL_CODE = UISgfMessages.INSTANCE.postalCodeColumn();
	private static final String COUNTRY = UISgfMessages.INSTANCE.countryColumn();

	private static final String PLATE = UISgfMessages.INSTANCE.plateColumn();
	private static final String KM_REVISION = UISgfMessages.INSTANCE.kmForReviewColumn();
	private static final String LAST_REVISION = UISgfMessages.INSTANCE.lastReviewColumn();
	private static final String COMMENT = UISgfMessages.INSTANCE.commentColumn();
	private static final String STATUS = UISgfMessages.INSTANCE.statusColumn();
	private static final String ACTIVE = UISgfMessages.INSTANCE.activeColumn();
	
	private SessionJSO session;

	private List<VehicleJSO> vehicles;
	private ProgressBarDialog autoMessageBox;
	
	@Inject
	private SGFServiceInfo SGFServiceInfo;
	
	@Inject
	private MessageDialogBuilder messageDialogBuilder;

	@Inject
	private ClientTaskManager taskManager;


	@Inject
	public LastPointRegisterVehicleTool(LayerManagerWidget layerTreeWidget,
			GeoMap geoMap) {
		super(layerTreeWidget, geoMap);
		setEnabled(false);
	}

	@Override
	public String getName() {
		return UISgfMessages.INSTANCE.lastRegisterPoint();
	}

	@Override
	public ImageResource getIcon() {
		return ImageProvider.INSTANCE.searchGeocoding24();
	}

	@Override
	public void onClick() {
		
		autoMessageBox = new ProgressBarDialog(false,
				UIMessages.INSTANCE.processing());
		autoMessageBox.show();

		taskManager.execute(new Runnable() {

			@Override
			public void run() {
				for (VehicleJSO vehicle : vehicles) {
					
					//PointRegisterJSO point = getSamplePoint(vehicle);
					getRequestRegisteredPoint(session.getToken(),vehicle);
				}
			}
		});
	}
			
				
	private void getRequestRegisteredPoint(String token,
			final VehicleJSO vehicle) {
		autoMessageBox.setProgressStatusMessage(UISgfMessages.INSTANCE
				.getGPSData());

		RestClient.create(SGFRegisteredPointService.class,
				SGFServiceInfo.getURL(), new RemoteCallback<String>() {

					@Override
					public void callback(String pointRegisterListResponseJson) {

						PointRegisterListResponseJSO pointRegisterResponse = JsonUtils
								.safeEval(pointRegisterListResponseJson);
						PointRegisterJSO[] pointRegisters = pointRegisterResponse
								.getPointRegisterListEmbededJSO()
								.getPointRegister();
						List<PointRegisterJSO> points = Arrays
								.asList(pointRegisters);

						if (points.isEmpty()) {
							messageDialogBuilder.createInfo(
									UIMessages.INSTANCE.edtAlertDialogTitle(),
									UISgfMessages.INSTANCE.gpsDataNotFound())
									.show();
							return;
						}

						createLastPointRegisterLayer(vehicle, points.get(0));
						autoMessageBox.hide();
					}
				},

				new RestErrorCallback() {

					@Override
					public boolean error(Request message, Throwable throwable) {
						autoMessageBox.hide();
						messageDialogBuilder.createInfo(
								UISgfMessages.INSTANCE.errorDetected(),
								throwable.getMessage()).show();

						return false;
					}
				}, Response.SC_OK).getLastRegisteredPoints(token,
				vehicle.getId(), 1, "date,desc");
	}
		

	public void setVehicles(List<VehicleJSO> vehicles) {
		setEnabled(true);
		this.vehicles = vehicles;
	}

	private void createLastPointRegisterLayer(VehicleJSO vehicleJSO,
			PointRegisterJSO point) {
		VectorLayerConfig layerConfig = createVectorLayerConfig(vehicleJSO);

		VectorLayer vehicleLayer = (VectorLayer) layerManagerWidget
				.getVector(LAYER_NAME);
		if (vehicleLayer == null) {
			vehicleLayer = createEmptyPointRegisterLayer(layerConfig);
			vehicleLayer.setStyleMap(getStyleMap());
			layerManagerWidget.addVector(vehicleLayer);
		}
		else {
			removeLastPoint(vehicleLayer, vehicleJSO.getPlate());
		}
		
		WKT reader = new WKT();

		VectorFeature f = reader.read(point.getPosition())[0];

		Geometry g = f.getGeometry();
		Point currentPoint = Point.narrowToPoint(g.getJSObject());
		String position = "lon: " + currentPoint.getX() + " lat: "
				+ currentPoint.getY();

		f.getGeometry().transform(layerConfig.getProjection(),
				layerConfig.getDefaultProjection());

		vehicleLayer.addFeature(f);

		f.getAttributes().setAttribute(NAME, vehicleJSO.getName());
		f.getAttributes().setAttribute(PLATE, vehicleJSO.getPlate());
		f.getAttributes().setAttribute(DATE, getDateAsString(point.getDate()));
		f.getAttributes().setAttribute(TIME, getTimeAsString(point.getDate()));
		f.getAttributes().setAttribute(POSITION, position);
		f.getAttributes().setAttribute(STREET, point.getStreet());
		f.getAttributes().setAttribute(NUMBER, point.getNumber());
		f.getAttributes().setAttribute(LOCALITY, point.getLocality());
		f.getAttributes().setAttribute(PROVINCE, point.getProvince());
		f.getAttributes().setAttribute(POSTAL_CODE, point.getPostalCode());
		f.getAttributes().setAttribute(COUNTRY, point.getCountry());

		String active = "NO";
		if("ACTIVE".equals(vehicleJSO.getStatus())) {
			active = UISgfMessages.INSTANCE.yesValue();
		}
		f.getAttributes().setAttribute(ACTIVE, active);

		f.getAttributes().setAttribute(STATUS, getStatus(point.getDatos())); //PARADA/MARCHA

		layerManagerWidget.setSelectedLayer(LayerManagerWidget.VECTOR_TAB,
				vehicleLayer);
		
		g = f.getGeometry();
		currentPoint = Point.narrowToPoint(g.getJSObject());
		final LonLat lonLat = new LonLat(currentPoint.getX(), currentPoint.getY());
		geoMap.getMap().panTo(lonLat);
		geoMap.getMap().setCenter(lonLat, 20);
	}
	
	private String getStatus(String datos){
		String status = "undefined";
		if (datos.contains("1=1")) {
			status = UISgfMessages.INSTANCE.marchingValue();
		} else if (datos.contains("1=0")) {
			status = UISgfMessages.INSTANCE.stoppedValue();
		}
		return status;
	}

	private VectorLayerConfig createVectorLayerConfig(VehicleJSO vehicleJSO) {

		VectorLayerConfig layerConfig = new VectorLayerConfig();
		layerConfig.setEpsg(GPS_DEFAULT_PROJECTION);
		layerConfig.setLayerName(LAYER_NAME);

		return layerConfig;
	}

	private VectorLayer createEmptyPointRegisterLayer(
			VectorLayerConfig layerConfig) {

		VectorLayer routeLayer = null;

		try {

			routeLayer = VectorLayerFactory.createEmptyVectorLayer(layerConfig);
			routeLayer.addAttribute(NAME, false);
			routeLayer.addAttribute(PLATE, false);
			routeLayer.addAttribute(DATE, false);
			routeLayer.addAttribute(TIME, false);
			routeLayer.addAttribute(STREET, false);
			routeLayer.addAttribute(NUMBER, false);
			routeLayer.addAttribute(LOCALITY, false);
			routeLayer.addAttribute(PROVINCE, false);
			routeLayer.addAttribute(POSTAL_CODE, false);
			routeLayer.addAttribute(COUNTRY, false);
			routeLayer.addAttribute(STATUS, false);
			routeLayer.addAttribute(ACTIVE, false);
			routeLayer.addAttribute(POSITION, false);

		} catch (Exception e) {
			messageDialogBuilder.createInfo("Error", e.getMessage()).show();
		}

		return routeLayer;
	}

	private PointRegisterJSO getSamplePoint(VehicleJSO vehicleJSO) {
		String data = SampleDataProvider.INSTANCE.lastPointRegister()
				.getText();
		
		PointRegisterListResponseJSO pointRegisterResponse = JsonUtils
				.safeEval(data);
		
		PointRegisterJSO point = pointRegisterResponse
				.getPointRegisterListEmbededJSO().getPointRegister()[0];
				
		return point;
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
	
	private void removeLastPoint(VectorLayer vehicleLayer, String plate) {
		List<VectorFeature> features = Arrays.asList(vehicleLayer.getFeatures());
		
		for(VectorFeature feature: features) {
			String featurePlate = feature.getAttributes().getAttributeAsString(PLATE);
			if(plate.equals(featurePlate)) {
				vehicleLayer.removeFeature(feature);
				break;
			}			
		}
	}
	
	private StyleMap getStyleMap() {				
		final Style style = createStyle(ImageProvider.INSTANCE.markerCarRed24()
				.getSafeUri().asString());

		final Style hoverStyle = createStyle(ImageProvider.INSTANCE.markerCarBlue24()
				.getSafeUri().asString());

		final Style selectStyle = createStyle(ImageProvider.INSTANCE.markerCarGreen24()
				.getSafeUri().asString());

		return new StyleMap(style, selectStyle, hoverStyle);
	}

	private Style createStyle(final String imageUrl) {
		final Style style = new Style();
		style.setGraphicOpacity(1);
		style.setGraphicSize(24, 30);
		style.setGraphicOffset(-12, -30);
		style.setExternalGraphic(imageUrl);

		style.setBackgroundGraphic(ImageProvider.INSTANCE.w3wShadow()
				.getSafeUri().asString());
		style.setBackgroundOffset(0, -28);
		return style;
	}
	
	public void setSession(SessionJSO session) {
		this.session = session;
	}
}
