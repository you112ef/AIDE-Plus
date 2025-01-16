/**
 * @Author ZeroAicy
 * @AIDE AIDE+
*/
package org.eclipse.jdt.internal.core;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IJavaModelStatus;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.CoreException;

public class JavaModelStatus implements IStatus, IJavaModelStatus {
	public JavaModelStatus() {

	}

	public JavaModelStatus(int code, Throwable e) {

	}

	public JavaModelStatus(CoreException nestedCoreException) {

	}

	@Override
	public IJavaElement[] getElements() {
		// TODO: Implement this method
		return null;
	}

	@Override
	public IPath getPath() {
		// TODO: Implement this method
		return null;
	}

	@Override
	public String getString() {
		// TODO: Implement this method
		return null;
	}

	@Override
	public boolean isDoesNotExist() {
		// TODO: Implement this method
		return false;
	}

	@Override
	public IStatus[] getChildren() {
		// TODO: Implement this method
		return null;
	}

	@Override
	public int getCode() {
		// TODO: Implement this method
		return 0;
	}

	@Override
	public Throwable getException() {
		// TODO: Implement this method
		return null;
	}

	@Override
	public String getMessage() {
		// TODO: Implement this method
		return null;
	}

	@Override
	public String getPlugin() {
		// TODO: Implement this method
		return null;
	}

	@Override
	public int getSeverity() {
		// TODO: Implement this method
		return 0;
	}

	@Override
	public boolean isMultiStatus() {
		// TODO: Implement this method
		return false;
	}

	@Override
	public boolean isOK() {
		// TODO: Implement this method
		return false;
	}

	@Override
	public boolean matches(int p) {
		// TODO: Implement this method
		return false;
	}

}

