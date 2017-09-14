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
	
	@Source("sampledata/listRegisterPoint.json")
	TextResource listRegisterPoint();
	
	@Source("sampledata/20-puntos_registrados_example.json")
	TextResource list20RegisterPoint();
	
	@Source("sampledata/500-puntos_registrados_example.json")
	TextResource list500RegisterPoint();
	
	@Source("sampledata/lastPointRegister.json")
	TextResource lastPointRegister();
}
