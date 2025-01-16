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
 *     IBM Corporation - added getOption(String, boolean), getOptions(boolean) and setOptions(Map)
 *     IBM Corporation - deprecated getPackageFragmentRoots(IClasspathEntry) and
 *                               added findPackageFragmentRoots(IClasspathEntry)
 *     IBM Corporation - added isOnClasspath(IResource)
 *     IBM Corporation - added setOption(String, String)
 *     IBM Corporation - added forceClasspathReload(IProgressMonitor)
 *******************************************************************************/
package org.eclipse.jdt.core;
import java.util.Map;



/**
 * A Java project represents a view of a project resource in terms of Java
 * elements such as package fragments, types, methods and fields.
 * A project may contain several package roots, which contain package fragments.
 * A package root corresponds to an underlying folder or JAR.
 * <p>
 * Each Java project has a classpath, defining which folders contain source code and
 * where required libraries are located. Each Java project also has an output location,
 * defining where the builder writes <code>.class</code> files. A project that
 * references packages in another project can access the packages by including
 * the required project in a classpath entry. The Java model will present the
 * source elements in the required project; when building, the compiler will use
 * the corresponding generated class files from the required project's output
 * location(s)). The classpath format is a sequence of classpath entries
 * describing the location and contents of package fragment roots.
 * <p>
 * Java project elements need to be opened before they can be navigated or manipulated.
 * The children of a Java project are the package fragment roots that are
 * defined by the classpath and contained in this project (in other words, it
 * does not include package fragment roots for other projects). The children
 * (i.e. the package fragment roots) appear in the order they are defined by
 * the classpath.
 * <p>
 * An instance of one of these handles can be created via
 * <code>JavaCore.create(project)</code>.
 * </p>
 *
 * @see JavaCore#create(org.eclipse.core.resources.IProject)
 * @see IClasspathEntry
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface IJavaProject extends IParent, IJavaElement, IOpenable {
	
public Map<String, String> getOptions(boolean p0);
}
