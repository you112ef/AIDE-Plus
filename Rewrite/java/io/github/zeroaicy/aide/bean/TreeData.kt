package io.github.zeroaicy.aide.bean


/**
 * @author: 罪慾
 * @github: https://github.com/neu233/
 * @mail: 3115093767@qq.com
 * @createTime: 2025/1/30
 */


import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream

data class XmlElement(
    val name: String,
    val attributes: Map<String, String>,
    val children: MutableList<XmlElement> = mutableListOf()
)

class XmlParser {

    fun parse(inputStream: InputStream): XmlElement {
        val parser = Xml.newPullParser()
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
        parser.setInput(inputStream, null)
        return parseElement(parser)
    }

    private fun parseElement(parser: XmlPullParser): XmlElement {
        var eventType = parser.eventType
        while (eventType != XmlPullParser.START_TAG) {
            eventType = parser.next()
        }

        val name = parser.name
        val attributes = mutableMapOf<String, String>()
        for (i in 0 until parser.attributeCount) {
            attributes[parser.getAttributeName(i)] = parser.getAttributeValue(i)
        }

        val element = XmlElement(name, attributes)

        while (true) {
            eventType = parser.next()
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    element.children.add(parseElement(parser))
                }
                XmlPullParser.END_TAG -> {
                    if (parser.name == name) break
                }
                XmlPullParser.END_DOCUMENT -> return element
            }
        }
        return element
    }
}