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
package org.geowe.client.local.sgf;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.main.tool.info.EditLayerDataTool;
import org.geowe.client.local.main.tool.layer.LayerManagerTool;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.sgf.messages.UISgfMessages;
import org.geowe.client.shared.rest.sgf.model.jso.CompanyJSO;
import org.geowe.client.shared.rest.sgf.model.jso.SessionJSO;
import org.geowe.client.shared.rest.sgf.model.jso.VehicleJSO;
import org.slf4j.Logger;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.core.client.resources.ThemeStyles;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.fx.client.FxElement;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.RowExpander;
import com.sencha.gxt.widget.core.client.grid.filters.GridFilters;
import com.sencha.gxt.widget.core.client.grid.filters.StringFilter;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

/**
 * Diálogo que muestra la lista de vehículos de una empresa
 * 
 * @author jose@geowe.org
 *
 */
@ApplicationScoped
public class VehicleDialog extends Dialog {

	private static final String FIELD_WIDTH = "225px";
	@Inject
	private Logger logger;
	@Inject
	private VehicleToolBar vehicleToolBar;	
	@Inject	
	private EditLayerDataTool editLayerDataTool;
	private TextField companyNameField;
	private TextField companyCifField;	
	private ListStore<VehicleJSO> vehicleStore;
	private static final VehicleJSOProperties vehicleProps = GWT.create(VehicleJSOProperties.class);
	private Grid<VehicleJSO> vehiculeGrid;
	@Inject
	private LayerManagerWidget layerMangerWidget;
	
	public VehicleDialog() {
		super();
		this.getHeader().setIcon(ImageProvider.INSTANCE.layer16());
		this.setHeadingText(UISgfMessages.INSTANCE.vehicleLayerName());
		this.setPredefinedButtons(PredefinedButton.CLOSE);
		this.setPixelSize(560, 520);
		this.setModal(false);
		this.setResizable(false);
		this.setHideOnButtonClick(true);
	}

	@PostConstruct
	private void initialize() {
		add(createPanel());
	}

	private Widget createPanel() {

		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setPixelSize(490, 420);
		vPanel.setSpacing(5);		
		vPanel.add(createCompanyPanel());
		
		vPanel.add(createGridPanel());
		
		return vPanel;
	}
	
	private HorizontalPanel createCompanyPanel(){
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setSpacing(10);
		hPanel.addStyleName(ThemeStyles.get().style().borderBottom());
		hPanel.addStyleName(ThemeStyles.get().style().borderTop());
		hPanel.add(getCompanyNamePanel());
		hPanel.add(getReportPanel());
		
		return hPanel;
	}	
	
	private VerticalPanel getCompanyNamePanel() {
		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setSpacing(5);
		companyNameField = new TextField();
		companyNameField.setEnabled(false);
		companyNameField.setWidth(FIELD_WIDTH);
		vPanel.add(new Label(UISgfMessages.INSTANCE.companyLabel()));
		vPanel.add(companyNameField);
		
		return vPanel;
		
	}

	private HorizontalPanel getReportPanel() {
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setSpacing(5);
//		companyCifField = new TextField();
//		companyCifField.setEnabled(false);
//		companyCifField.setWidth(FIELD_WIDTH);
//		vPanel.add(new Label(UISgfMessages.INSTANCE.cifLabel()));		
//		vPanel.add(companyCifField);
		hPanel.add(createLayerManagerButton());	
		hPanel.add(editLayerDataTool);
		
		
		return hPanel;
		
	}
	
