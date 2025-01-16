/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.core;

import org.eclipse.core.runtime.IPath;

/**
 * A jar entry corresponding to a non-Java resource in an archive {@link IPackageFragment} or {@link IPackageFragmentRoot}.
 * <p>
 * One can navigate the non-Java resource tree using the {@link #getChildren()} and {@link #getParent()} methods.
 * Jar entry resources are either files ({@link #isFile()} returns true) or directories ({@link #isFile()} returns false).
 * Files don't have any children and the returned array is always empty.
 * </p><p>
 * Jar entry resources that refer to the same element are guaranteed to be equal, but not necessarily identical.
 * </p>
 *
 * @since 3.3
 */
public interface IJarEntryResource {}
