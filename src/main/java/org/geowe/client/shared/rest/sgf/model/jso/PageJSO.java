package org.geowe.client.shared.rest.sgf.model.jso;

import com.google.gwt.core.client.JavaScriptObject;

public class PageJSO extends JavaScriptObject {
	
	protected PageJSO() {}
	
	public final native int getSize() /*-{ return this.size; }-*/;
	public final native int getTotalElements() /*-{ return this.totalElements; }-*/;
	public final native int getTotalPages() /*-{ return this.totalPages; }-*/; 
	public final native int getNumber() /*-{ return this.number; }-*/;
		
}
