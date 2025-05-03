@file:Suppress("DEPRECATION")

package io.github.zeroaicy.aide.test.kotlinc

import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.common.base.Throwables
import io.github.zeroaicy.aide.test.kotlinc.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.kotlin.build.report.ICReporter
import org.jetbrains.kotlin.build.report.ICReporterBase
import org.jetbrains.kotlin.cli.common.ExitCode
import org.jetbrains.kotlin.cli.common.arguments.K2JVMCompilerArguments
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSourceLocation
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.incremental.makeJvmIncrementally
import org.lsposed.hiddenapibypass.HiddenApiBypass
import java.io.File


class MainActivity : AppCompatActivity() {

    private var mCollector: MessageCollector? = null

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: ArrayAdapter<CharSequence>

    private var dialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            HiddenApiBypass.addHiddenApiExemptions("")
        }

        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        showDialog("提示", "自己授权读写权限")

        init()

        adapter = ArrayAdapter<CharSequence>(this, android.R.layout.simple_list_item_1)


        binding.buildInfo.adapter = adapter


    }

    private fun init() {
        CoroutineScope(Dispatchers.IO).launch {
            assets.open("android.jar").use { input ->
                filesDir.resolve("android.jar").outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            assets.open("core-lambda-stubs.jar").use { input ->
                filesDir.resolve("core-lambda-stubs.jar").outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            assets.open("rt.jar").use { input ->
                filesDir.resolve("rt.jar").outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }

        val textKt = filesDir.resolve("kotlin/Main.kt")
        if (!textKt.exists()) {
            textKt.parentFile?.mkdirs()
            textKt.createNewFile()
            textKt.writeText(
                """
package org.example

fun main() {
    println("Hello World!")
}


""".trimIndent()
            )
        }

        binding.codeKotlin.setText(textKt.readText())

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (menu != null) {
            menu.add(1, 1, 1, "构建").apply {
                setIcon(R.drawable.baseline_build_24)
                setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
                setOnMenuItemClickListener {

                    dialog = ProgressDialog.show(this@MainActivity, "正在编译...", "")

                    val textKt = filesDir.resolve("kotlin/Main.kt")
                    textKt.writeText(binding.codeKotlin.text.toString())

                    val importText = binding.importJar.text.toString().lines()
                    val importJar = importText.map { File(it) }.filter { it.exists() }

                    compileKotlin(
                        mutableListOf(textKt.parentFile!!),
                        importJar.toMutableList(),
                    )

                    dialog?.dismiss()


                    true
                }
            }
            menu.add(1, 1, 1, "格式化").apply {
                setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
                setOnMenuItemClickListener {


                    val codeKotlin = binding.codeKotlin.text.toString()

                    val formatterText = formater(codeKotlin)
                    binding.codeKotlin.setText(formatterText)

                    val textKt = filesDir.resolve("kotlin/Main.kt")
                    textKt.writeText(formatterText)




                    true
                }
            }


        }
        return super.onCreateOptionsMenu(menu)
    }


    private fun compileKotlin(
        classpathLibrary: MutableList<File>,
        javaSourceList: MutableList<File>
    ) {

        mCollector = Collector(adapter)

        adapter.clear()

        adapter.add("开始编译...")


        // Kotlin编译后输出的路径
        val mClassOutput = filesDir.resolve("classes").apply { mkdirs() }
        val kotlinHomeDir = filesDir.resolve("kotlin").apply { mkdirs() }
        val cacheDir = filesDir.resolve("intermediate/kotlin").apply { mkdirs() }



        adapter.add("输出目录${mClassOutput.absolutePath}")

        // 此处是所有依赖的集合
        val fileMutableList = mutableListOf<File>()

        fileMutableList.add(filesDir.resolve("core-lambda-stubs.jar"))
        fileMutableList.add(filesDir.resolve("android.jar"))
        fileMutableList.add(filesDir.resolve("rt.jar"))

        if (classpathLibrary.isNotEmpty()) {
            fileMutableList.addAll(classpathLibrary)
        }

        adapter.add("相关依赖")

        // 转换和添加内容
        val arguments = mutableListOf<String>().apply {
            add(0, "-cp")
            fileMutableList.forEach {
                add(it.absolutePath)
                println(it)
                adapter.add(it.absolutePath)
            }
        }


        // Java文件或目录
        val javaSourceRootsFile = mutableListOf<File>() // 添加数据

        javaSourceRootsFile.addAll(javaSourceList)

        val args: K2JVMCompilerArguments by lazy {
            K2JVMCompilerArguments().apply {
                useFirLT = true
                useFirIC = true
                useFastJarFileSystem = true
                useJavac = false

                noJdk = true
                noReflect = true
                noStdlib = true
                newInference = true

                compileJava = false
                includeRuntime = false
                suppressWarnings = false
                script = false

                moduleName = "AIDE-Plus" // 偷偷加点料（

                classpath =
                    fileMutableList.joinToString(separator = File.pathSeparator) { it.absolutePath }

                destination = mClassOutput.absolutePath //Class输出目录
                kotlinHome = kotlinHomeDir.absolutePath // kotlin文件目录


                /*javaSourceRoots =
                    javaSourceRootsFile.map { it.absolutePath }.toTypedArray() //Java目录*/



                languageVersion = "2.1" //可以自定义，后期扩展
                apiVersion = "2.1" //同上

                jvmTarget = "21" //Java目标输出版本
            }
        }


        try {
            /*
            // 不增量编译
            val compiler = K2JVMCompiler()
            compiler.parseArguments(arguments.toTypedArray<String>(), args)
            compiler.exec(mCollector!!, Services.EMPTY, args)*/


            // 增量编译
            makeJvmIncrementally(
                cacheDir,
                listOf(kotlinHomeDir),  // 传入java目录
                args,
                mCollector!!,
                object : ICReporterBase() {
                    override fun report(
                        message: () -> String,
                        severity: ICReporter.ReportSeverity
                    ) {
                        adapter.add("incremental：$message")

                    }

                    override fun reportCompileIteration(
                        incremental: Boolean,
                        sourceFiles: Collection<File>,
                        exitCode: ExitCode
                    ) {
                        adapter.add("incremental：$incremental")
                        sourceFiles.forEach {
                            adapter.add("sourceFiles：$it")
                        }
                        adapter.add(exitCode.toString())
                    }
                })


        } catch (e: Exception) {
            showDialog(message = Throwables.getStackTraceAsString(e))
        }

        if (mCollector!!.hasErrors()) {
            showDialog(message = "Compilation failed, see logs for more details")
        }

        adapter.add("编译完成，输出目录：$mClassOutput")

    }


    /**
     * 格式化Kotlin代码
     *
     * @param code  这个是格式化的内容
     * @param style 这个是格式化的样式
     * 可以填 "dropbox"，"google"和"kotlinlang"
     * @return
     */
    private fun formater(
        code: String,
        style: String = "--kotlinlang-style"
    ): String {
        return code
/*
        val out = ByteArrayOutputStream()
        val err = ByteArrayOutputStream()

        val main =
            Main(
                ByteArrayInputStream(code.toByteArray(StandardCharsets.UTF_8)),
                PrintStream(out),
                PrintStream(err),
                arrayOf("-")
            )
        val exitCode = main.run()
        if (exitCode != 0) {
            return code
        }

        return out.toString()*/

/*
        val file = createTempFile("file", ".kt").apply { writeText(code) }
        val args = listOf(style, file.toAbsolutePath().toString())
        Main(System.`in`, System.out, System.err, args.toTypedArray()).run()
        val formattedCode = file.readText()
        file.deleteIfExists()
        return formattedCode*/
    }

    private fun showDialog(
        title: String = "出现异常",
        message: String,
    ) {
        MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok, null)
            .create()
            .show()
    }


}


class Collector(
    private val adapter: ArrayAdapter<CharSequence>
) : MessageCollector {
    private val mDiagnostics = mutableListOf<CompilerMessageSourceLocation?>() // 错误列表
    private var mHasErrors = false

    override fun clear() {
        mDiagnostics.clear()
    }

    override fun hasErrors(): Boolean {
        return mHasErrors
    }

    override fun report(
        severity: CompilerMessageSeverity,
        message: String,
        location: CompilerMessageSourceLocation?
    ) {
        println(message)

        if (message.contains("No class roots are found in the JDK path")) {
            // Android does not have JDK so its okay to ignore this error
            return
        }

        mDiagnostics.add(location)

        // 别忘了添加到错误列表内

        // 这里是错误的详细信息，自己改改吧
        when (severity) {
            CompilerMessageSeverity.ERROR -> {
                mHasErrors = true
                if (location != null) {
                    val aa =
                        "出现错误！！！文件路径：${location.path}  行数：${location.line}-${location.lineEnd}  内容：${location.lineContent}  "
                    adapter.add(aa)


                }
            }

            CompilerMessageSeverity.STRONG_WARNING, CompilerMessageSeverity.WARNING -> {
                if (location != null) {
                    val aa =
                        "出现警告！！文件路径：${location.path}  行数：${location.line}-${location.lineEnd}  内容：${location.lineContent}  "
                    adapter.add(aa)


                }
            }

            CompilerMessageSeverity.INFO -> {}
            else -> {}
        }
    }


}