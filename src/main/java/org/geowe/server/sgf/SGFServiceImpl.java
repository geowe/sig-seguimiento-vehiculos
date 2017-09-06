package org.geowe.server.sgf;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.geowe.client.shared.rest.sgf.SGFService;
import org.jboss.errai.security.server.properties.ErraiAppPropertiesProducer;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * 
 * @author lotor
 *
 */
public class SGFServiceImpl extends RemoteServiceServlet implements SGFService {

	private static final long serialVersionUID = -6250042509770729218L;

	private static final Logger LOG = LoggerFactory.getLogger(SGFServiceImpl.class.getName());

	private String loginUrl;

	public SGFServiceImpl() {
		super();
		initProperties();
	}

	private void initProperties() {
		final ErraiAppPropertiesProducer producer = new ErraiAppPropertiesProducer();
		final Properties prop = producer.getErraiAppProperties();
		this.loginUrl = prop.getProperty("sgf.url") + "/login";
	}

	// TODO: controlar los errores mas finamente.
	@Override
	public String login(String user, String password) throws IllegalArgumentException {
		String response = "";
		try {
			response = doHttpPost(user, password);
		} catch (RuntimeException e) {
			LOG.error("SGF ERROR: " + e.getMessage());
			throw new RuntimeException("Error de autenticacion");
		} catch (Exception e) {
			LOG.error("SGF ERROR inesperado: " + e.getMessage());
			throw new RuntimeException("Error inesperado");

		}
		return response;
	}

	private String doHttpPost(final String userName, final String password) throws Exception {

		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(loginUrl);

		// add header
		post.addHeader("Content-Type", "application/json; charset=utf-8");

		final String PAYLOAD = "{\"username\":\"" + userName + "\",\"password\": \"" + password + "\"}";

		StringEntity body = new StringEntity(PAYLOAD);
		body.setContentType("application/json");
		post.setEntity(body);

		HttpResponse response = client.execute(post);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Error de autenticacion");
		}

		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}

		JSONObject jsonObject = new JSONObject(result.toString());

		jsonObject.accumulate("token",
				response.getFirstHeader("Authorization").getValue());

		client.getConnectionManager().shutdown();

		return jsonObject.toString();
	}

}
