package org.geowe.client.shared.rest.sgf;

import com.google.gwt.user.client.rpc.AsyncCallback;


public interface SGFServiceAsync {

	void login(String user, String password, AsyncCallback<String> callback)
			throws IllegalArgumentException;

}
