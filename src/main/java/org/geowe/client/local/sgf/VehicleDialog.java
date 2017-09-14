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
import org.geowe.client.local.messages.UICatalogMessages;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.geowe.client.local.ui.PagingFeatureGrid;
import org.geowe.client.shared.rest.sgf.model.jso.CompanyJSO;
import org.geowe.client.shared.rest.sgf.model.jso.VehicleJSO;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
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
import com.sencha.gxt.core.client.resources.ThemeStyles;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.RowExpander;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

/**
 * Diálogo para la búsqueda de elementos, por atributo, de la capa seleccionada
 * 
 * @author geowe
 *
 */
@ApplicationScoped
public class VehicleDialog extends Dialog {
	//public static final int FEATURES_PER_PAGE = 50;
	private static final String FIELD_WIDTH = "225px";
	@Inject
	private Logger logger;
	@Inject
	private VehicleToolBar vehicleToolBar;
	@Inject
	private MessageDialogBuilder messageDialogBuilder;

	private TextField companyNameField;
	private TextField companyCifField;
	//private TextButton searchButton;

	//private VectorLayer selectedLayer;
	private PagingFeatureGrid featureGrid;

	//private ComboBox<FeatureAttributeDef> attributeCombo;

	//private CheckBox isCaseSensitive;
	
	private ListStore<VehicleJSO> vehicleStore;
	private static final VehicleJSOProperties vehicleProps = GWT.create(VehicleJSOProperties.class);
	//private Grid<VehicleJSO> vehicleGrid;
	

	public VehicleDialog() {
		super();
		this.getHeader().setIcon(ImageProvider.INSTANCE.layer16());
		this.setHeadingText("Vehículos");
		this.setPredefinedButtons(PredefinedButton.CLOSE);
		this.setPixelSize(560, 520);
		this.setModal(false);
		this.setResizable(false);
		this.setHideOnButtonClick(true);
	}

	@PostConstruct
	private void initialize() {
		add(createPanel());
		//addKeyShortcuts();
	}

	private Widget createPanel() {

		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setPixelSize(490, 420);
		//vPanel.setPixelSize(490, 90);
		vPanel.setSpacing(5);
		
		//vPanel.add(createTopPanel());
		vPanel.add(createCompanyPanel());
		vPanel.add(createGridPanel());
		
		
		

		return vPanel;
	}

	
	private HorizontalPanel createCompanyPanel(){
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setSpacing(10);
		hPanel.addStyleName(ThemeStyles.get().style().borderBottom());
		hPanel.addStyleName(ThemeStyles.get().style().borderTop());
		//hPanel.add(getAttrCombo());
		hPanel.add(getCompanyNamePanel());
		hPanel.add(getCompanyCifPanel());
		
		return hPanel;
	}
	
	
	private VerticalPanel getCompanyNamePanel() {
		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setSpacing(5);
		companyNameField = new TextField();
		companyNameField.setEnabled(false);
		companyNameField.setWidth(FIELD_WIDTH);
		vPanel.add(new Label("Compañía"));
		vPanel.add(companyNameField);
		
		return vPanel;
		
	}

	private VerticalPanel getCompanyCifPanel() {
		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setSpacing(5);
		companyCifField = new TextField();
		companyCifField.setEnabled(false);
		companyCifField.setWidth(FIELD_WIDTH);
		vPanel.add(new Label("CIF"));
		vPanel.add(companyCifField);
		
		return vPanel;
		
	}

	private HorizontalPanel createGridPanel() {
//		HorizontalLayoutContainer hPanel = new HorizontalLayoutContainer();
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setSize("510px", "320px");//"220px"
		
		vehicleStore = new ListStore<VehicleJSO>(vehicleProps.key());
		Grid<VehicleJSO> vehiculeGrid = createGrid(vehicleStore, vehicleProps);
		
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
	}
		
	
	public void setCompany(CompanyJSO company) {
		this.companyNameField.setText(company.getName());
		this.companyCifField.setText(company.getCif());
	}

	

	
	private Grid<VehicleJSO> createGrid(ListStore<VehicleJSO> dataStore,
			VehicleJSOProperties properties) {
		
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
		
		return grid;
	}
	
	private RowExpander<VehicleJSO> createRowExpander() {
		return new RowExpander<VehicleJSO>(new AbstractCell<VehicleJSO>() {
			@Override
			public void render(Context context, VehicleJSO value,
					SafeHtmlBuilder sb) {
				sb.appendHtmlConstant("<p style='margin: 5px 5px 10px'><b>"
						+ UICatalogMessages.INSTANCE.description() + ":</b> "
						+ value.getKmsLeftForRevision() + "</p>");
			}
		});
	}
	
	private ColumnModel<VehicleJSO> createColumnList(VehicleJSOProperties props, 
			RowExpander<VehicleJSO> rowExpander) {
		
		rowExpander.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		rowExpander.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		
		ColumnConfig<VehicleJSO, String> plateColumn = new ColumnConfig<VehicleJSO, String>(
				props.plate(), 200, SafeHtmlUtils.fromTrustedString("<b>"
						+ "Matrícula" + "</b>"));
		
		plateColumn.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);		
		
		ColumnConfig<VehicleJSO, String> statusColumn = new ColumnConfig<VehicleJSO, String>(
				props.status(), 200, SafeHtmlUtils.fromTrustedString("<b>"
						+ "Estado" + "</b>"));
		statusColumn.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		
		ColumnConfig<VehicleJSO, String> lastRevisionDateColumn = new ColumnConfig<VehicleJSO, String>(
				props.lastRevisionDate(), 200, SafeHtmlUtils.fromTrustedString("<b>"
						+ "Última revisión" + "</b>"));
		lastRevisionDateColumn.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);	
		
				
		List<ColumnConfig<VehicleJSO, ?>> columns = new ArrayList<ColumnConfig<VehicleJSO, ?>>();
		columns.add(rowExpander);
		columns.add(plateColumn);
		columns.add(statusColumn);	
		columns.add(lastRevisionDateColumn);
		
		
		return new ColumnModel<VehicleJSO>(columns);
	}
}
