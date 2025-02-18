package io.github.zeroaicy.aide.build.kotlin

import com.aide.codemodel.HighlighterSyntax
import com.aide.codemodel.api.abstraction.CodeAnalyzer
import com.aide.codemodel.api.abstraction.CodeRenderer
import com.aide.codemodel.api.abstraction.FormatOption
import com.aide.codemodel.api.abstraction.Language
import com.aide.codemodel.api.abstraction.SignatureAnalyzer
import com.aide.codemodel.api.abstraction.Syntax
import com.aide.codemodel.api.abstraction.Tools
import com.aide.codemodel.api.abstraction.TypeSystem


/**
 * @author: 罪慾
 * @github: https://github.com/neu233/
 * @mail: 3115093767@qq.com
 * @createTime: 2025/2/6
 */


class KotlinLanguage(codeModel: KotlinCodeModel) : Language {
    // private final KotlinCodeModel codeModel;
    private var kotlinTools: KotlinTools =KotlinTools(codeModel.model)

    override fun shrink() {
    }

    override fun getTypeSystem(): TypeSystem? {
        return null
    }

    override fun getCodeAnalyzer(): CodeAnalyzer? {
        return null
    }

    override fun getSignatureAnalyzer(): SignatureAnalyzer? {
        return null
    }

    override fun getFormatOptionSet(): Set<FormatOption?>? {
        return null
    }

    override fun getCodeRenderer(): CodeRenderer? {
        return null
    }

    override fun getName(): String {
        return "Kotlin"
    }

    override fun isParenChar(c: Char): Boolean {
        return false
    }

    override fun getTools(): Tools {
        return kotlinTools
    }

    override fun getSyntax(): Syntax {
        return HighlighterSyntax()
    }
}
