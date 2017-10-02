package org.geowe.client.shared.rest.sgf;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.geowe.client.shared.rest.sgf.model.pageable.registeredpoint.RegisteredPointResponse;

/**
 * Servicio que expone la API de Puntos Registrados por los GPS
 * TODO: pediente de que estén disponibles todos los recursos
 * @author lotor 
 */
@Path("/")
public interface SGFRegisteredPointService {

	
	@GET	
	@Path("registeredpoints")
	@Consumes("application/json")
	@Produces("application/json")
	String getAll(@HeaderParam("Authorization") String authorization, @QueryParam("size") int size,
			@QueryParam("sort") String sort);
	
	@GET	
	@Consumes("application/json")
	@Produces("application/json")
	@Path("vehicles/{vehicleId}/registered-points")
	String getLastRegisteredPoints(@HeaderParam("Authorization") String authorization,
			@PathParam("vehicleId") int id,
			@QueryParam("size")int size,
			@QueryParam("sort")String sort);
	
	@GET	
	@Consumes("application/json")
	@Produces("application/json")
	@Path("vehicles/{vehicleId}/registered-points")
	String getRegisteredPoints(@HeaderParam("Authorization") String authorization,
			@PathParam("vehicleId") int id,
			@QueryParam("startdate") String startDate,
			@QueryParam("enddate") String endDate,
			@QueryParam("size") int size,
			@QueryParam("sort") String sort);
}
