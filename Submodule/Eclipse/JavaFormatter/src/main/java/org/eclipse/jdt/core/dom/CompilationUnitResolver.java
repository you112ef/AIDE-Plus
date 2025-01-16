/**
 * @Author ZeroAicy
 * @AIDE AIDE+
 */
package org.eclipse.jdt.core.dom;


import java.util.Map;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.DefaultErrorHandlingPolicies;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;
import org.eclipse.jdt.internal.core.dom.ICompilationUnitResolver;
import org.eclipse.jdt.internal.core.util.CommentRecorderParser;
public class CompilationUnitResolver implements ICompilationUnitResolver{

	private CommentRecorderParser parser;
	
	@Override
	public CompilationUnit toCompilationUnit(org.eclipse.jdt.internal.compiler.env.ICompilationUnit sourceUnit){
		CompilationResult unitResult = new CompilationResult(sourceUnit, 0, 1, this.options.maxProblemsPerUnit);

		CompilationUnitDeclaration compilationUnitDeclaration = this.parser.parse(sourceUnit, unitResult);
		CompilationUnit unit = convertCompilationUnit(AST.JLS23, compilationUnitDeclaration, sourceUnit, 0, null);
		
		
		return unit;
	}
	public CompilationUnit convertCompilationUnit(
		int level,
		org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration compilationUnitDeclaration,
		org.eclipse.jdt.internal.compiler.env.ICompilationUnit workingCopy,
		int reconcileFlags,
		IProgressMonitor monitor) {
			
		Map<String, String> options = this.options.getMap();
		
		boolean isResolved = false;
		
		ASTConverter converter = new ASTConverter(options, isResolved, monitor);
		AST ast = AST.newAST(level, JavaCore.ENABLED.equals(options.get(JavaCore.COMPILER_PB_ENABLE_PREVIEW_FEATURES)));
		String sourceModeSetting = (String) options.get(JavaCore.COMPILER_SOURCE);
		long sourceLevel = CompilerOptions.versionToJdkLevel(sourceModeSetting);
		if (sourceLevel == 0) {
			// unknown sourceModeSetting
			sourceLevel = ClassFileConstants.JDK1_3;
		}
		ast.scanner.sourceLevel = sourceLevel;
		String compliance = (String) options.get(JavaCore.COMPILER_COMPLIANCE);
		long complianceLevel = CompilerOptions.versionToJdkLevel(compliance);
		if (complianceLevel == 0) {
			// unknown sourceModeSetting
			complianceLevel = sourceLevel;
		}
		ast.scanner.complianceLevel = complianceLevel;
		ast.scanner.previewEnabled = JavaCore.ENABLED.equals(options.get(JavaCore.COMPILER_PB_ENABLE_PREVIEW_FEATURES));
		int savedDefaultNodeFlag = ast.getDefaultNodeFlag();
		ast.setDefaultNodeFlag(ASTNode.ORIGINAL);
		BindingResolver resolver = null;
		resolver = new BindingResolver();
		
		ast.setFlag(reconcileFlags);
		ast.setBindingResolver(resolver);
		converter.setAST(ast);

		CompilationUnit unit = converter.convert(compilationUnitDeclaration, workingCopy.getContents());
		unit.setLineEndTable(compilationUnitDeclaration.compilationResult.getLineSeparatorPositions());
		// unit.setTypeRoot(workingCopy.originalFromClone());
		ast.setDefaultNodeFlag(savedDefaultNodeFlag);
		return unit;
	}
	
	

	public static final int RESOLVE_BINDING = 0x1;
	public static final int PARTIAL = 0x2;
	public static final int STATEMENT_RECOVERY = 0x4;
	public static final int IGNORE_METHOD_BODIES = 0x8;
	public static final int BINDING_RECOVERY = 0x10;
	public static final int INCLUDE_RUNNING_VM_BOOTCLASSPATH = 0x20;

	static CompilationUnitResolver CompilationUnitResolver = new CompilationUnitResolver();

	public static ICompilationUnitResolver getInstance() {
		return CompilationUnitResolver;
	}

	// 通用参数
	private static CompilerOptions compilerOptions;

	private static CompilerOptions getCompilerOptions() {
		if (compilerOptions == null) {
			compilerOptions = new CompilerOptions();
			compilerOptions.parseLiteralExpressionsAsConstants = false;
			compilerOptions.produceDebugAttributes = 
				ClassFileConstants.ATTR_SOURCE
				| ClassFileConstants.ATTR_LINES 
				| ClassFileConstants.ATTR_VARS;
			compilerOptions.produceMethodParameters = true;

			compilerOptions.sourceLevel = ClassFileConstants.JDK23;
			compilerOptions.complianceLevel = ClassFileConstants.JDK23;
			compilerOptions.originalComplianceLevel = ClassFileConstants.JDK23;
			compilerOptions.originalSourceLevel = ClassFileConstants.JDK23;

			compilerOptions.targetJDK = ClassFileConstants.JDK23;

		}
		return compilerOptions;
	}


	protected CompilerOptions options;
	public CompilationUnitResolver(){
		this.options = getCompilerOptions();
		initializeParser();
	}

	protected void initializeParser() {
		this.parser = new CommentRecorderParser(
			new ProblemReporter(
				DefaultErrorHandlingPolicies.proceedWithAllProblems(),
				this.options,
				new DefaultProblemFactory()),
			false);

	}
}
	
