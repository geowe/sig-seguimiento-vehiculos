package org.geowe.client.local.sgf;

import org.jboss.errai.enterprise.client.jaxrs.api.ResponseCallback;
import org.jboss.errai.enterprise.client.jaxrs.api.interceptor.RestCallContext;
import org.jboss.errai.enterprise.client.jaxrs.api.interceptor.RestClientInterceptor;

import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;

public class LoginInterceptor implements RestClientInterceptor {

	
	@Override
	public void aroundInvoke(final RestCallContext context) {
		//TODO: GET Authorizarion token
		context.proceed(new ResponseCallback() {
			
			@Override
			public void callback(Response response) {
				
				
				Window.alert(response.getHeader("sgf-auth"));
				Window.alert(response.getHeadersAsString());
//				SgfUser user = (SgfUser) context.getResult();
//				
//				user.setPassword(response.getHeadersAsString());
//				context.setResult(user);
				context.setResult(context.getResult());
				
			}
		});

	}

}
