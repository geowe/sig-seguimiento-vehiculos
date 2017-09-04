package org.geowe.client.shared.rest.sgf.model.pageable;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class PageInfo {
	
	private int size;
	
	private int totalElements;
	
	private int totalPages;
	

	private int number;

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(int totalElements) {
		this.totalElements = totalElements;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
	
	@Override
	public String toString() {
		return "Page [size=" + size + ", totalElements=" + totalElements + ", totalPages=" + totalPages + ", number="
				+ number + "]";
	}

}
