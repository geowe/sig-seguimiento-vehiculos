package org.geowe.client.shared.rest.sgf.model.jso;

import com.google.gwt.core.client.JavaScriptObject;

public class ActiveGPSJSO extends JavaScriptObject {
	
	protected ActiveGPSJSO() {}
	
	public final native String getImei() /*-{ return this.imei; }-*/;
	public final native String getStatus() /*-{ return this.status; }-*/;
	
}
