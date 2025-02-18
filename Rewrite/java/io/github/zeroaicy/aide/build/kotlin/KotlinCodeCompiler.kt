package io.github.zeroaicy.aide.build.kotlin

import com.aide.codemodel.api.FileSpace
import com.aide.codemodel.api.FileSpace.Assembly
import com.aide.codemodel.api.Model
import com.aide.codemodel.api.SyntaxTree
import com.aide.codemodel.api.abstraction.CodeCompiler
import com.aide.codemodel.api.abstraction.CodeModel
import com.aide.codemodel.api.abstraction.Language
import com.aide.codemodel.api.collections.OrderedMapOfIntInt
import com.aide.codemodel.api.collections.SetOfInt
import com.aide.common.AppLog
import io.github.zeroaicy.util.reflect.ReflectPie


/**
 * @author: 罪慾
 * @github: https://github.com/neu233/
 * @mail: 3115093767@qq.com
 * @createTime: 2025/2/6
 */


class KotlinCodeCompiler(
    val model: Model,
    val language: Language
) : CodeCompiler {

    private lateinit var mainProjectPath: String

    override fun compile(list: MutableList<SyntaxTree>, p: Boolean) {
        //List<File> files = new ArrayList<>();
        for (syntaxTree in list) {
            if (syntaxTree.language === this.language) {
                try {
                    val pathString = syntaxTree.file.pathString
                    println(pathString)
                    //files.add(new File(pathString));
                } catch (e: Exception) {
                    e.printStackTrace()
                    return
                }
            }
        }





    }

    override fun init(codeModel: CodeModel) {
        if (codeModel !is KotlinCodeModel) {
            return
        }

        val fileSpace = model.fileSpace
        val fileSpaceReflect = ReflectPie.on(fileSpace)
        val assemblyMap = fileSpaceReflect.get<java.util.HashMap<Int, Assembly>>("assemblyMap")
        val assemblyReferences = fileSpaceReflect.get<OrderedMapOfIntInt>("assemblyReferences")
        // OrderedMapOfIntInt允许多个相同的key
        // 应该是 int int 对
        val defaultIterator = assemblyReferences.default_Iterator

        // 被依赖的assemblyId
        val mainProjectAssemblyId = findMainProjectAssemblyId(defaultIterator, assemblyMap)
        if (mainProjectAssemblyId < 0) {
            AppLog.println_e("KotlinCodeCompiler找不到主项目")
            return
        }

        val mainProjectAssembly = assemblyMap[mainProjectAssemblyId]
        this.mainProjectPath = Assembly.Zo(mainProjectAssembly)

        defaultIterator.init()

        // 项目依赖映射 都是 项目路径 不是其内部目录依赖
        val referenceAssemblyHashMap = HashMap<String, MutableSet<Int>>()

        // 遍历所有 SolutionProject的 AssemblyId
        while (defaultIterator.hasMoreElements()) {
            val projectAssemblyId: Int = defaultIterator.nextKey()
            val referencedProjectAssembly: Int = defaultIterator.nextValue()

            // 自己会依赖自己，排除
            if (projectAssemblyId == referencedProjectAssembly) {
                continue
            }

            val projectAssembly = assemblyMap[projectAssemblyId]

            val projectPath = Assembly.Zo(projectAssembly)

            var references = referenceAssemblyHashMap[projectPath]
            if (references == null) {
                references = HashSet()
                referenceAssemblyHashMap[projectPath] = references
            }

            // Assembly referenceProjectAssembly =  assemblyMap.get(projectAssemblyId);
            // String referenceProjectPath = Assembly.Zo(referenceProjectAssembly);
            references.add(projectAssemblyId)
        }




    }


    private fun findMainProjectAssemblyId(
        defaultIterator: OrderedMapOfIntInt.Iterator,
        assemblyMap: HashMap<Int, FileSpace.Assembly>
    ): Int {
        val referencedSet = SetOfInt()
        // 重置
        defaultIterator.init()
        // 遍历
        while (defaultIterator.hasMoreElements()) {
            val key = defaultIterator.nextKey()
            val referenced = defaultIterator.nextValue()
            // 自己会依赖自己，排除
            if (key != referenced
                && !referencedSet.contains(referenced)
            ) {
                referencedSet.put(referenced)
            }
        }
        for (assemblyId in assemblyMap.keys) {
            // int assemblyId = assemblyIdInteger.intValue();
            if (referencedSet.contains(assemblyId)) {
                continue
            }
            AppLog.println_d("主项目AssemblyId: ", assemblyId)
            return assemblyId
        }
        return -1
    }


}