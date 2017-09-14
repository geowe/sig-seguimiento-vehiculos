package org.geowe.client.shared.rest.sgf.model.jso;

import com.google.gwt.core.client.JavaScriptObject;


public class VehicleListResponseJSO extends JavaScriptObject {
	
	protected VehicleListResponseJSO() {}
	
	public final native VehicleListEmbededJSO getVehicleListEmbededJSO() /*-{ return this._embedded; }-*/;
	public final native PageJSO gePageJSO() /*-{ return this.page; }-*/;	
	
}
