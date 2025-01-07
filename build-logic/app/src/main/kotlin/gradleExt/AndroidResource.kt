package gradleExt

import com.android.build.api.dsl.AndroidResources
import java.io.File

/**
 * @author: 罪慾
 * @github: https://github.com/neu233/
 * @mail: 3115093767@qq.com
 * @createTime: 2025/1/5
 */





fun AndroidResources.createPublicTxt(
    packageName: String,
    publicXmlFile: File,
    publicTxtFile: File,
) {
    // 创建父目录并确保 publicTxtFile 存在
    publicTxtFile.parentFile?.mkdirs()
    if (publicTxtFile.exists()) {
        publicTxtFile.delete()
    }
    publicTxtFile.createNewFile()

    // 解析 public.xml 文件并将内容写入 public.txt
    val nodes = javax.xml.parsers.DocumentBuilderFactory.newInstance()
        .newDocumentBuilder()
        .parse(publicXmlFile)
        .documentElement
        .getElementsByTagName("public")

    for (i in 0 until nodes.length) {
        val node = nodes.item(i)
        val type = node.attributes.getNamedItem("type").nodeValue
        val name = node.attributes.getNamedItem("name").nodeValue
        val id = node.attributes.getNamedItem("id").nodeValue
        publicTxtFile.appendText("$packageName:$type/$name = $id\n")
    }
    // 添加稳定 ID 参数
    additionalParameters.clear()
    additionalParameters.add("--stable-ids")
    additionalParameters.add(publicTxtFile.path)
}