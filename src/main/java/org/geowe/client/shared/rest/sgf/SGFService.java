package org.geowe.client.shared.rest.sgf;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * 
 * @author lotor
 *
 */

public interface SGFService {

	@POST
	@Path("/login")
	@Produces("application/json; charset=utf-8")
	@Consumes("application/json; charset=utf-8")
	String login(String payload);
}
