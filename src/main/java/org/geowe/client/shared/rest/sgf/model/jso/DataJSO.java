package org.geowe.client.shared.rest.sgf.model.jso;

import com.google.gwt.core.client.JavaScriptObject;

public class DataJSO extends JavaScriptObject {
	
	protected DataJSO() {}
	
	public final native int getHeight() /*-{ return this.alt; }-*/;
	public final native String getData() /*-{ return this.dat; }-*/;
	public final native int getDirection() /*-{ return this.dir; }-*/; 
	public final native int getSatelite() /*-{ return this.sat; }-*/;
	
	//{\"alt\": 259, \"dat\": \"1=0,179=0,180=0\", \"dir\": 48, \"sat\": 19, \"evID\": 1}
		
}
