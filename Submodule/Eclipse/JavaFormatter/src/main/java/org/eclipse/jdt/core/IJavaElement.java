/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;

/**
 * Common protocol for all elements provided by the Java model.
 * Java model elements are exposed to clients as handles to the actual underlying element.
 * The Java model may hand out any number of handles for each element. Handles
 * that refer to the same element are guaranteed to be equal, but not necessarily identical.
 * <p>
 * Methods annotated as "handle-only" do not require underlying elements to exist.
 * Methods that require underlying elements to exist throw
 * a <code>JavaModelException</code> when an underlying element is missing.
 * <code>JavaModelException.isDoesNotExist</code> can be used to recognize
 * this common special case.
 * </p>
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface IJavaElement extends IAdaptable {}
