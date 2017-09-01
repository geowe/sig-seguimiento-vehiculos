package org.geowe.client.shared.rest.sgf.model.pageable;

import org.geowe.client.shared.rest.sgf.model.RegisteredPoint;
import org.jboss.errai.common.client.api.annotations.MapsTo;
import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class RegisteredPointPage {

	private RegisteredPointPageContent content;

	private Page page;

	public RegisteredPointPage(@MapsTo("_embedded") RegisteredPointPageContent content,
			@MapsTo("page") Page page) {
		super();
		this.content = content;
		this.page = page;
	}

	public PageContent<RegisteredPoint> getContent() {
		return content;
	}

	public void setContent(RegisteredPointPageContent content) {
		this.content = content;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	@Override
	public String toString() {
		return "RegisteredPointPage [content=" + content + ", page=" + page + "]";
	}

}
