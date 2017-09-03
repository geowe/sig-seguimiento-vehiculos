package org.geowe.client.shared.rest.sgf.model.pageable.company;

import org.geowe.client.shared.rest.sgf.model.Company;
import org.geowe.client.shared.rest.sgf.model.pageable.PageContent;
import org.geowe.client.shared.rest.sgf.model.pageable.PageInfo;
import org.geowe.client.shared.rest.sgf.model.pageable.PageableResponse;
import org.geowe.client.shared.rest.sgf.model.pageable.PagedResponse;
import org.jboss.errai.common.client.api.annotations.MapsTo;
import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class CompanyResponse extends PagedResponse implements PageableResponse<Company> {

	private CompanyPageContent content;
	
	public CompanyResponse(@MapsTo("_embedded") CompanyPageContent content,
			@MapsTo("page") PageInfo pageInfo) {
		super(pageInfo);
		this.content= content;
	}

	@Override
	public PageContent<Company> getContent() {
		return this.content;
	}

}
