package org.geowe.client.shared.rest.sgf.model;

import java.util.Arrays;

import org.jboss.errai.common.client.api.annotations.Portable;

/**
 * Representa a un punto registrado por el servicio de captura de datos
 * 
 * Ejemplo de json devuelto (puede qeu cambie): { "_embedded": {
 * "registeredPointResourceList": [ { "imei": "test imei", "date": [ 2017, 9, 1,
 * 0, 0 ], "speed": "0", "datos": "AAAAAQ==", "position": "POINT (0 0)",
 * "_links": { "self": { "href": "http://127.0.0.1:8081/registeredpoints/1" } }
 * } ] }, "_links": { "self": { "href":
 * "http://127.0.0.1:8081/registeredpoints?page=0&size=20" } }, "page": {
 * "size": 20, "totalElements": 1, "totalPages": 1, "number": 0 } }
 * 
 * @author rltorres
 *
 */
@Portable
public class RegisteredPoint {

	
	private String imei;

	/*
	 * TODO: java 8 lo envía como array de enteros ver como mapearlo [year,
	 * month, day, hour, min, sec]
	 */
	private Integer[] date;

	/*
	 * Velocidad en km/h
	 */
	private String speed;

	/*
	 * TODO: almacenado como jsonb por mapear
	 */
	private String datos;
	/*
	 * Punto en WKT
	 */
	private String position;
	
	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public Integer[] getDate() {
		return date;
	}

	public void setDate(Integer[] date) {
		this.date = date;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public String getDatos() {
		return datos;
	}

	public void setDatos(String datos) {
		this.datos = datos;
	}

	public String getPositionWkt() {
		return position;
	}

	public void setPositionWkt(String positionWkt) {
		this.position = positionWkt;
	}

	public String getDateAsString() {
		String dateAsString = "";
		if (this.date != null && this.date.length >= 3) {
			dateAsString = this.date[0] + "-" + this.date[1] + "-" + this.date[2];
		}
		return dateAsString;
	}

	public String getTimeAsString() {
		StringBuilder time = new StringBuilder("");
		if (this.date != null && this.date.length > 3) {
			for (int i = 3; i < date.length; i++) {
				time.append(date[i] + ":");
			}
		}
		return time.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(date);
		result = prime * result + ((imei == null) ? 0 : imei.hashCode());
		result = prime * result + ((position == null) ? 0 : position.hashCode());
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
		RegisteredPoint other = (RegisteredPoint) obj;
		if (!Arrays.equals(date, other.date))
			return false;
		if (imei == null) {
			if (other.imei != null)
				return false;
		} else if (!imei.equals(other.imei))
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RegisteredPoint [imei=" + imei + ", date=" + Arrays.toString(date) + ", speed=" + speed + ", datos="
				+ datos + ", positionWkt=" + position + "]";
	}

}
