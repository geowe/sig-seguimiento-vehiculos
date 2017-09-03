package org.geowe.client.shared.rest.sgf.model.pageable.vehicle;

import java.util.Set;

import org.geowe.client.shared.rest.sgf.model.pageable.PageContent;
import org.geowe.client.shared.rest.sgf.model.vehicle.Vehicle;
import org.jboss.errai.common.client.api.annotations.MapsTo;
import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class VehiclePageContent extends PageContent<Vehicle>{

	public VehiclePageContent(@MapsTo("vehicleResourceList")Set<Vehicle> resourceList) {
		super(resourceList);
	}

}
