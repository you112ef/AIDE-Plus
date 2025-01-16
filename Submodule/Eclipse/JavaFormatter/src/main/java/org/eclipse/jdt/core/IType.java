/*******************************************************************************
 * Copyright (c) 2000, 2021 IBM Corporation and others.
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
 *     IBM Corporation - added J2SE 1.5 support
 *******************************************************************************/
package org.eclipse.jdt.core;



/**
 * Represents either a source type in a compilation unit (either a top-level
 * type, a member type, a local type, an anonymous type or a lambda expression)
 * or a binary type in a class file. Enumeration classes and annotation
 * types are subkinds of classes and interfaces, respectively.
 * <p>
 * Note that the element name of an anonymous source type and lambda expressions
 * is always empty. Types representing lambda expressions are pseudo-elements
 * and not included in the children of their parent. Lambda expressions are created
 * as the result of a <code>ICodeAssist.codeSelect(...)</code>. For more information
 * on such pseudo-elements, see <code>ILocalVariable</code>.
 * </p><p>
 * If a binary type cannot be parsed, its structure remains unknown.
 * Use <code>IJavaElement.isStructureKnown</code> to determine whether this
 * is the case.
 * </p>
 * <p>
 * The children are of type <code>IMember</code>, which includes <code>IField</code>,
 * <code>IMethod</code>, <code>IInitializer</code> and <code>IType</code>.
 * The children are listed in the order in which they appear in the source or class file.
 * </p>
 * <p>
 * Caveat: The {@link #getChildren() children} of a {@link #isBinary() binary} type include
 * nested types. However, the {@link #getParent() parent} of such a nested binary type is
 * <em>not</em> the enclosing type, but that nested type's {@link IClassFile}!
 * </p>
 *
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface IType extends IMember {}
