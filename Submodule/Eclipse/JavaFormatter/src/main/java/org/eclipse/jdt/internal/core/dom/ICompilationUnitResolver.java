/**
 * @Author ZeroAicy
 * @AIDE AIDE+
 */
package org.eclipse.jdt.internal.core.dom;
import java.util.List;
import java.util.Map;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.WorkingCopyOwner;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.internal.compiler.batch.FileSystem;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;

public interface ICompilationUnitResolver{

	// public CompilationUnit toCompilationUnit(ICompilationUnit sourceUnit, boolean needToResolveBindings, Object p2, List<FileSystem.Classpath> classpath, int focalPointPosition, int apiLevel, Map<String, String> safeUnmodifiableMap, int flags, IProgressMonitor monitor);
	public CompilationUnit toCompilationUnit(ICompilationUnit sourceUnit);

}
