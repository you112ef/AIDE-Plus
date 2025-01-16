import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jdt.internal.formatter.DefaultCodeFormatter;
import org.eclipse.jdt.internal.formatter.DefaultCodeFormatterOptions;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.TextEdit;

import java.io.File;
import java.nio.file.Files;

public class Main {
    public static void main(String[] args) throws Exception {


        String v = "/storage/emulated/0/AppProjects1/.ZeroAicy/git/ecj_formatter/console/src/main/java/org/eclipse/jdt/core/ToolFactory.java";
        v = "/storage/emulated/0/AppProjects1/.ZeroAicy/git/ecj_formatter/console/src/main/java/mtm.java";
        File file = new File(v);

        String inputText = new String(Files.readAllBytes(file.toPath()));
        int indentationLevel = 0;
        String lineSeparator = "\n";
        IDocument doc = new Document(inputText);

        DefaultCodeFormatterOptions options = DefaultCodeFormatterOptions.getEclipseDefaultSettings();
        DefaultCodeFormatter codeFormatter = new DefaultCodeFormatter(options);

        int kind = CodeFormatter.K_COMPILATION_UNIT;
        TextEdit edit = codeFormatter.format(kind, inputText, 0, inputText.length(), indentationLevel, lineSeparator);
        edit.apply(doc);

        System.out.println(doc.get());
        System.out.println(edit);

    }
}

// 5
// 4
// 3
// 2
// 2
