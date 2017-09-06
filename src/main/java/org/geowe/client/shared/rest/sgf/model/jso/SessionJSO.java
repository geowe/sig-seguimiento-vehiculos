package org.geowe.client.shared.rest.sgf.model.jso;

import com.google.gwt.core.client.JavaScriptObject;

public class SessionJSO extends JavaScriptObject {
	
	protected SessionJSO() {}
	
	public final native int getId() /*-{ return this.id; }-*/;
	public final native String getName() /*-{ return this.name; }-*/; 
	public final native String getFirstSurname() /*-{ return this.firstSurname; }-*/;
	public final native String getSecondtSurname() /*-{ return this.secondSurname; }-*/;
	public final native String getUserName() /*-{ return this.username; }-*/;
	public final native String getPassowrd() /*-{ return this.password; }-*/;
	public final native String getStatus() /*-{ return this.status; }-*/;	
	public final native String getRole() /*-{ return this.role; }-*/;
	public final native String getToken() /*-{ return this.token; }-*/;
	public final native String getPhone() /*-{ return this.phone; }-*/;
	public final native String getEmail() /*-{ return this.email; }-*/;
	
	public final native CompanyJSO getCompany() /*-{ return this.company; }-*/;	
		
}
