/*******************************************************************************
 * Copyright (c) 2000, 2019 IBM Corporation and others.
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



/**
 * A Java element delta describes changes in Java element between two discrete
 * points in time.  Given a delta, clients can access the element that has
 * changed, and any children that have changed.
 * <p>
 * Deltas have a different status depending on the kind of change they represent.
 * The list below summarizes each status (as returned by {@link #getKind})
 * and its meaning (see individual constants for a more detailed description):
 * <ul>
 * <li>{@link #ADDED} - The element described by the delta has been added.</li>
 * <li>{@link #REMOVED} - The element described by the delta has been removed.</li>
 * <li>{@link #CHANGED} - The element described by the delta has been changed in some way.
 * Specification of the type of change is provided by {@link #getFlags} which returns the following values:
 * <ul>
 * <li>{@link #F_ADDED_TO_CLASSPATH} - A classpath entry corresponding to the element
 * has been added to the project's classpath. This flag is only valid if the element is an
 * {@link IPackageFragmentRoot}.</li>
 * <li>{@link #F_ARCHIVE_CONTENT_CHANGED} - The contents of an archive
 * has changed in some way. This flag is only valid if the element is an {@link IPackageFragmentRoot}
 * which is an archive.</li>
 * <li>{@link #F_CHILDREN} - A child of the element has changed in some way.  This flag
 * is only valid if the element is an {@link IParent}.</li>
 * <li>{@link #F_CLASSPATH_REORDER} - A classpath entry corresponding to the element
 * has changed position in the project's classpath. This flag is only valid if the element is an
 * {@link IPackageFragmentRoot}.</li>
 * <li>{@link #F_CLOSED} - The underlying {@link org.eclipse.core.resources.IProject}
 * has been closed. This flag is only valid if the element is an {@link IJavaProject}.</li>
 * <li>{@link #F_CONTENT} - The contents of the element have been altered.  This flag
 * is only valid for elements which correspond to files.</li>
 *<li>{@link #F_FINE_GRAINED} - The delta is a fine-grained delta, that is, an analysis down
 * to the members level was done to determine if there were structural changes to members of the element.</li>
 * <li>{@link #F_MODIFIERS} - The modifiers on the element have changed in some way.
 * This flag is only valid if the element is an {@link IMember}.</li>
 * <li>{@link #F_OPENED} - The underlying {@link org.eclipse.core.resources.IProject}
 * has been opened. This flag is only valid if the element is an {@link IJavaProject}.</li>
 * <li>{@link #F_REMOVED_FROM_CLASSPATH} - A classpath entry corresponding to the element
 * has been removed from the project's classpath. This flag is only valid if the element is an
 * {@link IPackageFragmentRoot}.</li>
 * <li>{@link #F_SOURCEATTACHED} - The source attachment path or the source attachment root path
 * of a classpath entry corresponding to the element was added. This flag is only valid if the element is an
 * {@link IPackageFragmentRoot}.</li>
 * <li>{@link #F_SOURCEDETACHED} - The source attachment path or the source attachment root path
 * of a classpath entry corresponding to the element was removed. This flag is only valid if the element is an
 * {@link IPackageFragmentRoot}.</li>
 * <li>{@link #F_SUPER_TYPES} - One of the supertypes of an {@link IType} has changed.</li>
 * </ul>
 * </li>
 * </ul>
 * <p>
 * Move operations are indicated by other change flags, layered on top
 * of the change flags described above. If element A is moved to become B,
 * the delta for the  change in A will have status {@link #REMOVED},
 * with change flag {@link #F_MOVED_TO}. In this case,
 * {@link #getMovedToElement} on delta A will return the handle for B.
 * The  delta for B will have status {@link #ADDED}, with change flag
 * {@link #F_MOVED_FROM}, and {@link #getMovedFromElement} on delta
 * B will return the handle for A. (Note, the handle to A in this case represents
 * an element that no longer exists).
 * </p>
 * <p>
 * Note that the move change flags only describe the changes to a single element, they
 * do not imply anything about the parent or children of the element.
 * </p>
 * <p>
 * The {@link #F_ADDED_TO_CLASSPATH}, {@link #F_REMOVED_FROM_CLASSPATH} and
 * {@link #F_CLASSPATH_REORDER} flags are triggered by changes to a project's classpath. They do not mean that
 * the underlying resource was added, removed or changed. For example, if a project P already contains a folder src, then
 * adding a classpath entry with the 'P/src' path to the project's classpath will result in an {@link IJavaElementDelta}
 * with the {@link #F_ADDED_TO_CLASSPATH} flag for the {@link IPackageFragmentRoot} P/src.
 * On the contrary, if a resource is physically added, removed or changed and this resource corresponds to a classpath
 * entry of the project, then an {@link IJavaElementDelta} with the {@link #ADDED},
 * {@link #REMOVED}, or {@link #CHANGED} kind will be fired.
 * </p>
 * <p>
 * Note that when a source attachment path or a source attachment root path is changed, then the flags of the delta contain
 * both {@link #F_SOURCEATTACHED} and {@link #F_SOURCEDETACHED}.
 * </p>
 * <p>
 * No assumptions should be made on whether the java element delta tree is rooted at the {@link IJavaModel}
 * level or not.
 * </p>
 * <p>
 * {@link IJavaElementDelta} object are not valid outside the dynamic scope
 * of the notification.
 * </p>
 *
 * @see IElementChangedListener
 * @see ElementChangedEvent
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface IJavaElementDelta {}
