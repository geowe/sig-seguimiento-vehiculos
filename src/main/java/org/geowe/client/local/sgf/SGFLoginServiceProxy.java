package org.geowe.client.local.sgf;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.sgf.messages.UISgfMessages;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.geowe.client.local.welcome.Welcome;
import org.geowe.client.shared.rest.sgf.SGFService;
import org.geowe.client.shared.rest.sgf.SGFServiceAsync;
import org.geowe.client.shared.rest.sgf.model.jso.SessionJSO;
import org.slf4j.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsonUtils;
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
		
//		if(true) {
//			welcome.hideProgressImage();
//			welcome.hideDialog();
//			SessionJSO session = JsonUtils.safeEval(SampleDataProvider.INSTANCE.session().getText());
//			vehicleTool.setSession(session);			
//			vehicleTool.onRelease();			
//			
//			return;
//		}
		
		
		
		sgfServiceAsync.login(userName, password, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				messageDialogBuilder.createError("Error", UISgfMessages.INSTANCE.authError()).show();
				welcome.hideProgressImage();
			}

			@Override
			public void onSuccess(String sessionJson) {
				welcome.hideProgressImage();
				welcome.hideDialog();
				SessionJSO session = JsonUtils.safeEval(sessionJson);
				vehicleTool.setSession(session);	
				vehicleTool.onRelease();			
			}

		});
	}
}
