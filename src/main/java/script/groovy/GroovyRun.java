package script.groovy;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.SecureASTCustomizer;
import org.codehaus.groovy.control.customizers.SecureASTCustomizer.ExpressionChecker;
import org.codehaus.groovy.control.customizers.SecureASTCustomizer.StatementChecker;

import groovy.lang.GroovyShell;
import groovy.lang.Script;
import script.execute.ScriptInst;
import script.execute.ScriptInstThread;
import script.execute.result.ScriptError;
import script.execute.result.ScriptErrorType;
import xml.LogHandler;

public class GroovyRun {

	/**
	 * Configuration for compiler, can restrict rights
	 */
	@SuppressWarnings("unused")
	private static CompilerConfiguration getCompilerConfiguration() {
		List<String> importsBlacklist = new ArrayList<String>();
		//ImportCustomizer importCustom = new ImportCustomizer();

		SecureASTCustomizer astCustomizer = new SecureASTCustomizer();
		astCustomizer.setImportsBlacklist(importsBlacklist);

		astCustomizer.addExpressionCheckers(new ExpressionChecker() {
			@Override
			public boolean isAuthorized(Expression expr) {
				if (expr instanceof ClassExpression) {
					ClassExpression classExp = (ClassExpression) expr;

					ClassNode classNode = classExp.getType();
					String name = classNode.getName();
					//System.out.println("####### name: " + name);

					//System.out.println(" classExp: " + classExp);
				}

				ClassNode classNode = expr.getType();
				String packageName = classNode.getPackageName();

				/*if (packageName.startsWith("com.innolist")) {
					System.out.println("FAILED: " + expr.getText());

					return false;
				}*/

				//System.out.println(" expr: " + expr.getClass().getSimpleName() + ", " + expr.getType() + ", " + expr);
				return true;
			}
		});

		astCustomizer.addStatementCheckers(new StatementChecker() {
			@Override
			public boolean isAuthorized(Statement stmt) {
				//System.out.println(" stmt: " + stmt);

				return true;
			}
		});

		CompilerConfiguration conf = new CompilerConfiguration();
		conf.addCompilationCustomizers(astCustomizer);

		return conf;
	}

}
