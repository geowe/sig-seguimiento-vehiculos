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

import org.geowe.client.shared.rest.sgf.model.jso.VehicleJSO;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

/**
 * Interfaz que define la representación de un LayerDef en una lista o tabla,
 * indicando aquellos campos que estaran disponibles para ser mostrados.
 * 
 * @author Atanasio Muñoz
 *
 */
public interface VehicleJSOProperties extends PropertyAccess<VehicleJSO> {
	@Path("plate")
	ModelKeyProvider<VehicleJSO> key();

	@Path("plate")
	LabelProvider<VehicleJSO> plateLabel();

	ValueProvider<VehicleJSO, String> plate();
	
	ValueProvider<VehicleJSO, String> kmsLeftForRevision();

	ValueProvider<VehicleJSO, String> lastRevisionDate();

	ValueProvider<VehicleJSO, String> status();

}