	private TextButton createLayerManagerButton() {		
		TextButton showButton = new TextButton();
		showButton.setIcon(ImageProvider.INSTANCE.layerIcon());
		showButton.setText(UIMessages.INSTANCE.layerManagerToolText());
		showButton.setTitle(UIMessages.INSTANCE.layerManagerToolTip());
		showButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				if (layerMangerWidget.asWidget().isVisible()) {
					layerMangerWidget.asWidget().getElement().<FxElement> cast()
							.fadeToggle();
				} else {
					layerMangerWidget.asWidget().getElement().<FxElement> cast()
							.fadeToggle();
					layerMangerWidget.asWidget().setVisible(true);
				}
			}
		});

		return showButton;
	}

	private HorizontalPanel createGridPanel() {
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setSize("510px", "320px");
		
		vehicleStore = new ListStore<VehicleJSO>(vehicleProps.key());
		vehiculeGrid = createGrid(vehicleStore, vehicleProps);
		
		vehiculeGrid.getSelectionModel().addSelectionChangedHandler(
				new SelectionChangedHandler<VehicleJSO>() {
					@Override
					public void onSelectionChanged(
							SelectionChangedEvent<VehicleJSO> event) {
						List<VehicleJSO> selected = event.getSelection();						
						vehicleToolBar.setVehicles(selected);						
					}
				});

		VerticalLayoutContainer gridContainer = new VerticalLayoutContainer();
		gridContainer.setWidth(500);
		gridContainer.setHeight(320);
		gridContainer.add(vehiculeGrid, new VerticalLayoutData(1, 1));				
		hPanel.add(gridContainer);
		hPanel.add(vehicleToolBar);
		return hPanel;
	}

	public void setVehicle(List<VehicleJSO> vehicles) {
		vehicleStore.clear();		
		vehicleStore.addAll(vehicles);		
		vehiculeGrid.getView().refresh(true);
	}
		
	public void setSession(SessionJSO session) {
		
		CompanyJSO company = session.getCompany();
		this.companyNameField.setText(company.getName());
		//this.companyCifField.setText(company.getCif());
		
		vehicleToolBar.setSession(session);
	}
	
	private Grid<VehicleJSO> createGrid(ListStore<VehicleJSO> dataStore,
			VehicleJSOProperties properties) {
		
		StringFilter<VehicleJSO> plateFilter = new StringFilter<VehicleJSO>(properties.plate());
		StringFilter<VehicleJSO> nameFilter = new StringFilter<VehicleJSO>(properties.name());
		GridFilters<VehicleJSO> filters = new GridFilters<VehicleJSO>();
		
		
		RowExpander<VehicleJSO> rowExpander = createRowExpander();
		ColumnModel<VehicleJSO> columnModel = createColumnList(properties, rowExpander);
		Grid<VehicleJSO> grid = new Grid<VehicleJSO>(dataStore, columnModel);		
		
		grid.setBorders(true);
		grid.getView().setForceFit(true);				
		grid.getView().setAutoExpandColumn(columnModel.getColumn(2));
		grid.getView().setStripeRows(true);
		grid.getView().setColumnLines(true);
		grid.setBorders(true);
		grid.setColumnReordering(true);
		rowExpander.initPlugin(grid);	
		
		filters.initPlugin(grid);
		filters.setLocal(true);		 
		filters.addFilter(nameFilter);
		filters.addFilter(plateFilter);
		
		return grid;
	}
	
	private RowExpander<VehicleJSO> createRowExpander() {
		
		return new RowExpander<VehicleJSO>(new AbstractCell<VehicleJSO>() {
			@Override
			public void render(Context context, VehicleJSO value,
					SafeHtmlBuilder sb) {
				
				String comment = value.getComments();
				if( comment == null) {
					comment = UISgfMessages.INSTANCE.noCommentLabel();
				}
				
				sb.appendHtmlConstant("<p style='margin: 5px 5px 10px'><b>"
						+ UISgfMessages.INSTANCE.commentColumn() + ":</b> "
						+ comment + "</p>");
				
			}
		});
	}
	
	private ColumnModel<VehicleJSO> createColumnList(VehicleJSOProperties props, 
			RowExpander<VehicleJSO> rowExpander) {
		
		rowExpander.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		rowExpander.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		ColumnConfig<VehicleJSO, String> nameColumn = new ColumnConfig<VehicleJSO, String>(
				props.name(), 200, SafeHtmlUtils.fromTrustedString("<b>"
						+ UISgfMessages.INSTANCE.nameColumn() + "</b>"));
		
		nameColumn.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		
		ColumnConfig<VehicleJSO, String> plateColumn = new ColumnConfig<VehicleJSO, String>(
				props.plate(), 200, SafeHtmlUtils.fromTrustedString("<b>"
						+ UISgfMessages.INSTANCE.plateColumn() + "</b>"));
		
		plateColumn.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);		
		
		ColumnConfig<VehicleJSO, String> statusColumn = new ColumnConfig<VehicleJSO, String>(
				props.status(), 200, SafeHtmlUtils.fromTrustedString("<b>"
						+ UISgfMessages.INSTANCE.statusColumn() + "</b>"));
		statusColumn.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		
		ColumnConfig<VehicleJSO, String> lastRevisionDateColumn = new ColumnConfig<VehicleJSO, String>(
				props.lastRevisionDate(), 200, SafeHtmlUtils.fromTrustedString("<b>"
						+ UISgfMessages.INSTANCE.lastReviewColumn() + "</b>"));
		lastRevisionDateColumn.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);	
		
		ColumnConfig<VehicleJSO, String> kmRevisionColumn = new ColumnConfig<VehicleJSO, String>(
				props.kmsLeftForRevision(), 200, SafeHtmlUtils.fromTrustedString("<b>"
						+ UISgfMessages.INSTANCE.kmForReviewColumn() + "</b>"));
		kmRevisionColumn.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);	
				
		List<ColumnConfig<VehicleJSO, ?>> columns = new ArrayList<ColumnConfig<VehicleJSO, ?>>();
		columns.add(rowExpander);
		columns.add(nameColumn);
		columns.add(plateColumn);
		columns.add(statusColumn);	
		columns.add(lastRevisionDateColumn);
		columns.add(kmRevisionColumn);
		
		return new ColumnModel<VehicleJSO>(columns);
	}
}
