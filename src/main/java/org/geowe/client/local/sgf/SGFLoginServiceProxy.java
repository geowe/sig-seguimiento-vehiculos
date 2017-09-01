package org.geowe.client.local.sgf;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.sgf.messages.UISgfMessages;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.geowe.client.local.welcome.Welcome;
import org.geowe.client.shared.rest.sgf.SGFRegisteredPointService;
import org.geowe.client.shared.rest.sgf.SGFService;
import org.geowe.client.shared.rest.sgf.SGFServiceAsync;
import org.geowe.client.shared.rest.sgf.model.RegisteredPoint;
import org.geowe.client.shared.rest.sgf.model.pageable.RegisteredPointPage;
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
		RestClient.create(SGFRegisteredPointService.class, "http://127.0.0.1:8081",
				new RemoteCallback<RegisteredPointPage>() {

					@Override
					public void callback(RegisteredPointPage response) {
						
						for(RegisteredPoint point : response.getContent().getResourceList()){
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
				.getAll("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiAxIiwiZXhwIjoxNTA1MTY1OTQwfQ.5S63d0Yltd3l8AUnK4_nibbBONoFpeTQJzp-UrFNYp64hm4TpnGUjvNYkeX3qe77wYzv092UJNy3_WXyPhnpUw",
						10, "id");

	}

}
