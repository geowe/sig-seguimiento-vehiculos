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
import org.geowe.client.local.ui.KeyShortcutHandler;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.slf4j.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
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
import com.sencha.gxt.widget.core.client.button.TextButton;
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
	@Inject
	private MessageDialogBuilder messageDialogBuilder;

	private Dialog welcomeDialog;

	private TextField userNameField;
	private PasswordField passwordField;
	private Image progressImage;

	public interface WelcomeTemplate extends XTemplates {
		@XTemplate(source = "welcomeTemplate.html")
		public SafeHtml renderTemplate(UIWelcomeMessages uimessages);
	}

	public void showDialog() {
		if (welcomeDialog == null) {
			welcomeDialog = new Dialog();
			welcomeDialog.setHeaderVisible(false);
			welcomeDialog.setSize("530px", "330px");
			welcomeDialog.setModal(true);
			welcomeDialog.setClosable(false);
			welcomeDialog.setResizable(false);
			welcomeDialog.setHideOnButtonClick(false);
			welcomeDialog.setPredefinedButtons(PredefinedButton.OK);
			welcomeDialog.setBodyStyleName("pad-text");
			welcomeDialog.getBody().addClassName("pad-text");
			welcomeDialog.add(getPanel(getHtml()));
			
			TextButton okButton = welcomeDialog.getButton(PredefinedButton.OK);
			KeyShortcutHandler keyShortcut = new KeyShortcutHandler(okButton,
					KeyCodes.KEY_ENTER);
			
			passwordField.addKeyDownHandler(keyShortcut);
			
			okButton.addSelectHandler(
					new SelectHandler() {
						@Override
						public void onSelect(final SelectEvent event) {
							if(isEmptyCredential()) {
								messageDialogBuilder.createInfo(UIMessages.INSTANCE.warning(),
										"Debe rellenar las credenciales correctamente").show();
								return;
							}
							
							uRLVectorLayerInitializer.createLayerFromURL();
							progressImage.setVisible(true);

							String userName = userNameField.getText();
							String passwd = passwordField.getText();

							sgfLoginServiceProxy.login(userName, passwd);
						}
					});
		}
		
		
		
		welcomeDialog.show();
	}
	
	
	private boolean isEmptyCredential() {
		boolean isEmpty = false;
		if(userNameField.getText().trim().isEmpty() || passwordField.getText().trim().isEmpty()) {
			isEmpty = true;
		}
		
		return isEmpty;
	}

	public void hideDialog() {
		progressImage.setVisible(false);
		welcomeDialog.hide();
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

	private VerticalPanel getAuthenticationPanel() {
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
		userNameField.focus();
		panel.add(userNameField);

		passwordField = new PasswordField();
		passwordField.setTitle(UIMessages.INSTANCE.gitHubPasswordField());
		passwordField.setEmptyText(UIMessages.INSTANCE.gitHubPasswordField());
		passwordField.setWidth(120);
		passwordField.setAllowBlank(false);
		panel.add(passwordField);

		progressImage = new Image(ImageProvider.INSTANCE.circleProgress());
		progressImage.setVisible(false);
		panel.add(progressImage);
		return panel;
	}

	public HTML getHtml() {
		WelcomeTemplate template = GWT.create(WelcomeTemplate.class);

		return new HTML(template.renderTemplate(UIWelcomeMessages.INSTANCE));
	}

	public void hideProgressImage() {
		this.progressImage.setVisible(false);
	}
}
