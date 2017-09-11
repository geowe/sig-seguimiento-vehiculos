package org.geowe.client.shared.rest.sgf.model.jso;

import com.google.gwt.core.client.JavaScriptObject;

public class PointRegisterJSO extends JavaScriptObject {
	
	protected PointRegisterJSO() {}
	
	public final native String getImei() /*-{ return this.imei; }-*/;
	public final native int[] getDate() /*-{ return this.date; }-*/;
	public final native String getSpeed() /*-{ return this.speed; }-*/; 
	public final native String getDatos() /*-{ return this.datos; }-*/;
	public final native String getPosition() /*-{ return this.position; }-*/;
	
		
}
