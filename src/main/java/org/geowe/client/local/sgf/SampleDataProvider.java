package org.geowe.client.local.sgf;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface SampleDataProvider extends ClientBundle {
	public static final SampleDataProvider INSTANCE = GWT.create(SampleDataProvider.class);

	
	@Source("sampledata/session.json")
	TextResource session();
		
	@Source("sampledata/listVehicle.json")
	TextResource listVehicle();
}