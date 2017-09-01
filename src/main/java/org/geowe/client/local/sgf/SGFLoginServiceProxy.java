package org.geowe.client.local.sgf;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.sgf.messages.UISgfMessages;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.geowe.client.local.welcome.Welcome;
import org.geowe.client.shared.rest.sgf.SGFService;
import org.geowe.client.shared.rest.sgf.SGFServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
/**
 * 
 * @author lotor
 *
 */
@ApplicationScoped
public class SGFLoginServiceProxy {

	@Inject
	private MessageDialogBuilder messageDialogBuilder;


	@Inject
	private Welcome welcome;

	private final SGFServiceAsync sgfServiceAsync = GWT.create(SGFService.class);

	public void login(String userName, String password) {
		
		sgfServiceAsync.login(userName, password, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				messageDialogBuilder.createError("Error",
						UISgfMessages.INSTANCE.authError()).show();
				welcome.hideProgressImage();
			}

			@Override
			public void onSuccess(String result) {
				welcome.hideDialog();
				messageDialogBuilder.createInfo("SUCCESS", result).show();;

			}
		});
	}

}
