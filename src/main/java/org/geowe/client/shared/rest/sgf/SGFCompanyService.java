package org.geowe.client.shared.rest.sgf;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.geowe.client.shared.rest.sgf.model.pageable.company.CompanyResponse;
import org.geowe.client.shared.rest.sgf.model.vehicle.Vehicle;

/**
 * Servicio que expone la API de Empresas
 * @author lotor
 *
 */
@Path("/companies")
public interface SGFCompanyService {
	
	/**
	 * Obtiene todas las empresas de la plataforma
	 * solo disponible para ROLE_PLATFORM_ADMIN
	 * @param authorization
	 * @param size: nº de elementos devueltos en cada página
	 * @param sort: atributo por el que se ordenan los elementos
	 * @return
	 */
	@GET	
	@Consumes("application/json")
	CompanyResponse get(@HeaderParam("Authorization") String authorization,
			@QueryParam("size")int size,
			@QueryParam("sort")String sort);
	
	/**
	 * Obtiene información de una empresa
	 * @param authorization
	 * @param id: identificador de la empresa
	 * @return
	 */
	@GET
	@Consumes("application/json")
	@Path("/{id}")
	Vehicle get(@HeaderParam("Authorization") String authorization,
			@PathParam("id") int id);
	
	
	@GET	
	@Consumes("application/json")
	@Path("/{id}/registered-points")
	String getRegisteredPoints(@HeaderParam("Authorization") String authorization,
			@PathParam("id") int id,
			@QueryParam("imei")String imei,
			@QueryParam("startdate")String startDate,
			@QueryParam("enddate")String endDate,
			@QueryParam("size")int size,
			@QueryParam("sort")String sort);
	
	@GET	
	@Consumes("application/json")
	@Path("/{id}/registered-points")
	String getLastRegisteredPoints(@HeaderParam("Authorization") String authorization,
			@PathParam("id") int id,
			@QueryParam("imei")String imei,			
			@QueryParam("size")int size,
			@QueryParam("sort")String sort);

}
