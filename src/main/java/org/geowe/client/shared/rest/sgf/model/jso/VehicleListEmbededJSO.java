package org.geowe.client.shared.rest.sgf.model.jso;

import com.google.gwt.core.client.JavaScriptObject;

public class VehicleListEmbededJSO extends JavaScriptObject {
	
	protected VehicleListEmbededJSO() {}
	
	public final native VehicleJSO[] getVehicles() /*-{ return this.vehicleResourceList; }-*/;
		
	
}
