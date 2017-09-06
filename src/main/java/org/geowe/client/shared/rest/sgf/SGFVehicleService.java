package org.geowe.client.shared.rest.sgf;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.geowe.client.shared.rest.sgf.model.pageable.vehicle.VehicleResponse;
import org.geowe.client.shared.rest.sgf.model.vehicle.Vehicle;

/**
 * Servicio que expone la API de Vehículos.
 * 
 * @author rltorres
 *
 */
@Path("/")
public interface SGFVehicleService {
	
	/**
	 * Obtiene todos los vehículos de todas las empresas.
	 * Solo disponible para ROLE_PLATFORM_ADMIN.
	 * @param authorization
	 * @param size: nº de elementos devueltos en cada página
	 * @param sort: atributo por el que se ordenan los elementos
	 * @return
	 */
	@GET	
	@Consumes("application/json")
	@Path("vehicles")
	VehicleResponse get(@HeaderParam("Authorization") String authorization,
			@QueryParam("size")int size,
			@QueryParam("sort")String sort);

	/**
	 * Obtiene todos los vehículos de la empresa indicada
	 * @param authorization
	 * @param companyId: identificado de la empresa
	 * @param size: nº de elementos devueltos en cada página
	 * @param sort: atributo por el que se ordenan los elementos
	 * @return cadena em formato JSON de los vehículos de la compañia companyId
	 */
	@GET	
	@Consumes("application/json")
	@Produces("application/json")
	@Path("companies/{companyId}/vehicles")
	String get(@HeaderParam("Authorization") String authorization,
			@PathParam("companyId") int companyId,
			@QueryParam("size")int size,
			@QueryParam("sort")String sort);
	
	/**
	 * Obtiene información de un vehículo
	 * @param authorization
	 * @param id: identificador del vehículo
	 * @return
	 */
	@GET
	@Consumes("application/json")
	@Path("vehicles/{id}")
	Vehicle get(@HeaderParam("Authorization") String authorization,
			@PathParam("id") int id);

}
