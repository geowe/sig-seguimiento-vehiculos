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
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.shared.rest.sgf.model.jso.VehicleJSO;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.ContentPanel;

@ApplicationScoped
public class VehicleToolBar extends ContentPanel {

	@Inject
	//@New
	private RouteVehicleTool routeVehicleTool;
	
	@Inject
	//@New
	private LastPointRegisterVehicleTool lastPointRegisterVehicleTool;
//	@Inject
//	@New
//	private SingleFeatureInfoTool singleFeatureInfoTool;
//	@Inject
//	@New
//	private SelectVectorFeatureTool selectVectorFeatureTool;
//	@Inject
//	@New
//	private ExportCSVLayerTool exportCSVLayerTool;
//	@Inject
//	private CreateNewLayerTool createNewLayerTool;

	private final List<VehicleButtonTool> tools;
	private final VerticalPanel verticalGroup;

	public VehicleToolBar() {
		super();
		setHeight("100px");
		setHeaderVisible(false);

		tools = new ArrayList<VehicleButtonTool>();
		verticalGroup = new VerticalPanel();
		verticalGroup.setSpacing(3);
	}

	@PostConstruct
	private void initialize() {
		addTool(routeVehicleTool);
		addTool(lastPointRegisterVehicleTool);
//		addTool(selectVectorFeatureTool);
//		addTool(singleFeatureInfoTool);
//		addTool(createNewLayerTool);
//		addTool(exportCSVLayerTool);
		setWidget(verticalGroup);
	}

	@Override
	public void addTool(Widget tool) {
		verticalGroup.add(tool);
		tools.add((VehicleButtonTool) tool);
	}

	public List<VehicleButtonTool> getTools() {
		return tools;
	}
		
	public void setVehicles(List<VehicleJSO> vehicles) {
		routeVehicleTool.setVehicles(vehicles);
		lastPointRegisterVehicleTool.setVehicles(vehicles);
	}
}
