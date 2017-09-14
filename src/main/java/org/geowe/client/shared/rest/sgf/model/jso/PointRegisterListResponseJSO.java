package org.geowe.client.shared.rest.sgf.model.jso;

import com.google.gwt.core.client.JavaScriptObject;


public class PointRegisterListResponseJSO extends JavaScriptObject {
	
	protected PointRegisterListResponseJSO() {}
	
	public final native PointRegisterListEmbededJSO getPointRegisterListEmbededJSO() /*-{ return this._embedded; }-*/;
	public final native PageJSO gePageJSO() /*-{ return this.page; }-*/;	
	
}
