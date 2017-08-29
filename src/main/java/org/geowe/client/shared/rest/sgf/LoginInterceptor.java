package org.geowe.client.shared.rest.sgf;

import org.geowe.client.shared.rest.sgf.model.SgfUser;
import org.jboss.errai.enterprise.client.jaxrs.api.ResponseCallback;
import org.jboss.errai.enterprise.client.jaxrs.api.interceptor.RestCallContext;
import org.jboss.errai.enterprise.client.jaxrs.api.interceptor.RestClientInterceptor;

import com.google.gwt.http.client.Response;

public class LoginInterceptor implements RestClientInterceptor {

	
	@Override
	public void aroundInvoke(final RestCallContext context) {
		context.proceed(new ResponseCallback() {
			
			@Override
			public void callback(Response response) {
				
				SgfUser user = (SgfUser) context.getResult();
				
				user.setPassword(response.getHeadersAsString());
				context.setResult(user);
				
			}
		});

	}

}
