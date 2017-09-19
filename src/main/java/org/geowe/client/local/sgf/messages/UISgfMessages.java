package org.geowe.client.local.sgf.messages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

public interface UISgfMessages extends Messages {

	UISgfMessages INSTANCE = GWT.create(UISgfMessages.class);
	
	String authError();
	
	String getIMEI();
	
	String notGPSFound();
	
	String lastRegisterPoint();
	
	String getGPSData();
	
	String gpsDataNotFound();
	
	String errorDetected();
	
	String vehicleLayerName();
	
	String imeiColumn();
	
	String dateColumn();
	
	String timeColumn();
	
	String speedColumn();
	
	String dataColumn();
	
	String positionColumn();
	
	String distanceColumn();
	
	String streetColumn();
	
	String numberColumn();
	
	String localityColumn();
	
	String provinceColumn();
	
	String postalCodeColumn();
	
	String countryColumn();
	
	String plateColumn();
	
	String kmForReviewColumn();
	
	String lastReviewColumn();
	
	String commentColumn();
	
	String statusColumn();
	
	String acummulatedDistanceColumn();
	
	String drawRoute();
	
	String selectDate();
	
	String companyLabel();
	
	String cifLabel();
	
	String noCommentLabel();
	
	String vehicleList();
	
	String companyVehicleList();
	
	String exit();
}
