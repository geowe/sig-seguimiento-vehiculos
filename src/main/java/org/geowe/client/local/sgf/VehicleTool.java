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

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.main.map.GeoMap;
import org.geowe.client.local.main.tool.ButtonTool;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.geowe.client.shared.rest.sgf.SGFVehicleService;
import org.geowe.client.shared.rest.sgf.model.jso.CompanyJSO;
import org.geowe.client.shared.rest.sgf.model.jso.SessionJSO;
import org.geowe.client.shared.rest.sgf.model.jso.VehicleJSO;
import org.geowe.client.shared.rest.sgf.model.jso.VehicleListResponseJSO;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.enterprise.client.jaxrs.api.RestClient;
import org.jboss.errai.enterprise.client.jaxrs.api.RestErrorCallback;

import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.google.inject.Inject;
import com.sencha.gxt.core.client.Style.Side;

/**
 * Attribute search tool.
 * 
 * @author geowe
 *
 */
@ApplicationScoped
public class VehicleTool extends ButtonTool {

	@Inject
	private VehicleDialog vehicleDialog;
	@Inject
	private LayerManagerWidget layerManagerWidget;
	
	@Inject
	private MessageDialogBuilder messageDialogBuilder;
	
	private SessionJSO session;

	public void setSession(SessionJSO session) {
		this.session = session;
	}

	@Inject
	public VehicleTool(GeoMap geoMap) {
		super("Listado de vehículos",
				ImageProvider.INSTANCE.search32());
		setToolTipConfig(createTooltipConfig(
				"Listado de vehículos",
				"Listado de vehículos de una compañía", Side.LEFT));
		setEnabled(false);
	}

	@Override
	protected void onRelease() {				
		CompanyJSO company = session.getCompany();				
		vehicleDialog.setCompany(company);
		loadVehicles(session.getToken(), company.getId());
	}
	
	
	private void loadVehicles(String token, int companyId) {
		VehicleListResponseJSO vehicleListResponse = JsonUtils.safeEval(SampleDataProvider.INSTANCE.listVehicle().getText());
		List<VehicleJSO> vehicles = Arrays.asList(vehicleListResponse.getVehicleListEmbededJSO().getVehicles());
		vehicleDialog.setVehicle(vehicles);
		vehicleDialog.show();
	}
	
	
	
//	private void loadVehicles(String token, int companyId) {
//		RestClient.create(SGFVehicleService.class, "http://10.79.213.50:8081",
//				new RemoteCallback<String>() {
//
//					@Override
//					public void callback(String vehicleListResponseJson) {												
//						VehicleListResponseJSO vehicleListResponse = JsonUtils.safeEval(vehicleListResponseJson);						
//						List<VehicleJSO> vehicles = Arrays.asList(vehicleListResponse.getVehicleListEmbededJSO().getVehicles());
//						
//						
//						
//						for(VehicleJSO vehicle: vehicles) {
//							messageDialogBuilder.createInfo("matricula: ",  "matricula: " + vehicle.getPlate()).show();
//						}
//
//					}
//				},
//
//				new RestErrorCallback() {
//					
//					@Override
//					public boolean error(Request message, Throwable throwable) {
//						//messageDialogBuilder.createError("Error", UISgfMessages.INSTANCE.authError()).show();
//						
//						messageDialogBuilder.createInfo("Error",  throwable.getMessage()).show();
//						
//						return false;
//					}
//				}, Response.SC_OK).get(token, companyId, 10, "id");
//			
//
//	}

	
}
