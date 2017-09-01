package org.geowe.client.shared.rest.sgf.model.pageable;

import org.geowe.client.shared.rest.sgf.model.RegisteredPoint;
import org.jboss.errai.common.client.api.annotations.MapsTo;
import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class RegisteredPointResponse implements PageableResponse<RegisteredPoint>{

	private RegisteredPointPageContent content;

	private PageInfo pageInfo;

	public RegisteredPointResponse(@MapsTo("_embedded") RegisteredPointPageContent content,
			@MapsTo("page") PageInfo pageInfo) {
		super();
		this.content = content;
		this.pageInfo = pageInfo;
	}

	public PageContent<RegisteredPoint> getContent() {
		return content;
	}

	public void setContent(RegisteredPointPageContent content) {
		this.content = content;
	}

	public PageInfo getPageInfo() {
		return pageInfo;
	}

	public void setPageInfo(PageInfo page) {
		this.pageInfo = page;
	}

	@Override
	public String toString() {
		return "RegisteredPointPage [content=" + content + ", page=" + pageInfo + "]";
	}

}
