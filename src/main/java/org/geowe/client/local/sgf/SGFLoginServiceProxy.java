package org.geowe.client.local.sgf;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.sgf.messages.UISgfMessages;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.geowe.client.local.welcome.Welcome;
import org.geowe.client.shared.rest.sgf.SGFCompanyService;
import org.geowe.client.shared.rest.sgf.SGFService;
import org.geowe.client.shared.rest.sgf.SGFServiceAsync;
import org.geowe.client.shared.rest.sgf.model.Company;
import org.geowe.client.shared.rest.sgf.model.pageable.company.CompanyResponse;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.enterprise.client.jaxrs.api.RestClient;
import org.jboss.errai.enterprise.client.jaxrs.api.RestErrorCallback;
import org.slf4j.Logger;

import com.google.gwt.core.client.GWT;
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

	private final SGFServiceAsync sgfServiceAsync = GWT.create(SGFService.class);

	public void login(String userName, String password) {

		sgfServiceAsync.login(userName, password, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				messageDialogBuilder.createError("Error", UISgfMessages.INSTANCE.authError()).show();
				welcome.hideProgressImage();
			}

			@Override
			public void onSuccess(String result) {
				welcome.hideDialog();
				messageDialogBuilder.createInfo("SUCCESS", result).show();

				getRegisteredPoints();

			}

		});
	}

	// TODO: TEST
	private void getRegisteredPoints() {
		RestClient.create(SGFCompanyService.class, "http://127.0.0.1:8081",
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
				.get("Bearer  eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiAxIiwiZXhwIjoxNTA1MjA5OTE3fQ.QLnlVDrqt9pAmAlCIDNIyulepnxZB9qd7q5RVZQFTj-hbZN_wkMjxHg_kM6uBmAoab9tybXpBw8vcCRV0rPQ5w",
						10, "id");

	}

}
