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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.layermanager.tool.LayerTool;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.model.vector.VectorLayer;
import org.geowe.client.local.model.vector.VectorLayerConfig;
import org.geowe.client.local.model.vector.VectorLayerFactory;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.geowe.client.shared.rest.sgf.model.jso.PointRegisterJSO;
import org.geowe.client.shared.rest.sgf.model.jso.PointRegisterListResponseJSO;
import org.geowe.client.shared.rest.sgf.model.jso.VehicleJSO;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.format.WKT;
import org.gwtopenmaps.openlayers.client.geometry.Geometry;
import org.gwtopenmaps.openlayers.client.geometry.LineString;
import org.gwtopenmaps.openlayers.client.geometry.Point;

import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.DateField;

/**
 * Herramienta para trazar en el mapa la ruta de puntos GPS de un vehículo
 * @author jose@geowe.org
 * 
 */
@ApplicationScoped
public class RouteVehicleTool extends LayerTool implements VehicleButtonTool {
	private DateField field = new DateField();
	private VehicleJSO vehicleJSO;
	
	public void setVehicleJSO(VehicleJSO vehicleJSO) {
		this.vehicleJSO = vehicleJSO;
	}

	@Inject
	private MessageDialogBuilder messageDialogBuilder;

	@Inject
	public RouteVehicleTool(LayerManagerWidget layerTreeWidget, GeoMap geoMap) {
		super(layerTreeWidget, geoMap);
	}

	@Override
	public String getName() {
		return "Draw route";
	}
	
	@Override
	public ImageResource getIcon() {
		return ImageProvider.INSTANCE.lineString();
	}

	@Override
	public void onClick() {			
			field.setEditable(false);
	        final Dialog box = new Dialog();
	        box.setHeadingText("Date");
	        box.add(field);
	        box.setModal(true);
	        box.setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
	        box.setHideOnButtonClick(true);
	        box.getButton(PredefinedButton.OK).addSelectHandler(
					new SelectHandler() {
						@Override
						public void onSelect(final SelectEvent event) {
							messageDialogBuilder.createInfo("Atención", "fecha: " + field.getText()).show();
							
							VectorLayerConfig layerConfig = null;
							VectorLayer routeLayer = null;

							try {
								layerConfig = new VectorLayerConfig();
								layerConfig.setEpsg("EPSG:4326");
								layerConfig.setLayerName("Route_" + vehicleJSO.getPlate());								
								
								routeLayer = VectorLayerFactory.createEmptyVectorLayer(layerConfig);								
								routeLayer.addAttribute("IMEI", false);
								routeLayer.addAttribute("FECHA", false);
								routeLayer.addAttribute("HORA", false);
								routeLayer.addAttribute("VELOCIDAD", false);
								routeLayer.addAttribute("DATOS", false);
								routeLayer.addAttribute("POSICION", false);
								

//								layer = VectorLayerFactory.createVectorLayerFromGeoData(layerConfig);

							} catch (Exception e) {
								//showAlert(UIMessages.INSTANCE.gditAlertMessage());
								messageDialogBuilder.createInfo("Error", e.getMessage()).show();
							}

							
							
							
							
							PointRegisterListResponseJSO pointRegisterResponse = JsonUtils.safeEval(SampleDataProvider.INSTANCE.listRegisterPoint().getText());							
							PointRegisterJSO[] pointRegisters = pointRegisterResponse.getPointRegisterListEmbededJSO().getPointRegister();							
							List<PointRegisterJSO> points = Arrays.asList(pointRegisters);
							
							//messageDialogBuilder.createInfo("Error", "SIZE: " + points.size()).show();
							
							layerManagerWidget.addVector(routeLayer);
							layerManagerWidget.setSelectedLayer(LayerManagerWidget.VECTOR_TAB, routeLayer);	
							
							
							WKT reader = new WKT();
							List<Point> pointList = new ArrayList<Point>();
							
							
							for(PointRegisterJSO point: points) {
																
								VectorFeature f = reader.read(point.getPosition())[0];	
								
								Geometry g = f.getGeometry();
								Point p = Point.narrowToPoint(g.getJSObject());
								String position = p.getX() + " , " + p.getY();
								
								f.getGeometry().transform(layerConfig.getProjection(),
										layerConfig.getDefaultProjection());
								
								
								routeLayer.addFeature(f);
								
								
								
								f.getAttributes().setAttribute("IMEI", point.getImei());
								f.getAttributes().setAttribute("FECHA", getDateAsString(point.getDate()));
								f.getAttributes().setAttribute("HORA", getTimeAsString(point.getDate()));
								f.getAttributes().setAttribute("VELOCIDAD", point.getSpeed());
								f.getAttributes().setAttribute("DATOS", point.getDatos());
								f.getAttributes().setAttribute("POSICION", position);
								
								g = f.getGeometry();
//								if (g.getClassName().equals(
//						                org.gwtopenmaps.openlayers.client.geometry.Geometry.POINT_CLASS_NAME)) {
						             p = Point.narrowToPoint(g.getJSObject());
						            pointList.add(p);
//								} 
								
								
							
								
							}
							
							//messageDialogBuilder.createInfo("Error", "feature SIZE: " + routeLayer.getFeatures().length).show();
							
							LineString line = new LineString(pointList.toArray(new Point[]{}));
							
							
						        final VectorFeature lineFeature = new VectorFeature(line);
						        routeLayer.addFeature(lineFeature);
						        
						        routeLayer.getStyle().setStrokeColor("#339933");
						        routeLayer.getStyle().setStrokeWidth(3);
						        routeLayer.getStyle().setPointRadius(6);
							
						        geoMap.getMap().zoomToExtent(routeLayer.getDataExtent());
							
							
						}
					});
	        box.show();
	}
	
	
	//https://docs.sencha.com/gxt/4.x/guides/ui/fields/DateField.html
	
	public String getDateAsString(int[] date) {
		String dateAsString = "";
		if (date != null && date.length >= 3) {
			dateAsString = date[0] + "-" + date[1] + "-" + date[2];
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
		return time.toString();
	}
	

	
}
