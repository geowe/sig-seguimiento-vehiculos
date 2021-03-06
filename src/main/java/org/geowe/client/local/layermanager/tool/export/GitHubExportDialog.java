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
package org.geowe.client.local.layermanager.tool.export;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.tool.create.GitHubPathListDialog;
import org.geowe.client.local.messages.UIMessages;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.PasswordField;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * Representa el diálogo para realizar la exportación de los datos en GitHub
 * 
 * @author jose@geowe.org
 * @since 16/09/2016
 */
@ApplicationScoped
public class GitHubExportDialog extends Dialog {
	private static final String FIELD_WIDTH = "300px";
	
	private TextField userNameField;
	private PasswordField passwordField;
	private TextField repositoryField;
	private TextField pathField;
	private TextField fileNameField;
	private TextField messageField;
	private final TextButton createButton;
	private final TextButton updateButton;
	private TextButton repositoriesButton;
	
	@Inject
	private GitHubPathListDialog pathListDialog;

	
	public String getUserName() {
		return userNameField.getText();
	}

	public void setUserName(String userName) {
		this.userNameField.setText(userName);
	}

	public String getPassword() {
		return passwordField.getText();
	}

	public void setPassword(String password) {
		this.passwordField.setText(password);
	}
	
	public String getRepository() {
		return repositoryField.getText();
	}
	
	public TextField getRepositoryTextField() {
		return repositoryField;
	}

	public void setRepository(String repository) {
		this.repositoryField.setText(repository);
	}
	
	public String getPath() {
		return pathField.getText();
	}

	public void setPath(String path) {
		this.pathField.setText(path);
	}
	
	public String getMessage() {
		return messageField.getText();
	}

	public void setMessage(String message) {
		this.messageField.setText(message);
	}
	
	public String getFileName() {
		return fileNameField.getText();
	}

	public void setFileName(String fileName) {
		this.fileNameField.setText(fileName);
	}
			
	public GitHubExportDialog() {
		super();
		this.setHeadingText(UIMessages.INSTANCE.gitHubExportDialogTitle());
		this.getHeader().setIcon(ImageProvider.INSTANCE.github24());
		this.setPredefinedButtons(PredefinedButton.CANCEL);
		this.setPixelSize(350, 350);		
		this.setModal(true);
		this.setHideOnButtonClick(true);
		final VerticalPanel panel = new VerticalPanel();
		panel.add(createAuthenticationPanel());
		panel.add(createRepositoryPanel());
		panel.add(createCommitPanel());	
		add(panel);
		createButton = new TextButton(UIMessages.INSTANCE.gitHubCreateButton());
		updateButton = new TextButton(UIMessages.INSTANCE.gitHubUpdateButton());
				
		getButtonBar().add(createButton);
		getButtonBar().add(updateButton);		
	}
	
	public TextButton getCreateButton() {
		return createButton;
	}
	
	public TextButton getUpdateButton() {
		return updateButton;
	}
	
	public TextButton getRepositoriesButton() {
		return repositoriesButton;
	}
			
	private Widget createAuthenticationPanel() {
		final VerticalPanel panel = new VerticalPanel();
		panel.getElement().getStyle().setBackgroundColor("#E0ECF8");
		panel.setWidth("350px");		
		panel.setSpacing(10);

		userNameField = new TextField();
		userNameField.setTitle(UIMessages.INSTANCE.gitHubUserNameField());
		userNameField.setEmptyText(UIMessages.INSTANCE.gitHubUserNameField());
		userNameField.setWidth(FIELD_WIDTH);
		panel.add(userNameField);

		passwordField = new PasswordField();		
		passwordField.setTitle(UIMessages.INSTANCE.gitHubPasswordField());
		passwordField.setEmptyText(UIMessages.INSTANCE.gitHubPasswordField());
		passwordField.setWidth(FIELD_WIDTH);
		panel.add(passwordField);
		
		return panel;
	}
	
	private Widget createRepositoryPanel() {
		final VerticalPanel panel = new VerticalPanel();			
		panel.setWidth("350px");		
		panel.setSpacing(10);		
		
		repositoriesButton = new TextButton("...");
		repositoriesButton.setTitle(UIMessages.INSTANCE.gitHubTitleListRepo());
		
		
		final HorizontalPanel repositoryPanel = new HorizontalPanel();
		
		repositoryField = new TextField();
		repositoryField.setTitle(UIMessages.INSTANCE.gitHubRepositoryNameField());		
		repositoryField.setEmptyText(UIMessages.INSTANCE.gitHubRepositoryNameField());
		repositoryField.setWidth(FIELD_WIDTH);
		
		repositoryPanel.add(repositoryField);		
		repositoryPanel.add(repositoriesButton);
		
		panel.add(repositoryPanel);		
		
		pathField = new TextField();
		pathField.setTitle(UIMessages.INSTANCE.gitHubPathNameField());		
		pathField.setEmptyText(UIMessages.INSTANCE.gitHubPathNameField());
		pathField.setWidth(FIELD_WIDTH);
		
		TextButton pathButton = new TextButton("...");
		pathButton.addSelectHandler(getPathEvent());
		
		HorizontalPanel pathPanel = new HorizontalPanel();
		pathPanel.add(pathField);
		pathPanel.add(pathButton);
		
		panel.add(pathPanel);				

		return panel;
	}
	
	private Widget createCommitPanel() {
		final VerticalPanel panel = new VerticalPanel();
		panel.setWidth("350px");		
		panel.setSpacing(10);				
		
		fileNameField = new TextField();
		fileNameField.setTitle(UIMessages.INSTANCE.gitHubFileNameField());
		fileNameField.setEmptyText(UIMessages.INSTANCE.gitHubFileNameField());
		fileNameField.setWidth(FIELD_WIDTH);
		panel.add(fileNameField);
				
		messageField = new TextField();
		messageField.setTitle(UIMessages.INSTANCE.gitHubMessageCommitField());
		messageField.setEmptyText(UIMessages.INSTANCE.gitHubMessageCommitField());
		messageField.setWidth(FIELD_WIDTH);
		panel.add(messageField);

		return panel;
	}	
	
	private SelectHandler getPathEvent() {
		return new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {				
				
				pathListDialog.setTargetTextField(pathField);
				pathListDialog.load(userNameField.getText(), repositoryField.getText(), pathField.getText());
			}

		};
	}
}
