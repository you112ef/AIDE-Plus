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

import org.eclipse.core.runtime.IPath;

/**
 * An entry on a Java project classpath identifying one or more package fragment
 * roots. A classpath entry has a content kind (either source,
 * {@link IPackageFragmentRoot#K_SOURCE}, or binary, {@link IPackageFragmentRoot#K_BINARY}), which is inherited
 * by each package fragment root and package fragment associated with the entry.
 * <p>
 * A classpath entry can refer to any of the following:<ul>
 *	<li>Source code in the current project. In this case, the entry identifies a
 *		root folder in the current project containing package fragments and
 *		source files with one of the {@link JavaCore#getJavaLikeExtensions()
 *		Java-like extensions}. The root folder itself represents a default
 *		package, subfolders represent package fragments, and files with a
 *     Java-like extension (e.g. <code>.java</code> files)
 *		represent compilation units. All compilation units will be compiled when
 * 		the project is built. The classpath entry must specify the
 *		absolute path to the root folder. Entries of this kind are
 *		associated with the {@link #CPE_SOURCE} constant.
 *      Source classpath entries can carry inclusion and exclusion patterns for
 *      selecting which source files appear as compilation
 *      units and get compiled when the project is built.
 *  </li>
 *
 *	<li>A binary library in the current project, in another project, or in the external
 *		file system. In this case the entry identifies a JAR (or root folder) containing
 *		package fragments and <code>.class</code> files.  The classpath entry
 *		must specify the absolute path to the JAR (or root folder), and in case it refers
 *		to an external JAR, then there is no associated resource in the workbench. Entries
 *		of this kind are associated with the {@link #CPE_LIBRARY} constant.</li>
 *
 *	<li>A required project. In this case the entry identifies another project in
 *		the workspace. The required project is used as a binary library when compiling
 *		(that is, the builder looks in the output location of the required project
 *		for required <code>.class</code> files when building). When performing other
 *		"development" operations - such as code assist, code resolve, type hierarchy
 *		creation, etc. - the source code of the project is referred to. Thus, development
 *		is performed against a required project's source code, and compilation is
 *		performed against a required project's last built state.  The
 *		classpath entry must specify the absolute path to the
 *		project. Entries of this kind are  associated with the {@link #CPE_PROJECT}
 *		constant.
 * 		Note: referencing a required project with a classpath entry refers to the source
 *     code or associated <code>.class</code> files located in its output location.
 *     It will also automatically include any other libraries or projects that the required project's classpath
 *     refers to, iff the corresponding classpath entries are tagged as being exported
 *     ({@link IClasspathEntry#isExported}).
 *    Unless exporting some classpath entries, classpaths are not chained by default -
 *    each project must specify its own classpath in its entirety.</li>
 *
 *  <li> A path beginning in a classpath variable defined globally to the workspace.
 *		Entries of this kind are  associated with the {@link #CPE_VARIABLE} constant.
 *      Classpath variables are created using {@link JavaCore#setClasspathVariable(String, IPath, org.eclipse.core.runtime.IProgressMonitor)},
 * 		and gets resolved, to either a project or library entry, using
 *      {@link JavaCore#getResolvedClasspathEntry(IClasspathEntry)}.
 *		It is also possible to register an automatic initializer ({@link ClasspathVariableInitializer}),
 * 	which will be invoked through the extension point "org.eclipse.jdt.core.classpathVariableInitializer".
 * 	After resolution, a classpath variable entry may either correspond to a project or a library entry. </li>
 *
 *  <li> A named classpath container identified by its container path.
 *     A classpath container provides a way to indirectly reference a set of classpath entries through
 *     a classpath entry of kind {@link #CPE_CONTAINER}. Typically, a classpath container can
 *     be used to describe a complex library composed of multiple JARs, projects or classpath variables,
 *     considering also that containers can be mapped differently on each project. Several projects can
 *     reference the same generic container path, but have each of them actually bound to a different
 *     container object.
 *     The container path is a formed by a first ID segment followed with extra segments,
 *     which can be used as additional hints for resolving this container reference. If no container was ever
 *     recorded for this container path onto this project (using {@link JavaCore#setClasspathContainer},
 * 	then a {@link ClasspathContainerInitializer} will be activated if any was registered for this
 * 	container ID onto the extension point "org.eclipse.jdt.core.classpathContainerInitializer".
 * 	A classpath container entry can be resolved explicitly using {@link JavaCore#getClasspathContainer}
 * 	and the resulting container entries can contain any non-container entry. In particular, it may contain variable
 *     entries, which in turn needs to be resolved before being directly used.
 * 	<br> Also note that the container resolution APIs include an IJavaProject argument, so as to allow the same
 * 	container path to be interpreted in different ways for different projects. </li>
 * </ul>
 * The result of {@link IJavaProject#getResolvedClasspath} will have all entries of type
 * {@link #CPE_VARIABLE} and {@link #CPE_CONTAINER} resolved to a set of
 * {@link #CPE_SOURCE}, {@link #CPE_LIBRARY} or {@link #CPE_PROJECT}
 * classpath entries.
 * <p>
 * Any classpath entry other than a source folder (kind {@link #CPE_SOURCE}) can
 * be marked as being exported. Exported entries are automatically contributed to
 * dependent projects, along with the project's default output folder, which is
 * implicitly exported, and any auxiliary output folders specified on source
 * classpath entries. The project's output folder(s) are always listed first,
 * followed by the any exported entries.
 * <p>
 * Classpath entries can be created via methods on {@link JavaCore}.
 * </p>
 *
 * @see JavaCore#newLibraryEntry(org.eclipse.core.runtime.IPath, org.eclipse.core.runtime.IPath, org.eclipse.core.runtime.IPath)
 * @see JavaCore#newProjectEntry(org.eclipse.core.runtime.IPath)
 * @see JavaCore#newSourceEntry(org.eclipse.core.runtime.IPath)
 * @see JavaCore#newVariableEntry(org.eclipse.core.runtime.IPath, org.eclipse.core.runtime.IPath, org.eclipse.core.runtime.IPath)
 * @see JavaCore#newContainerEntry(org.eclipse.core.runtime.IPath)
 * @see ClasspathVariableInitializer
 * @see ClasspathContainerInitializer
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface IClasspathEntry {}
