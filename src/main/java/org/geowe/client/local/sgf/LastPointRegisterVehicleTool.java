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
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.geowe.client.local.ui.ProgressBarDialog;
import org.geowe.client.shared.rest.sgf.model.jso.PointRegisterJSO;
import org.geowe.client.shared.rest.sgf.model.jso.PointRegisterListResponseJSO;
import org.geowe.client.shared.rest.sgf.model.jso.VehicleJSO;
import org.gwtopenmaps.openlayers.client.LonLat;
import org.gwtopenmaps.openlayers.client.Style;
import org.gwtopenmaps.openlayers.client.StyleMap;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.format.WKT;
import org.gwtopenmaps.openlayers.client.geometry.Geometry;
import org.gwtopenmaps.openlayers.client.geometry.Point;
import org.jboss.errai.common.client.api.tasks.ClientTaskManager;

import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.resources.client.ImageResource;

/**
 * Herramienta para trazar en el mapa la ruta de puntos GPS de un vehículo
 * 
 * @author jose@geowe.org
 * 
 */
@ApplicationScoped
public class LastPointRegisterVehicleTool extends LayerTool implements
		VehicleButtonTool {
	
	private static final String LAYER_NAME = "Vehiculos";
	private static final String GPS_DEFAULT_PROJECTION = "EPSG:4326";
	private static final String IMEI = "IMEI";
	private static final String DATE = "FECHA";
	private static final String TIME = "HORA";
	private static final String SPEED = "VEL(Km/h)";
	private static final String POSITION = "POSICION";
	private static final String STREET = "CALLE";
	private static final String NUMBER = "Nº";
	private static final String LOCALITY = "LOCALIDAD";
	private static final String PROVINCE = "PROVINCIA";
	private static final String POSTAL_CODE = "CP";
	private static final String COUNTRY = "PAIS";

	private static final String PLATE = "MATRICULA";
	private static final String KM_REVISION = "KM REVI.";
	private static final String LAST_REVISION = "ULT. REVI.";
	private static final String COMMENT = "COMENTARIO";
	private static final String STATUS = "ESTADO";
	private static final String DATA = "DATOS";

	private List<VehicleJSO> vehicles;
	private ProgressBarDialog autoMessageBox;

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
		return "Último punto registrado";
	}

	@Override
	public ImageResource getIcon() {
		return ImageProvider.INSTANCE.searchGeocoding24();
	}

	@Override
	public void onClick() {
		DateTimeFormat fmt = DateTimeFormat.getFormat("yyyy-MM-dd");
		Date today = new Date();
		
		final String currentDate = fmt.format(today);
		autoMessageBox = new ProgressBarDialog(false,
				UIMessages.INSTANCE.processing());
		autoMessageBox.show();

		taskManager.execute(new Runnable() {

			@Override
			public void run() {
				for (VehicleJSO vehicle : vehicles) {
					createLastPointRegisterLayer(vehicle, currentDate);
				}
				autoMessageBox.hide();
			}
		});

	}

	public void setVehicles(List<VehicleJSO> vehicles) {
		setEnabled(true);
		this.vehicles = vehicles;
	}

	private void createLastPointRegisterLayer(VehicleJSO vehicleJSO,
			String dateToSearch) {
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

		PointRegisterJSO point = getSamplePoint(vehicleJSO);

		if (point == null) {
			messageDialogBuilder
					.createInfo("Atención",
							"No se encuentran datos registrados para la fecha especificada")
					.show();
			return;
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

		f.getAttributes().setAttribute(IMEI, point.getImei());
		f.getAttributes().setAttribute(DATE, getDateAsString(point.getDate()));
		f.getAttributes().setAttribute(TIME, getTimeAsString(point.getDate()));
		int speed = Double.valueOf(point.getSpeed()).intValue();

		f.getAttributes().setAttribute(SPEED, speed);
		f.getAttributes().setAttribute(POSITION, position);

		f.getAttributes().setAttribute(STREET, point.getStreet());
		f.getAttributes().setAttribute(NUMBER, point.getNumber());
		f.getAttributes().setAttribute(LOCALITY, point.getLocality());
		f.getAttributes().setAttribute(PROVINCE, point.getProvince());
		f.getAttributes().setAttribute(POSTAL_CODE, point.getPostalCode());
		f.getAttributes().setAttribute(COUNTRY, point.getCountry());
		
		f.getAttributes().setAttribute(PLATE, vehicleJSO.getPlate());
		f.getAttributes().setAttribute(KM_REVISION, vehicleJSO.getKmsLeftForRevision());
		f.getAttributes().setAttribute(LAST_REVISION, vehicleJSO.getLastRevisionDate());
		f.getAttributes().setAttribute(COMMENT, vehicleJSO.getComments());
		f.getAttributes().setAttribute(STATUS, vehicleJSO.getStatus());
		// TODO: pendiente de modelar los datos
		f.getAttributes()
				.setAttribute(DATA, point.getDatos().replace(",", " "));

//		String color = vehicleLayer.getVectorStyle().getFill().getNormalColor();
//		vehicleLayer.getVectorStyle().getLine().setNormalColor(color);
//		vehicleLayer.getVectorStyle().getLine().setThickness(3);
		
		
		//vehicleLayer.redraw();
		layerManagerWidget.setSelectedLayer(LayerManagerWidget.VECTOR_TAB,
				vehicleLayer);
		//geoMap.getMap().zoomToExtent(vehicleLayer.getDataExtent());
		
		g = f.getGeometry();
		currentPoint = Point.narrowToPoint(g.getJSObject());
		final LonLat lonLat = new LonLat(currentPoint.getX(), currentPoint.getY());
		geoMap.getMap().panTo(lonLat);
		geoMap.getMap().setCenter(lonLat, 20);
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
			routeLayer.addAttribute(PLATE, false);
			routeLayer.addAttribute(IMEI, false);
			routeLayer.addAttribute(DATE, false);
			routeLayer.addAttribute(TIME, false);
			routeLayer.addAttribute(SPEED, false);

			routeLayer.addAttribute(POSITION, false);

			routeLayer.addAttribute(STREET, false);
			routeLayer.addAttribute(NUMBER, false);
			routeLayer.addAttribute(LOCALITY, false);
			routeLayer.addAttribute(PROVINCE, false);
			routeLayer.addAttribute(POSTAL_CODE, false);
			routeLayer.addAttribute(COUNTRY, false);
			
			routeLayer.addAttribute(KM_REVISION, false);
			routeLayer.addAttribute(LAST_REVISION, false);
			routeLayer.addAttribute(COMMENT, false);
			routeLayer.addAttribute(STATUS, false);
			routeLayer.addAttribute(DATA, false);

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

	// https://docs.sencha.com/gxt/4.x/guides/ui/fields/DateField.html

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
}
