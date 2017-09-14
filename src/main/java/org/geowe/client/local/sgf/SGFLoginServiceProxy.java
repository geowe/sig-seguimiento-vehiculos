package org.geowe.client.local.sgf;

import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.sgf.messages.UISgfMessages;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.geowe.client.local.welcome.Welcome;
import org.geowe.client.shared.rest.sgf.SGFCompanyService;
import org.geowe.client.shared.rest.sgf.SGFService;
import org.geowe.client.shared.rest.sgf.SGFServiceAsync;
import org.geowe.client.shared.rest.sgf.SGFVehicleService;
import org.geowe.client.shared.rest.sgf.model.Company;
import org.geowe.client.shared.rest.sgf.model.jso.CompanyJSO;
import org.geowe.client.shared.rest.sgf.model.jso.SessionJSO;
import org.geowe.client.shared.rest.sgf.model.jso.VehicleJSO;
import org.geowe.client.shared.rest.sgf.model.jso.VehicleListResponseJSO;
import org.geowe.client.shared.rest.sgf.model.pageable.company.CompanyResponse;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.enterprise.client.jaxrs.api.RestClient;
import org.jboss.errai.enterprise.client.jaxrs.api.RestErrorCallback;
import org.slf4j.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * @author lotor
 *
 */
@ApplicationScoped
public class SGFLoginServiceProxy {
	
	@Inject
	private Logger logger;

	@Inject
	private MessageDialogBuilder messageDialogBuilder;

	@Inject
	private Welcome welcome;
	
	@Inject
	private VehicleTool vehicleTool;

	private final SGFServiceAsync sgfServiceAsync = GWT.create(SGFService.class);

	public void login(String userName, String password) {
		
		if(true) {
			welcome.hideProgressImage();
			welcome.hideDialog();
			SessionJSO session = JsonUtils.safeEval(SampleDataProvider.INSTANCE.session().getText());
			vehicleTool.setSession(session);			
			vehicleTool.onRelease();
			
			
			
//			CompanyJSO company = session.getCompany();
			//messageDialogBuilder.createInfo("Bienvenido", session.getFirstSurname() + " de la compay: " + company.getName() + "[" + company.getCif()+ "]" ).show();
			
//			VehicleListResponseJSO vehicleListResponse = JsonUtils.safeEval(SampleDataProvider.INSTANCE.listVehicle().getText());
//			
//			List<VehicleJSO> vehicles = Arrays.asList(vehicleListResponse.getVehicleListEmbededJSO().getVehicles());
//			
//			messageDialogBuilder.createInfo("vehiculos: ",  "Num vehiculos: " + vehicles.size()).show();
//			
//			for(VehicleJSO vehicle: vehicles) {
//				messageDialogBuilder.createInfo("matricula: ",  "matricula: " + vehicle.getPlate()).show();
//			}
			
			return;
		}
		
		
		
//		sgfServiceAsync.login(userName, password, new AsyncCallback<String>() {
//
//			@Override
//			public void onFailure(Throwable caught) {
//				messageDialogBuilder.createError("Error", UISgfMessages.INSTANCE.authError()).show();
//				welcome.hideProgressImage();
//			}
//
//			@Override
//			public void onSuccess(String sessionJson) {
//				welcome.hideProgressImage();
//				welcome.hideDialog();
//				SessionJSO session = JsonUtils.safeEval(sessionJson);
//				CompanyJSO company = session.getCompany();				
//				getVehicles(session.getToken(), company.getId());				
//			}
//
//		});
	}

	
	
	private void getVehicles(String token, int companyId) {
		RestClient.create(SGFVehicleService.class, "http://10.79.213.50:8081",
				new RemoteCallback<String>() {

					@Override
					public void callback(String vehicleListResponseJson) {												
						VehicleListResponseJSO vehicleListResponse = JsonUtils.safeEval(vehicleListResponseJson);						
						List<VehicleJSO> vehicles = Arrays.asList(vehicleListResponse.getVehicleListEmbededJSO().getVehicles());
						
						
						
						for(VehicleJSO vehicle: vehicles) {
							messageDialogBuilder.createInfo("matricula: ",  "matricula: " + vehicle.getPlate()).show();
						}

					}
				},

				new RestErrorCallback() {
					
					@Override
					public boolean error(Request message, Throwable throwable) {
						//messageDialogBuilder.createError("Error", UISgfMessages.INSTANCE.authError()).show();
						
						messageDialogBuilder.createInfo("Error",  throwable.getMessage()).show();
						
						return false;
					}
				}, Response.SC_OK).get(token, companyId, 10, "id");
			

	}
	
	// TODO: TEST
	private void getCompanies() {
		RestClient.create(SGFCompanyService.class, "http://10.79.213.50:8081",
				new RemoteCallback<CompanyResponse>() {

					@Override
					public void callback(CompanyResponse response) {
						
						for(Company point : response.getContent().getResourceList()){
							logger.info(point.toString());
						}
						messageDialogBuilder
								.createInfo("SUCCESS",
										Integer.toString(
												response.getContent().getResourceList().size()))
								.show();

					}
				},

				new RestErrorCallback() {
					
					@Override
					public boolean error(Request message, Throwable throwable) {
						messageDialogBuilder.createError("Error", UISgfMessages.INSTANCE.authError()).show();
						return false;
					}
				}, Response.SC_OK)
				.get("Bearer  eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiAxIiwiZXhwIjoxNTA1NDYyMTkzfQ.s27ukXwzbttB_G6kkaHBA0024I7MO8N6aicD4RooLORBXnSdovZswF6I6EHTM75GDPEiOa1rF0I5XcrN4_ULJA",
						10, "id");

	}

}
