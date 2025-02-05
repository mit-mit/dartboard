/*******************************************************************************
 * Copyright (c) 2019 vogella GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Jonas Hungershausen - initial API and implementation
 *******************************************************************************/
package org.eclipse.dartboard.preference;

import java.util.Optional;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.dartboard.CommandLineTools;
import org.eclipse.dartboard.Constants;
import org.eclipse.dartboard.Messages;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

public class DartPreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		ScopedPreferenceStore scopedPreferenceStore = new ScopedPreferenceStore(InstanceScope.INSTANCE,
				Constants.PLUGIN_ID);

		if (scopedPreferenceStore.getString(Constants.PREFERENCES_SDK_LOCATION).isEmpty()) {
			Optional<String> dartSDKLocation = CommandLineTools.getDartSDKLocation();
			if (dartSDKLocation.isPresent()) {
				scopedPreferenceStore.setDefault(Constants.PREFERENCES_SDK_LOCATION, dartSDKLocation.get());
			} else {
				MessageDialog.openError(null, Messages.Preference_SDKNotFound_Title,
						Messages.Preference_SDKNotFound_Body + getSearchedLocations());
			}
		}
	}

	/**
	 * Appends all locations in {@link CommandLineTools#POSSIBLE_DART_LOCATIONS}
	 * into a list separated by line breaks.
	 * 
	 * @return A {@link String} of all searched Dart SDK location
	 */
	private String getSearchedLocations() {
		StringBuilder builder = new StringBuilder();
		for (String string : CommandLineTools.POSSIBLE_DART_LOCATIONS) {
			builder.append("\n"); //$NON-NLS-1$
			builder.append("- "); //$NON-NLS-1$
			builder.append(string);
		}
		return builder.toString();
	}
}
