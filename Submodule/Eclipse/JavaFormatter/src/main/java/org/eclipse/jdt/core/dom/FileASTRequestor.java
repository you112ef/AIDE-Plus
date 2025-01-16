/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
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
package org.eclipse.jdt.core.dom;

/**
 * An AST requestor handles ASTs for compilation units passed to
 * {@link ASTParser#createASTs(String[], String[], String[], FileASTRequestor, org.eclipse.core.runtime.IProgressMonitor) ASTParser.createASTs}.
 * <p>
 * {@link FileASTRequestor#acceptAST(String, CompilationUnit) FileASTRequestor.acceptAST} is called for each of the
 * compilation units passed to {@link ASTParser#createASTs(String[], String[], String[], FileASTRequestor, org.eclipse.core.runtime.IProgressMonitor) ASTParser.createASTs}.
 * After all the compilation units have been processed,
 * {@link #acceptBinding(String, IBinding) FileASTRequestor.acceptBinding} is called for each
 * of the binding keys passed to {@link ASTParser#createASTs(String[], String[], String[], FileASTRequestor, org.eclipse.core.runtime.IProgressMonitor) ASTParser.createASTs}.
 * </p>
 * <p>
 * This class is intended to be subclassed by clients.
 * AST requestors are serially reusable, but neither reentrant nor thread-safe.
 * </p>
 *
 * @see ASTParser#createASTs(String[], String[], String[], FileASTRequestor, org.eclipse.core.runtime.IProgressMonitor)
 * @since 3.6
 */
public abstract class FileASTRequestor {}
