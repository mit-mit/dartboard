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
 *     Jonas Hungershausen - https://github.com/eclipse/dartboard/issues/1
 *******************************************************************************/
package org.eclipse.dartboard.launch;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.dartboard.Constants;
import org.eclipse.dartboard.launch.console.DartConsoleFactory;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link ILaunchShortcut} used to launch a single *.dart file.
 * 
 * The launch shortcut can be triggered from the context menu of a file in the
 * Project Explorer. It will run the selected file using the Dart sdk from the
 * preferences.
 * 
 * For more advanced settings the project should be launched using the
 * {@link LaunchShortcut} for projects.
 * 
 * @author Jonas Hungershausen
 * @see LaunchShortcut
 *
 */
public class LaunchFileShortcut implements ILaunchShortcut {

	private static final Logger LOG = LoggerFactory.getLogger(LaunchFileShortcut.class);

	private ScopedPreferenceStore preferences = new ScopedPreferenceStore(InstanceScope.INSTANCE, Constants.PLUGIN_ID);

	@Override
	public void launch(ISelection selection, String mode) {
		if (selection instanceof StructuredSelection) {
			Object firstElement = ((StructuredSelection) selection).getFirstElement();
			if (firstElement instanceof IFile) {
				IFile file = (IFile) firstElement;
				launch(file.getLocation(), null);
			}
		}
	}

	@Override
	public void launch(IEditorPart editor, String mode) {
		// TODO: Can't seem to trigger this part. If I understand correctly, this is
		// triggered when a launch config is triggered from an active editor. But
		// somehow the option is not shown for dart editors.
	}

	protected void launch(IPath file, String sdk) {
		if (sdk == null) {
			sdk = preferences.getString(Constants.PREFERENCES_SDK_LOCATION);
		}
		ProcessBuilder processBuilder = new ProcessBuilder(sdk + "/bin/dart", file.toOSString()); //$NON-NLS-1$

		try {
			Process process = processBuilder.start();
			new DartConsoleFactory(process.getInputStream()).openConsole();
		} catch (IOException e) {
			LOG.error(e.getMessage());
		}
	}
}
