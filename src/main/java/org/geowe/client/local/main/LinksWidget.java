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
package org.geowe.client.local.main;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.geowe.client.local.ImageProvider;
import org.geowe.client.local.layermanager.LayerManagerWidget;
import org.geowe.client.local.layermanager.LayerTree;
import org.geowe.client.local.main.map.SimpleMapVerticalLegend;
import org.geowe.client.local.messages.UIMessages;
import org.geowe.client.local.sgf.VehicleTool;
import org.geowe.client.local.sgf.messages.UISgfMessages;
import org.geowe.client.local.welcome.Welcome;
import org.gwtopenmaps.openlayers.client.layer.Layer;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.ScrollSupport;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.fx.client.FxElement;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

@ApplicationScoped
public class LinksWidget implements IsWidget {

	private HorizontalLayoutContainer widget;
	private HorizontalPanel hp;
	private SimpleMapVerticalLegend mapLegend;

	@Inject
	private StatusPanelWidget statusPanelWidget;
	
	@Inject
	private PreviewWidget previewWidget;
	
	@Inject
	private LayerManagerWidget layerManager;
	
	@Inject
	private VehicleTool vehicleTool;
	
	@Inject
	private Welcome welcome;

	@Override
	public Widget asWidget() {
		if (widget == null) {
			//String width="320px";
			String width="370px";
			String height="50px";
			widget = new HorizontalLayoutContainer();
			widget.getElement().getStyle().setPosition(Position.ABSOLUTE);
			widget.getElement().getStyle().setRight(5, Unit.PX);
			widget.getElement().getStyle().setTop(0, Unit.PX);
			widget.setSize(width, height);
			
			hp = new HorizontalPanel();
			hp.setSpacing(10);
			StyleInjector.inject(".linkPanel { " + "background: #FFFFFF;"
					+ "border-radius: 5px 10px;" + "opacity: 0.8}");
			hp.setStyleName("linkPanel");
			hp.setSize(width, height);
			
			ScrollSupport scrollSupport = widget.getScrollSupport();
			scrollSupport.setScrollMode(ScrollMode.AUTOX);
			
			setDefaultLinks();
			
			widget.add(hp);
		}
		return widget;
	}

	private void setDefaultLinks() {
		AnchorBuilder abuilder = new AnchorBuilder();
		hp.add(createExitDialogAnchor());
		hp.add(vehicleTool);
		hp.add(createPreviewDialogAnchor());
//		hp.add(abuilder.getGeoWEWebLink());
//		hp.add(abuilder.getGooglePlusLink());
//		hp.add(abuilder.getFaceBookLink());
//		hp.add(abuilder.getTwiterLink());
//		hp.add(abuilder.getBugLink());
		hp.add(createLegendAnchor());
		hp.add(createStatusPanelAnchor());
	}
	
	public void addLink(Anchor anchor){
		hp.add(anchor);
	}
	
	public void removeAllLinks(){
		hp.clear();
	}
	
	private TextButton createExitDialogAnchor() {
		TextButton showButton = new TextButton();
		showButton.setIcon(ImageProvider.INSTANCE.exit());
		showButton.setText(UISgfMessages.INSTANCE.exit());
		showButton.setTitle(UISgfMessages.INSTANCE.exit());
		showButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				forceReload();
			}
		});

		return showButton;
	}
	
	
	public static native void forceReload() /*-{
	    $wnd.location.reload(true);
	}-*/;
	
	private TextButton createPreviewDialogAnchor() {
		TextButton showButton = new TextButton();
		showButton.setIcon(ImageProvider.INSTANCE.printLogo());
		showButton.setTitle("Preview");
		showButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				showHidePreviewDialog();
			}
		});

		return showButton;
	}
	
	private TextButton createStatusPanelAnchor() {
		TextButton showButton = new TextButton();
		showButton.setIcon(ImageProvider.INSTANCE.info24());
		showButton.setTitle(UIMessages.INSTANCE.statusPanelTile());
		showButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				showHideStatusBar();
			}
		});

		return showButton;
	}

	private TextButton createLegendAnchor() {
		TextButton legendButton = new TextButton();
		legendButton.setIcon(ImageProvider.INSTANCE.mapLegend24());
		legendButton.setTitle(UIMessages.INSTANCE.mapLegendTitle());
		legendButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				if (mapLegend != null && mapLegend.isVisible()) {
					mapLegend.getElement().<FxElement> cast().fadeToggle();
					RootPanel.get().remove(mapLegend);
				} else {
					mapLegend = new SimpleMapVerticalLegend(layerManager);
					RootPanel.get().add(mapLegend);
					mapLegend.getElement().<FxElement> cast().fadeToggle();
					mapLegend.setVisible(true);
				}
			}
		});
		return legendButton;
	}

	private void showHideStatusBar() {
		statusPanelWidget.showHideStatusBar();
	}
	
	private void showHidePreviewDialog() {
		previewWidget.showHidePreview();
	}
}
