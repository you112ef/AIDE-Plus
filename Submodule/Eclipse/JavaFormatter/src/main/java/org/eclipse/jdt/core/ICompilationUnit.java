/**
 * @Author ZeroAicy
 * @AIDE AIDE+
*/

package org.eclipse.jdt.core;
import java.util.Map;





@SuppressWarnings("deprecation")
public interface ICompilationUnit {
	
	
	
	/**
	 * Constant indicating that a reconcile operation should not return an AST.
	 * @since 3.0
	 */
	public static final int NO_AST = 0;

	/**
	 * Constant indicating that a reconcile operation should recompute the problems
	 * even if the source hasn't changed.
	 * @since 3.3
	 */
	public static final int FORCE_PROBLEM_DETECTION = 0x01;

	/**
	 * Constant indicating that a reconcile operation should enable the statements recovery.
	 * @see ASTParser#setStatementsRecovery(boolean)
	 * @since 3.3
	 */
	public static final int ENABLE_STATEMENTS_RECOVERY = 0x02;

	/**
	 * Constant indicating that a reconcile operation should enable the bindings recovery
	 * @see ASTParser#setBindingsRecovery(boolean)
	 * @see IBinding#isRecovered()
	 * @since 3.3
	 */
	public static final int ENABLE_BINDINGS_RECOVERY = 0x04;

	/**
	 * Constant indicating that a reconcile operation could ignore to parse the method bodies.
	 * @see ASTParser#setIgnoreMethodBodies(boolean)
	 * @since 3.5.2
	 */
	public static final int IGNORE_METHOD_BODIES = 0x08;
	
	public Map<String, String> getOptions(boolean p0);

	
}

