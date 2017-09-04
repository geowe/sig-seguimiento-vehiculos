package org.geowe.client.shared.rest.sgf.model.pageable;

public abstract class PagedResponse {


	private PageInfo pageInfo;
	
	public PagedResponse(PageInfo pageInfo) {
		this.pageInfo = pageInfo;
	}
	
	public PageInfo getPageInfo() {
		return pageInfo;
	}

	public void setPageInfo(PageInfo page) {
		this.pageInfo = page;
	}
}
