package org.geowe.client.shared.rest.sgf.model.pageable;

public interface PageableResponse<T> {

	PageContent<T> getContent();

	PageInfo getPageInfo();

}
