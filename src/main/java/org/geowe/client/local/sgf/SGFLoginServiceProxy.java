package org.geowe.client.local.sgf;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.geowe.client.shared.rest.sgf.SGFService;
import org.geowe.client.shared.rest.sgf.model.SgfUser;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.enterprise.client.jaxrs.api.ResponseException;
import org.jboss.errai.enterprise.client.jaxrs.api.RestClient;
import org.jboss.errai.enterprise.client.jaxrs.api.RestErrorCallback;
import org.slf4j.Logger;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;

@ApplicationScoped
public class SGFLoginServiceProxy {
	//TODO: poner url como property
	private final static String URL_BASE = "http://localhost:8081";	
	@Inject
	private MessageDialogBuilder messageDialogBuilder;
	
	@Inject
	private Logger logger;
	

	//TODO: progress bar
	public void login(String payload){
		logger.info("Proxy payload:"+payload);
		RestClient.create(SGFService.class, URL_BASE,
				getRemoteCallback(), getErrorCallback(), Response.SC_OK)
				.login(payload);
		
	}
	
	//TODO: mejorar mensaje de error
	private RestErrorCallback getErrorCallback() {
		return new RestErrorCallback() {

			@Override
			public boolean error(Request request, Throwable throwable) {

				String message = "Not authenticated";
				int defaultCodeError = Response.SC_EXPECTATION_FAILED;
				logger.info("ERROR: "+defaultCodeError+throwable.getMessage());
				try {
					throw throwable;
					
				} catch (ResponseException e) {
					Response response = e.getResponse();
					message = response.getStatusText();
					defaultCodeError = response.getStatusCode();

				} catch (Throwable t) {
					//message = t.getStackTrace().toString();
				}
				
				messageDialogBuilder.createError(
						UIMessages.INSTANCE.warning() + " " + defaultCodeError,
						message).show();

				return false;
			}
		};
	}

	
	//TODO: Se necesita recoger la cabecera Authorization para el token JWT
	//a ver si se puede con un interceptor (implementado  listo para probar)
	private RemoteCallback<SgfUser> getRemoteCallback() {
		return new RemoteCallback<SgfUser>() {

			@Override
			public void callback(SgfUser response) {
				logger.info("RESPONSE: "+response.toString());
				
				messageDialogBuilder.createInfo(
						response.getName(),
						response.getUsername())
						.show();
			}

		};
	}
	
}
