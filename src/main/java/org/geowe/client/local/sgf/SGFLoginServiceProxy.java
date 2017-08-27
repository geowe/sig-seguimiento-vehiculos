package org.geowe.client.local.sgf;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.geowe.client.shared.rest.sgf.SGFService;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.enterprise.client.jaxrs.api.ResponseException;
import org.jboss.errai.enterprise.client.jaxrs.api.RestClient;
import org.jboss.errai.enterprise.client.jaxrs.api.RestErrorCallback;
import org.slf4j.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;

@ApplicationScoped
public class SGFLoginServiceProxy {
	private final static String URL_BASE = "http://localhost:8081";	
	final String urlBase = GWT.getHostPageBaseURL() + "gwtOpenLayersProxy";
	@Inject
	private MessageDialogBuilder messageDialogBuilder;
	
	@Inject
	private Logger logger;


	public void login(String payload){
		logger.info("Proxy payload:"+payload);
		RestClient.create(SGFService.class, urlBase+"?"+URL_BASE,
				getRemoteCallback(), getErrorCallback(), Response.SC_OK)
				.login(payload);
	}
	
	//TODO: mejorar mensaje de error
	private RestErrorCallback getErrorCallback() {
		return new RestErrorCallback() {

			@Override
			public boolean error(Request request, Throwable throwable) {

				String message = "Not authenticated";
				int defaultCodeError = Response.SC_NOT_MODIFIED;
				try {
					throw throwable;
				} catch (ResponseException e) {
					Response response = e.getResponse();
					message = response.getStatusText();
					defaultCodeError = response.getStatusCode();

				} catch (Throwable t) {
					message = t.getMessage();
				}

				messageDialogBuilder.createError(
						UIMessages.INSTANCE.warning() + " " + defaultCodeError,
						message).show();

				return false;
			}
		};
	}

	//TODO: La respuesta será un objeto Usuario. Por ahora para probar
	//TODO: Se necesita recoger la cabecera Authorization para el token JWT
	private RemoteCallback<String> getRemoteCallback() {
		return new RemoteCallback<String>() {

			@Override
			public void callback(String response) {
				logger.info(response);
				
				messageDialogBuilder.createInfo(
						UIMessages.INSTANCE.gitHubResponseTitle(),
						response)
						.show();
			}

		};
	}
}
