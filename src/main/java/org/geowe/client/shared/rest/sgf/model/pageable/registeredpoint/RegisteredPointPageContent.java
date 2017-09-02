package org.geowe.client.shared.rest.sgf.model.pageable.registeredpoint;

import java.util.Set;

import org.geowe.client.shared.rest.sgf.model.RegisteredPoint;
import org.geowe.client.shared.rest.sgf.model.pageable.PageContent;
import org.jboss.errai.common.client.api.annotations.MapsTo;
import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class RegisteredPointPageContent extends PageContent<RegisteredPoint> {

	public RegisteredPointPageContent(@MapsTo("registeredPointResourceList") Set<RegisteredPoint> resourceList) {
		super(resourceList);

	}

}
