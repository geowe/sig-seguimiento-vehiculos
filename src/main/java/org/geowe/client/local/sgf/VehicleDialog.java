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
import org.geowe.client.local.main.tool.map.catalog.dialog.LayerDefProperties;
import org.geowe.client.local.main.tool.map.catalog.model.LayerDef;
import org.geowe.client.local.messages.UICatalogMessages;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.model.vector.VectorLayer;
import org.geowe.client.local.ui.MessageDialogBuilder;
import org.geowe.client.local.ui.PagingFeatureGrid;
import org.geowe.client.shared.rest.sgf.model.jso.CompanyJSO;
import org.geowe.client.shared.rest.sgf.model.jso.VehicleJSO;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.slf4j.Logger;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
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
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.RowExpander;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import com.sencha.gxt.widget.core.client.toolbar.PagingToolBar;

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
//	@Inject
//	private LayerSearchToolBar layerSearchToolBar;
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
		//vPanel.setSpacing(5);
		vPanel.add(createTopPanel());
		vPanel.add(createBottomPanel());

		return vPanel;
	}

	private HorizontalLayoutContainer createTopPanel() {
		
		HorizontalLayoutContainer hPanel = new HorizontalLayoutContainer();

		VerticalPanel infoPanel = new VerticalPanel();
		//infoPanel.setPixelSize(490, 120);
		infoPanel.setSpacing(5);

//		companyNameField = new TextField();
//		companyNameField.setEnabled(false);
//		companyNameField.setWidth(FIELD_WIDTH);
//		infoPanel.add(new Label("Compañía"));
//		infoPanel.add(companyNameField);


		infoPanel.add(createCompanyPanel());
		//infoPanel.add(createSearchButtonPanel());

		hPanel.add(infoPanel);

		return hPanel;
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
	
//	private VerticalPanel getAttrCombo() {
//		VerticalPanel vPanel = new VerticalPanel();
//		vPanel.setSpacing(5);
//		vPanel.add(new Label(UIMessages.INSTANCE.asdAttributeLabel()));
//		initializeAttributeLabelCombo(FIELD_WIDTH);
//		vPanel.add(attributeCombo);
//
//		return vPanel;
//	}
	
	private VerticalPanel getCompanyNamePanel() {
		VerticalPanel vPanel = new VerticalPanel();
		//vPanel.setSpacing(5);
		companyNameField = new TextField();
		companyNameField.setEnabled(false);
		companyNameField.setWidth(FIELD_WIDTH);
		vPanel.add(new Label("Compañía"));
		vPanel.add(companyNameField);
		
		return vPanel;
		
	}

	private VerticalPanel getCompanyCifPanel() {
		VerticalPanel vPanel = new VerticalPanel();
		//vPanel.setSpacing(5);
		companyCifField = new TextField();
		companyCifField.setEnabled(false);
		companyCifField.setWidth(FIELD_WIDTH);
		vPanel.add(new Label("CIF"));
		vPanel.add(companyCifField);
		
		return vPanel;
		
	}

	private HorizontalLayoutContainer createBottomPanel() {
		HorizontalLayoutContainer hPanel = new HorizontalLayoutContainer();
		hPanel.setSize("510px", "380px");//"220px"
		
		vehicleStore = new ListStore<VehicleJSO>(vehicleProps.key());
		Grid<VehicleJSO> vehiculeGrid = createGrid(vehicleStore, vehicleProps);
		
		

		VerticalLayoutContainer gridContainer = new VerticalLayoutContainer();
		gridContainer.setWidth(500);
		gridContainer.setHeight(380);
		gridContainer.add(vehiculeGrid, new VerticalLayoutData(1, 1));
				
		hPanel.add(gridContainer);
		
		

//		PagingToolBar toolBar = new PagingToolBar(FEATURES_PER_PAGE);
//		toolBar.setBorders(false);
//
//		featureGrid = new PagingFeatureGrid(toolBar);
//		featureGrid.getSelectionModel().addSelectionChangedHandler(
//				new SelectionChangedHandler<VectorFeature>() {
//					@Override
//					public void onSelectionChanged(
//							SelectionChangedEvent<VectorFeature> event) {
//						//setSelectedElements();
//					}
//				});
//
//		VerticalLayoutContainer gridContainer = new VerticalLayoutContainer();
//		gridContainer.setWidth(500);
//		gridContainer.setHeight(220);
//		gridContainer.add(featureGrid, new VerticalLayoutData(1, 1));
//		gridContainer.add(toolBar, new VerticalLayoutData(1, -1));
//
//		
//		hPanel.add(gridContainer);
		//hPanel.add(layerSearchToolBar);
		return hPanel;
	}

//	private HorizontalPanel createSearchButtonPanel() {
//		HorizontalPanel hPanel = new HorizontalPanel();
//		hPanel.setSpacing(10);
//		searchButton = getSearchMenuButton();
//		hPanel.add(searchButton);
//
//		isCaseSensitive = new CheckBox();
//		isCaseSensitive.setBoxLabel(UIMessages.INSTANCE.caseSensitive());
//		isCaseSensitive.setValue(true);
//		hPanel.add(isCaseSensitive);
//		return hPanel;
//	}

//	private TextButton getSearchMenuButton() {
//		TextButton searchButton = new TextButton(
//				UIMessages.INSTANCE.asdSearchButtonText());
//		searchButton.setMenu(getSearchMenu());
//		return searchButton;
//	}
//
//	private Menu getSearchMenu() {
//		Menu menu = new Menu();
//		menu.add(new MenuItem(UIMessages.INSTANCE.exactEqual(),
//				new SelectionHandler<MenuItem>() {
//					@Override
//					public void onSelection(SelectionEvent<MenuItem> event) {
//						search(new EqualSearcher());
//					}
//				}));
//
//		menu.add(new MenuItem(UIMessages.INSTANCE.contains(),
//				new SelectionHandler<MenuItem>() {
//					@Override
//					public void onSelection(SelectionEvent<MenuItem> event) {
//						search(new ContainSearcher());
//					}
//				}));
//
//		menu.add(new MenuItem(UIMessages.INSTANCE.startsWith(),
//				new SelectionHandler<MenuItem>() {
//					@Override
//					public void onSelection(SelectionEvent<MenuItem> event) {
//						search(new StartSearcher());
//					}
//				}));
//
//		menu.add(new MenuItem(UIMessages.INSTANCE.endsWith(),
//				new SelectionHandler<MenuItem>() {
//					@Override
//					public void onSelection(SelectionEvent<MenuItem> event) {
//						search(new EndSearcher());
//					}
//				}));
//		menu.add(new MenuItem(UIMessages.INSTANCE.greater(),
//				new SelectionHandler<MenuItem>() {
//					@Override
//					public void onSelection(SelectionEvent<MenuItem> event) {
//						search(new GreaterThanSearcher());
//					}
//				}));
//		menu.add(new MenuItem(UIMessages.INSTANCE.smaller(),
//				new SelectionHandler<MenuItem>() {
//					@Override
//					public void onSelection(SelectionEvent<MenuItem> event) {
//						search(new SmallerThanSearcher());
//					}
//				}));
//
//		return menu;
//	}

//	private void initializeAttributeLabelCombo(String width) {
//		ListStore<FeatureAttributeDef> attributeLabelStore = new ListStore<FeatureAttributeDef>(
//				new ModelKeyProvider<FeatureAttributeDef>() {
//					@Override
//					public String getKey(FeatureAttributeDef item) {
//						return (item == null) ? null : item.getName();
//					}
//
//				});
//
//		attributeCombo = new ComboBox<FeatureAttributeDef>(attributeLabelStore,
//				new LabelProvider<FeatureAttributeDef>() {
//					@Override
//					public String getLabel(FeatureAttributeDef item) {
//						return (item == null) ? null : item.getName();
//					}
//				});
//
//		attributeCombo.setWidth(width);
//		attributeCombo.setTypeAhead(true);
//		attributeCombo.setEmptyText(UIMessages.INSTANCE
//				.asdAttributeComboEmptyText());
//		attributeCombo.setTriggerAction(TriggerAction.ALL);
//		attributeCombo.setForceSelection(true);
//		attributeCombo.setEditable(false);
//		attributeCombo.enableEvents();
//	}
//
//	private void search(Searcher searcher) {
//		String attributeName = attributeCombo.getValue().getName();
//		String attributeValue = valueAttributeField.getValue();
//		List<VectorFeature> filteredFeatures = null;
//		try {
//			List<VectorFeature> features = Arrays.asList(selectedLayer
//					.getFeatures());
//
//			filteredFeatures = searcher.search(features, attributeValue,
//					attributeName, isCaseSensitive.getValue());
//
//		} catch (Exception e) {
//			messageDialogBuilder.createError(UIMessages.INSTANCE.warning(),
//					e.getMessage());
//			logger.error(e.getMessage());
//		}
//
//		featureGrid.update(filteredFeatures);
//	}

//	public void setSelectedLayer(VectorLayer layer) {
//		selectedLayer = layer;
//		layerNameField.setText(selectedLayer.getName());
//
//		// Initilize the featureGrid for the new layer
//		featureGrid.rebuild(getFeatures(layer));
//		featureGrid.clear();
//		updateLayerAttributes();
//
//		valueAttributeField.clear();
//	}
	
	public void setVehicle(List<VehicleJSO> vehicles) {
		vehicleStore.clear();
		vehicleStore.addAll(vehicles);
	}
		
	
	public void setCompany(CompanyJSO company) {
		this.companyNameField.setText(company.getName());
		this.companyCifField.setText(company.getCif());
	}

	
//	private void updateLayerAttributes() {
//		try {
//			attributeCombo.getStore().clear();
//			attributeCombo.setEmptyText(UIMessages.INSTANCE
//					.asdAttributeComboEmptyText());
//			if (selectedLayer != null && selectedLayer.getAttributes() != null) {
//				attributeCombo.getStore().addAll(selectedLayer.getAttributes());
//			}
//		} catch (Exception e) {
//			logger.error(
//					"Error reloading label atribute combo: " + e.getMessage(),
//					e);
//		}
//	}

//	private void setSelectedElements() {
//		List<VectorFeature> selectedElements = featureGrid.getSelectionModel()
//				.getSelectedItems();
//
//		if (selectedElements == null || selectedElements.isEmpty()) {
//			Info.display(UIMessages.INSTANCE.warning(),
//					UIMessages.INSTANCE.selectAtLeast(1));
//		} else {
//			for (FeatureTool tool : layerSearchToolBar.getTools()) {
//				setSelectedElement(selectedElements, tool);
//			}
//		}
//	}

//	private void setSelectedElement(List<VectorFeature> selectedElements,
//			FeatureTool tool) {
//		tool.setSelectedLayer(selectedLayer);
//
//		if (selectedElements.size() > 1) {
//			tool.setSelectedFeatures(selectedElements);
//		} else {
//			tool.setSelectedFeature(selectedElements.get(0));
//		}
//	}
//
//	private void addKeyShortcuts() {
//		KeyShortcutHandler keyShortcut = new KeyShortcutHandler(searchButton,
//				KeyCodes.KEY_ENTER);
//
//		layerNameField.addKeyDownHandler(keyShortcut);
//		valueAttributeField.addKeyDownHandler(keyShortcut);
//		attributeCombo.addKeyDownHandler(keyShortcut);
//	}
	
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
		
//		ColumnConfig<LayerDef, String> typeColumn = new ColumnConfig<LayerDef, String>(
//				props.type(), 75, UICatalogMessages.INSTANCE.type());
//		typeColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
//		typeColumn.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);		
		
		
				
		List<ColumnConfig<VehicleJSO, ?>> columns = new ArrayList<ColumnConfig<VehicleJSO, ?>>();
		columns.add(rowExpander);
		columns.add(plateColumn);
		columns.add(statusColumn);	
		columns.add(lastRevisionDateColumn);
		
		
		return new ColumnModel<VehicleJSO>(columns);
	}
}
