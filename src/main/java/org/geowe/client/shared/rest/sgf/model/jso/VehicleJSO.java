package org.geowe.client.shared.rest.sgf.model.jso;

import com.google.gwt.core.client.JavaScriptObject;

public class VehicleJSO extends JavaScriptObject {
	
	protected VehicleJSO() {}
	
	public final native String getPlate() /*-{ return this.plate; }-*/;
	public final native int getKmsLeftForRevision() /*-{ return this.kmsLeftForRevision; }-*/;
	public final native String getLastRevisionDate() /*-{ return this.lastRevisionDate; }-*/; 
	public final native String getComments() /*-{ return this.comments; }-*/;
	public final native String getStatus() /*-{ return this.status; }-*/;
		
}
