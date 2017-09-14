package org.geowe.client.shared.rest.sgf.model.jso;

import com.google.gwt.core.client.JavaScriptObject;

public class CompanyJSO extends JavaScriptObject {
	
	protected CompanyJSO() {}
	
	public final native int getId() /*-{ return this.id; }-*/;
	public final native int getResourceId() /*-{ return this.resourceId; }-*/;
	public final native String getCif() /*-{ return this.cif; }-*/; 
	public final native String getName() /*-{ return this.name; }-*/;
	public final native String getPostalCode() /*-{ return this.postalCode; }-*/;
	public final native String getEmail() /*-{ return this.email; }-*/;
	public final native String getWeb() /*-{ return this.web; }-*/;
	public final native String getAddress() /*-{ return this.address; }-*/;	
	
}
