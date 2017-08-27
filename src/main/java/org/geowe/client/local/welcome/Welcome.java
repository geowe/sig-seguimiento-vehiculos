/*
 * #%L
 * GeoWE Project
 * %%
 * Copyright (C) 2015 - 2016 GeoWE.org
 * %%
 * This file is part of GeoWE.org.
 * 
 * GeoWE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * GeoWE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with GeoWE.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */
package org.geowe.client.local.welcome;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.initializer.URLVectorLayerInitializer;
import org.geowe.client.local.main.AnchorBuilder;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.sgf.SGFLoginServiceProxy;
import org.slf4j.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.PasswordField;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * 
 * @author rafa@geowe.org
 *
 */
@ApplicationScoped
public class Welcome {

	@Inject
	private Logger logger;
	@Inject
	private URLVectorLayerInitializer uRLVectorLayerInitializer;
	@Inject
	private SGFLoginServiceProxy sgfLoginServiceProxy;
	
	private TextField userNameField;
	private PasswordField passwordField;

	public interface WelcomeTemplate extends XTemplates {
		@XTemplate(source = "welcomeTemplate.html")
		public SafeHtml renderTemplate(UIWelcomeMessages uimessages);
	}

	public void showDialog() {
		final Dialog simple = new Dialog();
		simple.setHeaderVisible(false);
		simple.setSize("530px", "330px");
		simple.setModal(true);
		simple.setClosable(false);
		simple.setResizable(false);
		simple.setHideOnButtonClick(true);
		simple.setPredefinedButtons(PredefinedButton.OK);
		simple.setBodyStyleName("pad-text");
		simple.getBody().addClassName("pad-text");
		simple.add(getPanel(getHtml()));
		simple.getButton(PredefinedButton.OK).addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(final SelectEvent event) {
				uRLVectorLayerInitializer.createLayerFromURL();
				logger.info("ok pressed...");
				// TODO: comprobar campos (que no sean vacios)
				String userName = userNameField.getText();
				String passwd = passwordField.getText();
				// TODO: montar el payload de una menera menos cutre
				String payload = "{\"username\":\"" + userName + "\",\"password\": \"" + passwd + "\"}";

				sgfLoginServiceProxy.login(payload);
			}
		});
		simple.show();
	}
	

	private HorizontalPanel getPanel(final HTML data) {
		HorizontalPanel panel = new HorizontalPanel();
		panel.setSize("520px", "310px");
		panel.setSpacing(5);
		panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		

		panel.add(getAuthenticationPanel());
		panel.add(data);
		return panel;
	}
	
	private VerticalPanel getAuthenticationPanel(){
		final VerticalPanel panel = new VerticalPanel();
		
		panel.setWidth("150px");		
		panel.setSpacing(10);
		Anchor anchor = new AnchorBuilder().setHref("http://www.geowe.org")
				.setImage(new Image(ImageProvider.INSTANCE.geoweSquareLogo()))
				.build();
		panel.add(anchor);
		
		userNameField = new TextField();
		userNameField.setTitle(UIMessages.INSTANCE.gitHubUserNameField());
		userNameField.setEmptyText(UIMessages.INSTANCE.gitHubUserNameField());
		userNameField.setWidth(120);
		userNameField.setAllowBlank(false);
		panel.add(userNameField);

		passwordField = new PasswordField();		
		passwordField.setTitle(UIMessages.INSTANCE.gitHubPasswordField());
		passwordField.setEmptyText(UIMessages.INSTANCE.gitHubPasswordField());
		passwordField.setWidth(120);
		passwordField.setAllowBlank(false);
		panel.add(passwordField);
		
		return panel;
	}

	public HTML getHtml() {
		WelcomeTemplate template = GWT.create(WelcomeTemplate.class);

		return new HTML(template.renderTemplate(UIWelcomeMessages.INSTANCE));
	}
}
