package org.geowe.client.shared.rest.sgf.model.vehicle;

import org.jboss.errai.common.client.api.annotations.Portable;

/**
 * Representa un Vehículo del Sistema de Seguimiento de Flotas
 * 
 * @author rltorres
 *
 */
@Portable
public class Vehicle {

	private int id;

	private String plate;

	private String model;

	private String kind;

	private int kmsLeftForRevision;

	private String lastRevisionDate;

	private String comments;

	private String status;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public int getKmsLeftForRevision() {
		return kmsLeftForRevision;
	}

	public void setKmsLeftForRevision(int kmsLeftForRevision) {
		this.kmsLeftForRevision = kmsLeftForRevision;
	}

	public String getLastRevisionDate() {
		return lastRevisionDate;
	}

	public void setLastRevisionDate(String lastRevisionDate) {
		this.lastRevisionDate = lastRevisionDate;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((plate == null) ? 0 : plate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vehicle other = (Vehicle) obj;
		if (id != other.id)
			return false;
		if (plate == null) {
			if (other.plate != null)
				return false;
		} else if (!plate.equals(other.plate))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Vehicle [id=" + id + ", plate=" + plate + ", model=" + model
				+ ", kind=" + kind + ", kmsLeftForRevision="
				+ kmsLeftForRevision + ", lastRevisionDate=" + lastRevisionDate
				+ ", comments=" + comments + ", status=" + status + "]";
	}

}
