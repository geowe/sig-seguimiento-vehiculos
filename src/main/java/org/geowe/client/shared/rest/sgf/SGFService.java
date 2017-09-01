package org.geowe.client.shared.rest.sgf;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * 
 * @author lotor
 *
 */
@RemoteServiceRelativePath("sgfService")
public interface SGFService extends RemoteService {

	String login(String user, String password) throws IllegalArgumentException;;
}
