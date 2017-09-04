package org.geowe.client.shared.rest.sgf.model.vehicle;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class VehicleType {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "VehicleType [name=" + name + "]";
	}
	
}
