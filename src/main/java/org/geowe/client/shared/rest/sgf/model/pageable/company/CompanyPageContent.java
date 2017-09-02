package org.geowe.client.shared.rest.sgf.model.pageable.company;

import java.util.Set;

import org.geowe.client.shared.rest.sgf.model.Company;
import org.geowe.client.shared.rest.sgf.model.pageable.PageContent;
import org.jboss.errai.common.client.api.annotations.MapsTo;
import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class CompanyPageContent extends PageContent<Company> {

	public CompanyPageContent(@MapsTo("companyResourceList") Set<Company> resourceList) {
		super(resourceList);
	}

}
