package org.geowe.client.local.sgf.messages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

public interface UISgfMessages extends Messages {

	UISgfMessages INSTANCE = GWT.create(UISgfMessages.class);
	
	String authError();
}
