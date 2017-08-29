package org.geowe.client.shared.rest.sgf;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.geowe.client.shared.rest.sgf.model.SgfUser;
import org.jboss.errai.common.client.api.interceptor.InterceptedCall;

/**
 * 
 * @author lotor
 *
 */
@Path("/")
public interface SGFService {

	@POST
	@Path("login")
	@Produces("application/json; charset=utf-8")
	@Consumes("application/json; charset=utf-8")
	@InterceptedCall(LoginInterceptor.class)
	SgfUser login(String payload);
}
