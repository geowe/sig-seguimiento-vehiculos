package org.geowe.client.local.sgf;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.AppClientProperties;

@ApplicationScoped
public class SGFServiceInfo {
	private static final String URL_PROPERTY_NAME = "urlSGFAPI";
	
	@Inject
	private AppClientProperties appClientProperties;
		
	public String getURL() {
		return appClientProperties.getStringValue(URL_PROPERTY_NAME);
	}
}
