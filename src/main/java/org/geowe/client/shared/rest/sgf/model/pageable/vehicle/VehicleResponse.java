package org.geowe.client.shared.rest.sgf.model.pageable.vehicle;

import org.geowe.client.shared.rest.sgf.model.pageable.PageContent;
import org.geowe.client.shared.rest.sgf.model.pageable.PageInfo;
import org.geowe.client.shared.rest.sgf.model.pageable.PageableResponse;
import org.geowe.client.shared.rest.sgf.model.pageable.PagedResponse;
import org.geowe.client.shared.rest.sgf.model.vehicle.Vehicle;
import org.jboss.errai.common.client.api.annotations.MapsTo;

public class VehicleResponse extends PagedResponse implements PageableResponse<Vehicle>{

	private VehiclePageContent content;
	
	public VehicleResponse(@MapsTo("_embedded") VehiclePageContent content,
			@MapsTo("page") PageInfo pageInfo) {
		super(pageInfo);
	}

	@Override
	public PageContent<Vehicle> getContent() {
		return this.content;
	}

	public void setContent(VehiclePageContent content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "VehicleResponse [content=" + content + ", PageInfo=" + getPageInfo() + "]";
	}

	

}
