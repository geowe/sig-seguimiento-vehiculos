package org.geowe.client.shared.rest.sgf.model.pageable.registeredpoint;

import org.geowe.client.shared.rest.sgf.model.RegisteredPoint;
import org.geowe.client.shared.rest.sgf.model.pageable.PageContent;
import org.geowe.client.shared.rest.sgf.model.pageable.PageInfo;
import org.geowe.client.shared.rest.sgf.model.pageable.PageableResponse;
import org.geowe.client.shared.rest.sgf.model.pageable.PagedResponse;
import org.jboss.errai.common.client.api.annotations.MapsTo;
import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class RegisteredPointResponse extends PagedResponse implements PageableResponse<RegisteredPoint>{

	private RegisteredPointPageContent content;


	public RegisteredPointResponse(@MapsTo("_embedded") RegisteredPointPageContent content,
			@MapsTo("page") PageInfo pageInfo) {
		super(pageInfo);
		this.content = content;
	}

	public PageContent<RegisteredPoint> getContent() {
		return content;
	}

	public void setContent(RegisteredPointPageContent content) {
		this.content = content;
	}
	

	@Override
	public String toString() {
		return "RegisteredPointPage [content=" + content + ", page=" + getPageInfo() + "]";
	}

}
