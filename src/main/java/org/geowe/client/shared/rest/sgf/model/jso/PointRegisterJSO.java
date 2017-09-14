package org.geowe.client.shared.rest.sgf.model.jso;

import com.google.gwt.core.client.JavaScriptObject;

public class PointRegisterJSO extends JavaScriptObject {
	
	protected PointRegisterJSO() {}
	
	public final native String getImei() /*-{ return this.imei; }-*/;
	public final native int[] getDate() /*-{ return this.date; }-*/;
	public final native String getSpeed() /*-{ return this.speed; }-*/; 
	public final native String getDatos() /*-{ return this.datos; }-*/;
	public final native String getPosition() /*-{ return this.position; }-*/;
	public final native String getStreet() /*-{ return this.street; }-*/;
	public final native String getNumber() /*-{ return this.number; }-*/;
	public final native String getLocality() /*-{ return this.locality; }-*/;
	public final native String getProvince() /*-{ return this.province; }-*/; 
	public final native String getPostalCode() /*-{ return this.postalCode; }-*/; 
	public final native String getCountry() /*-{ return this.country; }-*/;
	     
}
