package org.geowe.client.shared.rest.sgf.model.pageable;

import java.util.Set;


public class PageContent<T> {

	private Set<T> resourceList;

	
	public PageContent(Set<T> resourceList) {
		super();
		this.resourceList = resourceList;
	}

	public Set<T> getResourceList() {
		return resourceList;
	}

	public void setResourceList(Set<T> resourceList) {
		this.resourceList = resourceList;
	}

	@Override
	public String toString() {
		return "PageContent [resourceList=" + resourceList + "]";
	}

	
	
}
