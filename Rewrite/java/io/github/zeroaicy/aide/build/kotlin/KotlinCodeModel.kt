package io.github.zeroaicy.aide.build.kotlin

import com.aide.codemodel.Highlighter
import com.aide.codemodel.api.FileEntry
import com.aide.codemodel.api.Model
import com.aide.codemodel.api.SyntaxTree
import com.aide.codemodel.api.SyntaxTreeStyles
import com.aide.codemodel.api.abstraction.CodeCompiler
import com.aide.codemodel.api.abstraction.CodeModel
import com.aide.codemodel.api.abstraction.Debugger
import com.aide.codemodel.api.abstraction.Language
import com.aide.codemodel.api.abstraction.Preprocessor
import com.aide.codemodel.language.kotlin.KotlinLexer
import java.io.Reader


/**
 * @author: 罪慾
 * @github: https://github.com/neu233/
 * @mail: 3115093767@qq.com
 * @createTime: 2025/2/6
 */


class KotlinCodeModel(val model: Model) : CodeModel {

    private val kotlinLexer = KotlinLexer()
    private val myLanguage = KotlinLanguage(this)
    private val myHighlighter = Highlighter(kotlinLexer)
    private val kotlinCodeCompiler = KotlinCodeCompiler(model, myLanguage)


    override fun getArchiveVersion(s: String): Long {
        return 0
    }

    override fun update() {
    }

    override fun u7(): Boolean {
        return false
    }

    override fun getExtendFilePatterns(): Array<String> {
        return emptyArray()
    }

    override fun getDefaultFilePatterns(): Array<String> {
        return arrayOf("*.kt", "*.kts")
    }

    override fun closeArchive() {
    }

    override fun getDebugger(): Debugger? {
        return null
    }

    override fun isSupportArchiveFile(): Boolean {
        return false
    }

    override fun getArchiveEntryReader(s: String, s1: String, s2: String): Reader? {
        return null
    }

    override fun getName(): String {
        return "Kotlin"
    }

    override fun processVersion(da: FileEntry?, na: Language?) {
    }

    override fun fillSyntaxTree(
        fileEntry: FileEntry?,
        reader: Reader?,
        map: Map<Language?, SyntaxTreeStyles?>
    ) {
        myHighlighter.highlight(fileEntry, reader, map[myLanguage])
    }

    override fun fillSyntaxTree(
        fileEntry: FileEntry?,
        reader: Reader?,
        map: Map<Language?, SyntaxTree?>,
        b: Boolean
    ) {
        if (map.containsKey(myLanguage)) {
            val syntaxTree = map[myLanguage]
            syntaxTree?.declareContent(syntaxTree.declareNode(0, true, IntArray(0), 0, 0, 1, 1))
            //sa.DW(sa.j6(0, true, new int[0], 0, 0, 1, 1));
        }
    }

    override fun getArchiveEntries(s: String): Array<String> {
        return emptyArray()
    }

    override fun getPreprocessor(): Preprocessor? {
        return null
    }

    override fun getCodeCompiler(): CodeCompiler {
        return this.kotlinCodeCompiler
    }

    override fun getLanguages(): List<Language> {
        val list: MutableList<Language> = ArrayList()
        list.add(myLanguage)
        return list
    }
}