package org.geowe.client.shared.rest.sgf;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.geowe.client.shared.rest.sgf.model.pageable.RegisteredPointPage;

/**
 * Servicio que expone la API de Puntos Registrados por los GPS
 * 
 * @author lotor
 *
 */
@Path("/registeredpoints")
public interface SGFRegisteredPointService {

	@GET	
	@Consumes("application/json")
	RegisteredPointPage getAll(@HeaderParam("Authorization") String authorization,
			@QueryParam("size")int size,
			@QueryParam("sort")String sort);
}
