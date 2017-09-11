package org.geowe.client.shared.rest.sgf.model.jso;

import com.google.gwt.core.client.JavaScriptObject;

public class PointRegisterListEmbededJSO extends JavaScriptObject {
	
	protected PointRegisterListEmbededJSO() {}
	
	public final native PointRegisterJSO[] getPointRegister() /*-{ return this.registeredPointResourceList; }-*/;
	
}
