package org.geowe.client.shared.rest.sgf.model.pageable;

import java.util.Set;

import org.geowe.client.shared.rest.sgf.model.RegisteredPoint;
import org.jboss.errai.common.client.api.annotations.MapsTo;
import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class RegisteredPointPageContent extends PageContent<RegisteredPoint> {

	public RegisteredPointPageContent(@MapsTo("registeredPointResourceList") Set<RegisteredPoint> resourceList) {
		super(resourceList);

	}

}
