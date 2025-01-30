package io.github.zeroaicy.aide.completion

import android.content.Context
import com.aide.codemodel.api.ClassType
import com.aide.codemodel.api.Model
import com.aide.codemodel.api.Namespace
import com.aide.codemodel.api.SyntaxTree
import com.aide.codemodel.api.collections.MapOfInt
import com.aide.common.AppLog
import com.aide.ui.util.FileSystem
import com.android.SdkConstants
import com.android.SdkConstants.ANDROID_MANIFEST_XML
import com.android.SdkConstants.ANDROID_NS_NAME
import com.android.SdkConstants.ANDROID_URI
import com.android.SdkConstants.APP_PREFIX
import com.android.SdkConstants.ATTR_ATTR
import com.android.SdkConstants.ATTR_CHECKABLE
import com.android.SdkConstants.ATTR_CHECKABLE_BEHAVIOR
import com.android.SdkConstants.ATTR_COLOR
import com.android.SdkConstants.ATTR_DRAWABLE
import com.android.SdkConstants.ATTR_FORMAT
import com.android.SdkConstants.ATTR_FRAGMENT
import com.android.SdkConstants.ATTR_MODULE_NAME
import com.android.SdkConstants.ATTR_NAME
import com.android.SdkConstants.ATTR_ORDER_IN_CATEGORY
import com.android.SdkConstants.ATTR_PARENT
import com.android.SdkConstants.ATTR_PREPROCESSING
import com.android.SdkConstants.ATTR_QUANTITY
import com.android.SdkConstants.ATTR_SHOW_AS_ACTION
import com.android.SdkConstants.ATTR_TITLE
import com.android.SdkConstants.ATTR_TRANSLATABLE
import com.android.SdkConstants.ATTR_TYPE
import com.android.SdkConstants.ATTR_VALUE
import com.android.SdkConstants.ATTR_VISIBLE
import com.android.SdkConstants.AUTO_URI
import com.android.SdkConstants.FD_RES_ANIM
import com.android.SdkConstants.FD_RES_ANIMATOR
import com.android.SdkConstants.FD_RES_DRAWABLE
import com.android.SdkConstants.FD_RES_LAYOUT
import com.android.SdkConstants.FD_RES_MENU
import com.android.SdkConstants.FD_RES_NAVIGATION
import com.android.SdkConstants.FD_RES_VALUES
import com.android.SdkConstants.FD_RES_XML
import com.android.SdkConstants.PreferenceTags.CHECK_BOX_PREFERENCE
import com.android.SdkConstants.PreferenceTags.EDIT_TEXT_PREFERENCE
import com.android.SdkConstants.PreferenceTags.LIST_PREFERENCE
import com.android.SdkConstants.PreferenceTags.MULTI_CHECK_PREFERENCE
import com.android.SdkConstants.PreferenceTags.MULTI_SELECT_LIST_PREFERENCE
import com.android.SdkConstants.PreferenceTags.PREFERENCE_CATEGORY
import com.android.SdkConstants.PreferenceTags.PREFERENCE_SCREEN
import com.android.SdkConstants.PreferenceTags.RINGTONE_PREFERENCE
import com.android.SdkConstants.PreferenceTags.SEEK_BAR_PREFERENCE
import com.android.SdkConstants.PreferenceTags.SWITCH_PREFERENCE
import com.android.SdkConstants.REQUEST_FOCUS
import com.android.SdkConstants.TAG_ADAPTIVE_ICON
import com.android.SdkConstants.TAG_ANIMATED_IMAGE
import com.android.SdkConstants.TAG_ANIMATED_SELECTOR
import com.android.SdkConstants.TAG_ANIMATED_VECTOR
import com.android.SdkConstants.TAG_ANIMATION_LIST
import com.android.SdkConstants.TAG_APPWIDGET_PROVIDER
import com.android.SdkConstants.TAG_ARGUMENT
import com.android.SdkConstants.TAG_ARRAY
import com.android.SdkConstants.TAG_ATTR
import com.android.SdkConstants.TAG_BITMAP
import com.android.SdkConstants.TAG_CLIP_PATH
import com.android.SdkConstants.TAG_COLOR
import com.android.SdkConstants.TAG_DATA
import com.android.SdkConstants.TAG_DECLARE_STYLEABLE
import com.android.SdkConstants.TAG_DEEP_LINK
import com.android.SdkConstants.TAG_DIMEN
import com.android.SdkConstants.TAG_DRAWABLE
import com.android.SdkConstants.TAG_EAT_COMMENT
import com.android.SdkConstants.TAG_ENUM
import com.android.SdkConstants.TAG_FLAG
import com.android.SdkConstants.TAG_FONT
import com.android.SdkConstants.TAG_FONT_FAMILY
import com.android.SdkConstants.TAG_FRAGMENT
import com.android.SdkConstants.TAG_GRADIENT
import com.android.SdkConstants.TAG_GROUP
import com.android.SdkConstants.TAG_HEADER
import com.android.SdkConstants.TAG_IMPORT
import com.android.SdkConstants.TAG_INCLUDE
import com.android.SdkConstants.TAG_INSET
import com.android.SdkConstants.TAG_INTEGER_ARRAY
import com.android.SdkConstants.TAG_ITEM
import com.android.SdkConstants.TAG_LAYER_LIST
import com.android.SdkConstants.TAG_LAYOUT
import com.android.SdkConstants.TAG_LEVEL_LIST
import com.android.SdkConstants.TAG_MASKABLE_ICON
import com.android.SdkConstants.TAG_MENU
import com.android.SdkConstants.TAG_NAVIGATION
import com.android.SdkConstants.TAG_NINE_PATCH
import com.android.SdkConstants.TAG_PATH
import com.android.SdkConstants.TAG_PLURALS
import com.android.SdkConstants.TAG_PUBLIC
import com.android.SdkConstants.TAG_PUBLIC_GROUP
import com.android.SdkConstants.TAG_RESOURCES
import com.android.SdkConstants.TAG_RIPPLE
import com.android.SdkConstants.TAG_ROTATE
import com.android.SdkConstants.TAG_SELECTOR
import com.android.SdkConstants.TAG_SHAPE
import com.android.SdkConstants.TAG_SKIP
import com.android.SdkConstants.TAG_STAGING_PUBLIC_GROUP
import com.android.SdkConstants.TAG_STAGING_PUBLIC_GROUP_FINAL
import com.android.SdkConstants.TAG_STRING
import com.android.SdkConstants.TAG_STRING_ARRAY
import com.android.SdkConstants.TAG_STYLE
import com.android.SdkConstants.TAG_TRANSITION
import com.android.SdkConstants.TAG_VARIABLE
import com.android.SdkConstants.TAG_VECTOR
import com.android.SdkConstants.TOOLS_NS_NAME
import com.android.SdkConstants.TOOLS_URI
import com.android.SdkConstants.UNIT_DP
import com.android.SdkConstants.UNIT_IN
import com.android.SdkConstants.UNIT_MM
import com.android.SdkConstants.UNIT_PT
import com.android.SdkConstants.UNIT_PX
import com.android.SdkConstants.UNIT_SP
import com.android.SdkConstants.VALUE_ALWAYS
import com.android.SdkConstants.VALUE_IF_ROOM
import com.android.SdkConstants.VIEW_FRAGMENT
import com.android.SdkConstants.VIEW_INCLUDE
import com.android.SdkConstants.VIEW_MERGE
import com.android.SdkConstants.VIEW_PKG_PREFIX
import com.android.SdkConstants.VIEW_TAG
import com.android.SdkConstants.WIDGET_PKG_PREFIX
import com.android.aapt.Resources.Attribute.FormatFlags
import com.android.aapt.Resources.Attribute.FormatFlags.BOOLEAN
import com.android.aapt.Resources.Attribute.FormatFlags.COLOR
import com.android.aapt.Resources.Attribute.FormatFlags.DIMENSION
import com.android.aapt.Resources.Attribute.FormatFlags.ENUM
import com.android.aapt.Resources.Attribute.FormatFlags.FLAGS
import com.android.aapt.Resources.Attribute.FormatFlags.INTEGER
import com.android.aapt.Resources.Attribute.FormatFlags.REFERENCE
import com.android.aapt.Resources.Attribute.FormatFlags.STRING
import com.android.aaptcompiler.AaptResourceType
import com.android.aaptcompiler.AaptResourceType.BOOL
import com.android.aaptcompiler.AaptResourceType.DIMEN
import com.android.aaptcompiler.AttributeResource
import com.android.aaptcompiler.ConfigDescription
import com.android.aaptcompiler.ResourceGroup
import com.android.aaptcompiler.ResourceTable
import com.android.aaptcompiler.ResourceTablePackage
import com.android.aaptcompiler.Styleable
import com.android.aaptcompiler.findEntries
import io.github.zeroaicy.aide.aaptcompiler.ApiVersionsUtils
import io.github.zeroaicy.aide.aaptcompiler.MANIFEST_TAG_PREFIX
import io.github.zeroaicy.aide.aaptcompiler.NAMESPACE_AUTO
import io.github.zeroaicy.aide.aaptcompiler.PCK_ANDROID
import io.github.zeroaicy.aide.aaptcompiler.ResourceUtils
import io.github.zeroaicy.aide.aaptcompiler.ResourceUtils.Companion.COMPLETION_MANIFEST_ATTR_RES
import io.github.zeroaicy.aide.aaptcompiler.WidgetTableUtils
import io.github.zeroaicy.aide.aaptcompiler.interfaces.widgets.Widget
import io.github.zeroaicy.aide.aaptcompiler.interfaces.widgets.WidgetTable
import io.github.zeroaicy.aide.aaptcompiler.permissions.Permission
import io.github.zeroaicy.aide.aaptcompiler.utils.attrValue_qualifiedRef
import io.github.zeroaicy.aide.aaptcompiler.utils.attrValue_qualifiedRefWithIncompletePckOrType
import io.github.zeroaicy.aide.aaptcompiler.utils.attrValue_qualifiedRefWithIncompleteType
import io.github.zeroaicy.aide.aaptcompiler.utils.attrValue_unqualifiedRef
import io.github.zeroaicy.readclass.classInfo.JavaViewUtils
import io.github.zeroaicy.util.Log
import java.io.File
import java.io.IOException


/**
 * @author: 罪慾
 * @github: https://github.com/neu233/
 * @mail: 3115093767@qq.com
 * @createTime: 2025/1/18
 */

const val TAG = "XmlCompletionUtils"

var resourceUtil: ResourceUtils? = null
lateinit var apiVersionsUtil: ApiVersionsUtils
lateinit var widgetTableUtil: WidgetTableUtils
lateinit var javaViewUtils: JavaViewUtils


/**
 * 补全xml的tag
 * */
fun completionXmlTag(
    model: Model,
    syntaxTree: SyntaxTree,
    line: Int,
    column: Int
) {
    model.codeCompleterCallback.listStarted()

    // 提示节点
    val parentFileName = getParentName(syntaxTree)
    val parentFile = File(parentFileName)
    val file = syntaxTree.file
    AppLog.d(TAG, "fileName %s ", file.fullNameString)
    when {
        file.fullNameString == ANDROID_MANIFEST_XML -> {
            completionManifestTag(model)
        }

        parentFile.name.startsWith(FD_RES_ANIM) -> {
            codeCompletion(ANIMATION_TAGS, model)
        }

        parentFile.name.startsWith(FD_RES_ANIMATOR) -> {
            codeCompletion(ANIMATOR_TAGS, model)
        }

        parentFile.name.startsWith(FD_RES_DRAWABLE) -> {
            codeCompletion(DRAWABLE_TAGS, model)
        }

        parentFile.name.startsWith(FD_RES_LAYOUT) -> {
            val identifierSpace = model.identifierSpace
            val entitySpace = model.entitySpace
            val rootNamespace = entitySpace.rootNamespace
            val androidIdentifierId = identifierSpace["android"]
            val androidNamespace = rootNamespace.getMemberNamespace(androidIdentifierId)
            val javaViewClasses = javaViewUtils.javaViewClasses
            AppLog.d(TAG, "javaViewClasses size %s ", javaViewClasses.size)
            javaViewClasses.forEach { javaViewClassName ->
                var className = getSimpleName(javaViewClassName)
                val classNameIdentifierId = identifierSpace[className]
                val type = when {
                    javaViewClassName.startsWith(WIDGET_PKG_PREFIX) -> {
                        val widgetIdentifierId = identifierSpace["widget"]
                        val androidWidgetNamespace =
                            androidNamespace.getMemberNamespace(widgetIdentifierId)
                        androidWidgetNamespace.allMemberClassTypes[classNameIdentifierId]
                    }

                    javaViewClassName.startsWith(VIEW_PKG_PREFIX) -> {
                        val viewIdentifierId = identifierSpace["view"]
                        val androidViewNamespace =
                            androidNamespace.getMemberNamespace(viewIdentifierId)
                        androidViewNamespace.allMemberClassTypes[classNameIdentifierId]
                    }

                    else -> {


                        //  计算 javaViewClassName 的 包名(Namespace)
                        val classNamespace = getClassNamespace(javaViewClassName, model)
                        val allMemberClassTypes: MapOfInt<ClassType> =
                            classNamespace.allMemberClassTypes
                        // 查找 类
                        val type = allMemberClassTypes[classNameIdentifierId]
                        // 非 WIDGET_PKG_PREFIX || VIEW_PKG_PREFIX 使用 全名
                        className = javaViewClassName
                        if (type == null) {
                            AppLog.d(
                                TAG,
                                "classNamespace %s ",
                                classNamespace.fullyQualifiedNameString
                            )
                            AppLog.d(TAG, "ClassType %s ", javaViewClassName)
                            AppLog.d(
                                TAG,
                                "allMemberClassTypes size %s ",
                                allMemberClassTypes.size()
                            )
                            AppLog.println_d()
                        }
                        type
                    }
                }

                type?.let {
                    model.codeCompleterCallback.aM(it, className)
                }
            }
            codeCompletion(LAYOUTS_TAGS, model)
            codeCompletion(DATA_BINDING_TAGS, model)
        }

        parentFile.name.startsWith(FD_RES_MENU) -> {
            codeCompletion(mutableListOf("item", "group", "menu"), model)
        }

        parentFile.name.startsWith(FD_RES_NAVIGATION) -> {
            codeCompletion(NAVIGATION_TAGS, model)
        }

        parentFile.name.startsWith(FD_RES_VALUES) -> {
            codeCompletion(RESOURCE_TAGS, model)
        }

        parentFile.name.startsWith(FD_RES_XML) -> {
            codeCompletion(APPWIDGET_TAGS, model)
            codeCompletion(PREFERENCE_TAGS, model)
        }

    }

}

/**
 * 补全attribute下的条目
 * */
fun completionXmlAttribute(
    model: Model,
    syntaxTree: SyntaxTree,
    tag: Int,
    element: Int
) {
    model.codeCompleterCallback.listStarted()

    val parentName = syntaxTree.getIdentifierString(element)
    val rootNamespace = model.entitySpace.rootNamespace
    val rootName = takeRootName(syntaxTree)

    //val memberNamespace = model.entitySpace.rootNamespace.getMemberNamespace(model.identifierSpace.get("android"))

    val allName = mutableMapOf(
        ANDROID_NS_NAME to ANDROID_URI,
        APP_PREFIX to AUTO_URI,
        TOOLS_NS_NAME to TOOLS_URI
    )

    //SyntaxTreeUtils.printNode(syntaxTree,syntaxTree.rootNode,0)

    allName.forEach { (key, value) ->
        val tables = findResourceTables(value)
        if (tables.isEmpty()) {
            AppLog.d(TAG, "No resource tables found for namespace: {}", key)
            return
        }
        val pck = value.substringAfter(SdkConstants.URI_PREFIX)
        val packages = mutableSetOf<ResourceTablePackage>()
        val allAttribute = mutableSetOf<String>()

        for (table in tables) {
            for (tablePackage in table.packages) {
                val styleables = tablePackage.findGroup(AaptResourceType.STYLEABLE)
                    ?: return
                val nodeStyleables =
                    findNodeStyleables(parentName, rootName, styleables)
                for (nodeStyleable in nodeStyleables) {
                    for (ref in nodeStyleable.entries) {
                        AppLog.d(TAG, ref.name.entry, pck)
                    }
                }
            }
            if (value == NAMESPACE_AUTO) {
                packages.addAll(table.packages.filter { it.name.isNotBlank() })
            } else {
                val tablePackage = table.findPackage(pck)
                tablePackage?.also { packages.add(it) }
            }
        }

        for (tablePackage in packages) {
            val styleables = tablePackage.findGroup(AaptResourceType.STYLEABLE)
                ?: return
            val nodeStyleables =
                findNodeStyleables(parentName, rootName, styleables)
            if (nodeStyleables.isEmpty()) {
                return
            }
            for (nodeStyleable in nodeStyleables) {
                for (ref in nodeStyleable.entries) {
                    var vorName = ANDROID_NS_NAME
                    when (pck) {
                        ANDROID_NS_NAME -> {
                            vorName = ANDROID_NS_NAME
                        }

                        AUTO_URI -> {
                            vorName = APP_PREFIX
                        }

                        TOOLS_URI -> {
                            vorName = TOOLS_NS_NAME
                        }
                    }
                    val entry = ref.name.entry
                    if (entry != null) {
                        val attrName = String.format("%s:%s=\"|\"", vorName, entry)
                        //val identifierAttributeSpace = String.format("%s:%s", vorName, entry)
                        if (
                            !allAttribute.contains(attrName) &&
                            !entry.startsWith("_")
                        ) {
                            val xmlType =
                                rootNamespace.getMemberNamespace(model.identifierSpace[entry])
                            /*XmlType(
                            model.fileSpace,
                            model.entitySpace,
                            inputString = entry
                        )*/
                            model.codeCompleterCallback.aM(xmlType, attrName)
                            allAttribute.add(attrName)
                        }
                    }
                }
            }

        }
    }
}

/**
 * 补全 value
 * */
fun completionXmlValue(
    model: Model,
    syntaxTree: SyntaxTree,
    property: Int,
    index: Int
) {
    model.codeCompleterCallback.listStarted()
    /** text 即 ‘android:label’ 前面的  ‘label’ */
    val text = model.identifierSpace.getString(property)

    /** ns 即 ‘android:label’ 前面的  ‘android’*/
    val ns = syntaxTree.getIdentifierString(
        syntaxTree.getChildNodeByLevels(index, 0, 0)
    )

    /** parent 即 <application 中的 application></application> */
    val parent = syntaxTree.getIdentifierString(
        syntaxTree.getChildNodeByLevels(syntaxTree.getParentNode(index), 1, 2)
    )

    /** 已经输入的用来判断是否是以 @ 开头 */
    val prefix = syntaxTree.getLiteralString(
        syntaxTree.getChildNodeByLevels(index, 2, 1)
    )

    val allName = mutableMapOf(
        ANDROID_NS_NAME to ANDROID_URI,
        APP_PREFIX to AUTO_URI,
        TOOLS_NS_NAME to TOOLS_URI
    )

    val parentFileName = getParentName(syntaxTree)
    val parentFile = File(parentFileName)
    val file = syntaxTree.file

    AppLog.d(TAG, "fileName %s ", file.fullNameString)

    if (file.fullNameString == ANDROID_MANIFEST_XML) {
        syntaxTree.completionManifestValue(model, text, ns, parent, property, index)
    }

    val namespace = allName[ns]
    if (namespace != null) {
        val tables = findResourceTables(namespace)
        if (tables.isEmpty()) {
            return
        }

        val pck = namespace.substringAfter(SdkConstants.URI_PREFIX)
        val attr =
            findAttr(tables, namespace, pck, text)
                ?: run {
                    AppLog.d(
                        TAG,
                        "No attribute found with name '{}' in package '{}'", text,
                        if (namespace == NAMESPACE_AUTO) "<auto>" else pck
                    )
                    return
                }

        if (!prefix.startsWith('@')) {
            model.addValuesForAttr(attr, pck, prefix)
            return
        }

        var matcher = attrValue_qualifiedRef.matcher(prefix)
        if (matcher.matches()) {
            val valPck = matcher.group(1)
            val typeStr = matcher.group(3)
            val valType = AaptResourceType.entries.firstOrNull { it.tagName == typeStr } ?: return
            val newPrefix = matcher.group(4) ?: ""
            model.addValues(valType, newPrefix) { it == valPck }
            return
        }

        matcher = attrValue_qualifiedRefWithIncompleteType.matcher(prefix)
        if (matcher.matches()) {
            val valPck = matcher.group(1)!!
            val incompleteType = matcher.group(3) ?: ""
            model.addResourceTypes(valPck, incompleteType)
            return
        }

        matcher = attrValue_qualifiedRefWithIncompletePckOrType.matcher(prefix)
        if (matcher.matches()) {
            val valPck = matcher.group(1)!!
            if (!valPck.contains('.')) {
                model.addResourceTypes("", valPck)
            }
            model.addPackages(valPck)
            return
        }

        matcher = attrValue_unqualifiedRef.matcher(prefix)
        if (matcher.matches()) {
            val typeStr = matcher.group(1)
            val newPrefix = matcher.group(2) ?: ""
            val valType = AaptResourceType.entries.firstOrNull { it.tagName == typeStr } ?: return
            model.addValues(valType, newPrefix)
            return
        }


    }

}


fun getSimpleName(name: String?): String {
    return name?.substringAfterLast('.', name) ?: ""
}

/**
 * 补全清单文件的tag
 * */
fun SyntaxTree.completionManifestValue(
    model: Model,
    text: String,
    ns: String,
    parent: String,
    property: Int,
    index: Int
) {
    if (text == "name" && ns == "android") {
        when (parent) {
            "action" -> {
                var actionsList = listOf<String>()
                val parentParent = getIdentifierString(
                    getChildNode(
                        getChildNode(
                            getChildNode(
                                getParentNode(property), 1
                            ), 2
                        ), 3
                    )
                )
                when (parentParent) {
                    SdkConstants.TAG_INTENT_FILTER -> {
                        actionsList = resourceUtil?.getActivityActions()!!
                    }

                    SdkConstants.TAG_RECEIVER -> {
                        actionsList = resourceUtil?.getBroadcastActions()!!
                    }

                    SdkConstants.TAG_SERVICE -> {
                        actionsList = resourceUtil?.getServiceActions()!!
                    }
                }
                for (action in actionsList) {
                    model.codeCompleterCallback.listElementKeywordFound(action)

                }
            }

            "category" -> {
                resourceUtil?.getCategories()?.forEach { category ->
                    model.codeCompleterCallback.listElementKeywordFound(category)
                }
            }

            "uses-permission" -> {
                Permission.entries.forEach { permission ->
                    model.codeCompleterCallback.listElementKeywordFound(permission.constant)
                }
            }

            "uses-feature" -> {
                resourceUtil?.getFeatures()?.forEach { feature ->
                    model.codeCompleterCallback.listElementKeywordFound(feature)
                }
            }

            "activity", "service", "application", "provider", "receiver" -> {
                val superClassType = when (parent) {
                    "activity" -> "Activity"
                    "service" -> "Service"
                    "application" -> "Application"
                    "provider" -> "BroadcastReceiver"
                    "receiver" -> "ContentProvider"
                    else -> throw IllegalArgumentException("Unexpected parent: $parent")
                }
                val packageName =
                    if (parent in listOf("activity", "service", "application")) "app" else "content"
                val literalString = getLiteralString(
                    getChildNode(
                        getChildNode(index, 2), 1
                    )
                )
                TODO("待收集")
                //addCompletionSuperClassSuper(this, model, literalString, true, packageName, superClassType)
            }
        }
    }

}

/**
 * 补全清单文件的tag
 * */
fun completionManifestTag(model: Model) {
    var completionManifestAttrRes = COMPLETION_MANIFEST_ATTR_RES
    if (completionManifestAttrRes == null) {
        completionManifestAttrRes = resourceUtil?.getManifestAttrTable()
    }
    val styleables = completionManifestAttrRes
        ?.findPackage(PCK_ANDROID)
        ?.findGroup(AaptResourceType.STYLEABLE, null)

    styleables
        ?.findEntries { entryName ->
            return@findEntries entryName.startsWith(MANIFEST_TAG_PREFIX)
        }
        ?.map { transformToTagName(it.name, MANIFEST_TAG_PREFIX) }
        ?.forEach {
            model.codeCompleterCallback.listElementKeywordFound(it)
        }
}


private fun getParentName(syntaxTree: SyntaxTree): String {
    return syntaxTree.file.parentDirectory.fullNameString
}

/**
 * 补全 tag 所需的方法
 */
private fun codeCompletion(strings: MutableList<String>, model: Model) {
    strings.forEach { s ->
        val memberNamespace =
            model.entitySpace.rootNamespace.getMemberNamespace(model.identifierSpace[s])
        model.codeCompleterCallback.aM(memberNamespace, s)
    }
}

/**
 * 获取包名，去除类名
 * */
private fun getClassNamespace(clazzName: String, model: Model): Namespace {
    val namespaceParts = clazzName.split("[.$]".toRegex()).dropLast(1)
    return namespaceParts.fold(model.entitySpace.rootNamespace) { namespace, part ->
        namespace.getMemberNamespace(model.identifierSpace[part]) ?: return namespace
    }
}

/**
 * 将条目名称转换为标签名称
 * 例如: `AndroidManifestUsesPermission` -> `uses-permission`
 */
fun transformToTagName(entryName: String, prefix: String = ""): String {
    val name = StringBuilder()
    var index = prefix.length
    while (index < entryName.length) {
        var c = entryName[index]
        if (c.isUpperCase()) {
            if (index != prefix.length) {
                name.append('-')
            }
            c = c.lowercaseChar()
        }

        name.append(c)
        ++index
    }
    return name.toString()
}

/**
 * 将标签名称转换为条目名称
 * 例如: `uses-permission` -> `AndroidManifestUsesPermission`
 */
fun transformToEntryName(tagName: String, prefix: String = ""): String {
    if (tagName == "manifest") {
        return MANIFEST_TAG_PREFIX
    }

    val name = StringBuilder(prefix)

    var index = 0
    var capitalize = false
    while (index < tagName.length) {
        var c = tagName[index]
        if (c == '-') {
            capitalize = true
            ++index
            continue
        }
        if (index == 0 || capitalize) {
            c = c.uppercaseChar()
            capitalize = false
        }
        name.append(c)
        ++index
    }

    return name.toString()
}

/**
 * 通过nsUri然后后获取对应的资源条目
 * */
fun findResourceTables(nsUri: String?): Set<ResourceTable> {
    if (nsUri.isNullOrBlank()) {
        return emptySet()
    }
    if (nsUri == AUTO_URI) {
        return findAllModuleResourceTables()
    }
    val pck = nsUri.substringAfter(SdkConstants.URI_PREFIX)
    if (pck.isBlank()) {
        AppLog.d(TAG, "Invalid namespace: {}", nsUri)
        AppLog.d(TAG, "Invalid namespace: {}", nsUri)
        return emptySet()
    }
    if (pck == ANDROID_NS_NAME) {
        val platformResTable = ResourceUtils.COMPLETION_FRAMEWORK_RES ?: run {
            return emptySet()
        }
        return setOf(platformResTable)
    }
    val table =
        resourceUtil?.forPackage(pck)
            ?: run {
                AppLog.d(TAG, "Cannot find resource table for package: $pck")
                AppLog.d(TAG, "Cannot find resource table for package: $pck")
                return emptySet()
            }
    return setOf(table)
}

fun findAllModuleResourceTables(): Set<ResourceTable> {
    return ResourceUtils.COMPLETION_MODULE_RES
}

/**
 * 初始化SDK内容
 * */
@Synchronized
fun initAndroidSDK(context: Context) {
    try {
        if (resourceUtil != null) {
            // 已initAndroidSDK
            return
        }
        val now = System.currentTimeMillis()
        val platformDir = getPlatformDir()
        // data,zip 根目录是 data
        val androidSdkDataDir = File(platformDir, "data")

        if (androidSdkDataDir.exists() && androidSdkDataDir.isDirectory) {
            initAndroidSdkData()
            Log.i(TAG, "platformDir exists")
        } else {
            // data,zip 根目录是 data
            FileSystem.unZip(context.assets.open("data.zip"), platformDir.absolutePath, true)
            initAndroidSdkData()
        }
        AppLog.d("解压耗时", (System.currentTimeMillis() - now).toString() + "ms")
    } catch (e: IOException) {
        e.printStackTrace()
        AppLog.e("initAndroidSDK", e.message, e)
    }
}

private fun initAndroidSdkData() {
    resourceUtil = ResourceUtils.getInstance(getPlatformDir())
    apiVersionsUtil = ApiVersionsUtils.getInstance(getPlatformDir())
    widgetTableUtil = WidgetTableUtils.getInstance(getPlatformDir())
    javaViewUtils = JavaViewUtils.getInstance()
}

/**
 * 获取sdk包的路径
 * */
fun getPlatformDir(): File {
    return File(FileSystem.getNoBackupFilesDirPath(), ".aide")
}

/**
 * 在缓存中查找styleables
 * */
fun findNodeStyleables(
    nodeName: String,
    parentName: String? = null,
    styleables: ResourceGroup
): Set<Styleable> {
    val widgets = widgetTableUtil.getWidgetTable() ?: return emptySet()
    // 找到安卓View
    val widget =
        if (nodeName.contains(".")) {
            widgets.getWidget(nodeName)
        } else {
            widgets.findWidgetWithSimpleName(nodeName)
        }
    if (widget != null) {
        // 这是来自 Android SDK 的小部件
        // 我们可以获得它的超类和其他东西
        return findStyleablesForWidget(styleables, widgets, widget, parentName = parentName)
    } else if (nodeName.contains('.')) {
        // 可能是自定义视图或库中的视图
        // 如果开发人员遵循命名约定，则仅提供补全
        // 当且仅当标签名称合格时才必须调用此函数
        return findStyleablesForName(
            styleables,
            nodeName = nodeName,
            parentName = parentName,
            true
        )
    }
    AppLog.d(TAG, "Cannot find styleable entries for tag: null")
    return emptySet()
}

/**
 * 在缓存中查找安卓View的styleables
 * */
fun findStyleablesForWidget(
    styleables: ResourceGroup,
    widgets: WidgetTable,
    widget: Widget,
    parentName: String? = null,
    addFromParent: Boolean = true,
    suffix: String = ""
): Set<Styleable> {
    val result = mutableSetOf<Styleable>()
    // 在资源组中查找安卓View的 <declare-styleable>
    addWidgetStyleable(styleables, widget, result, suffix = suffix)
    // 查找所有超类的样式
    addSuperclassStyleables(styleables, widgets, widget, result, suffix = suffix)
    // 添加布局参数提供的属性
    if (addFromParent && parentName != null) {
        val parentWidget =
            if (parentName.contains(".")) {
                widgets.getWidget(parentName)
            } else {
                widgets.findWidgetWithSimpleName(parentName)
            }
        if (parentWidget != null) {
            result.addAll(
                findStyleablesForWidget(
                    styleables,
                    widgets,
                    parentWidget,
                    parentName,
                    false,
                    "_Layout"
                )
            )
        } else {
            result.addAll(findLayoutParams(styleables, parentName))
        }
    }
    return result
}

/**
 * 通过名字查找安卓View的styleables
 * */
fun findStyleablesForName(
    styleables: ResourceGroup,
    nodeName: String,
    parentName: String? = null,
    addFromParent: Boolean = false,
    suffix: String = ""
): Set<Styleable> {
    val result = mutableSetOf<Styleable>()
    // 样式必须由 View 类的简单名称定义
    var name = nodeName
    if (name.contains('.')) {
        name = name.substringAfterLast('.')
    }
    // 所有视图的通用属性
    addWidgetStyleable(styleables = styleables, widget = "View", result = result)
    // 找到声明的样式
    val entry = findStyleableEntry(styleables, "$name$suffix")
    if (entry != null) {
        result.add(entry)
    }
    // 如果必须添加父级的布局参数，请检查父级然后添加它们
    // 布局参数属性只能从直接父级添加
    if (addFromParent) {
        parentName?.also { result.addAll(findLayoutParams(styleables, parentName)) }
    }
    return result
}

/**
 * 查找安卓View的LayoutParams
 * */
fun findLayoutParams(
    styleables: ResourceGroup,
    parentName: String,
): Set<Styleable> {
    val result = mutableSetOf<Styleable>()
    // 添加所有视图组通用的布局参数以及支持子边距的视图组
    addWidgetStyleable(styleables, "ViewGroup", result, suffix = "_Layout")
    addWidgetStyleable(styleables, "ViewGroup", result, suffix = "_MarginLayout")
    var name = parentName
    if (name.contains('.')) {
        name = name.substringAfterLast('.')
    }
    addWidgetStyleable(styleables, name, result, suffix = "_Layout")
    return result
}

/**
 * 添加安卓View的styleable
 * */
fun addWidgetStyleable(
    styleables: ResourceGroup,
    widget: Widget,
    result: MutableSet<Styleable>,
    suffix: String = ""
) {
    addWidgetStyleable(styleables, widget.simpleName, result, suffix)
}

/**
 * 添加安卓View的styleable
 * */
fun addWidgetStyleable(
    styleables: ResourceGroup,
    widget: String,
    result: MutableSet<Styleable>,
    suffix: String = ""
) {
    val entry = findStyleableEntry(styleables, "${widget}${suffix}")
    if (entry != null) {
        result.add(entry)
    }
}

/**
 * 添加超类实现的styleable
 * */
fun addSuperclassStyleables(
    styleables: ResourceGroup,
    widgets: WidgetTable,
    widget: Widget,
    result: MutableSet<Styleable>,
    suffix: String = ""
) {
    for (superclass in widget.superclasses) {
        // 当超类中遇到 ViewGroup 时，添加边距布局参数
        if ("android.view.ViewGroup" == superclass) {
            addWidgetStyleable(styleables, "ViewGroup", result, suffix = "_MarginLayout")
        }
        val superr = widgets.getWidget(superclass) ?: continue
        addWidgetStyleable(styleables, superr.simpleName, result, suffix = suffix)
    }
}

/**
 * 查找styleables的Entry
 * */
fun findStyleableEntry(styleables: ResourceGroup, name: String): Styleable? {
    val value = styleables.findEntry(name)?.findValue(ConfigDescription())?.value
    if (value !is Styleable) {
        AppLog.d(TAG, "Cannot find styleable for {}", name)
        return null
    }
    return value
}

/**
 * 提取上一级TAG
 * */
fun takeParent(
    syntaxTree: SyntaxTree,
    element: Int
) {
    TODO()
}

/**
 * 提取根tag名字
 * */
fun takeRootName(syntaxTree: SyntaxTree): String {
    val childNode = syntaxTree.getChildNode(syntaxTree.rootNode, 0)
    val childCount = syntaxTree.getChildCount(childNode)
    for (i in 0 until childCount) {
        val childNode2 = syntaxTree.getChildNode(childNode, i)
        if (syntaxTree.isBlockNode(childNode2)) {
            val childNode3 = syntaxTree.getChildNode(childNode2, 0)
            val childCount2 = syntaxTree.getChildCount(childNode3)
            for (i2 in 0 until childCount2) {
                val childNode4 = syntaxTree.getChildNode(childNode3, i2)
                if (217 == syntaxTree.getSyntaxTag(childNode4)) {
                    val childNode5 = syntaxTree.getChildNode(childNode4, 0)
                    if (syntaxTree.isIdentifierNode(childNode5) && syntaxTree.getIdentifierString(
                            childNode5
                        ).isEmpty()
                    ) {
                        return syntaxTree.getIdentifierString(
                            syntaxTree.getChildNode(
                                childNode4,
                                2
                            )
                        )
                    }
                }
            }
        }
    }
    return ""
}

/**
 * 通过指定层级的索引获取子节点
 * */
fun SyntaxTree.getChildNodeByLevels(index: Int, vararg levels: Int): Int {
    var currentNode = index
    for (level in levels) {
        currentNode = this.getChildNode(currentNode, level)
    }
    return currentNode
}

fun Model.addValuesForAttr(
    attr: AttributeResource,
    pck: String,
    prefix: String,
) {
    if (attr.typeMask == FormatFlags.REFERENCE_VALUE) {
        completeReferences(prefix)
    } else {
        // Check for specific attribute formats
        if (attr.hasType(STRING)) {
            addValues(type = AaptResourceType.STRING, prefix = prefix)
        }

        if (attr.hasType(INTEGER)) {
            addValues(type = AaptResourceType.INTEGER, prefix = prefix)
        }

        if (attr.hasType(COLOR)) {
            addValues(type = AaptResourceType.COLOR, prefix = prefix)
        }

        if (attr.hasType(BOOLEAN)) {
            addValues(type = BOOL, prefix = prefix)
        }

        if (attr.hasType(DIMENSION)) {
            if (prefix.isNotBlank() && prefix[0].isDigit()) {
                addConstantDimensionValues(prefix)
            } else addValues(type = DIMEN, prefix = prefix)
        }

        if (attr.hasType(INTEGER)) {
            addValues(type = AaptResourceType.INTEGER, prefix = prefix)
        }

        if (attr.hasType(ENUM) || attr.hasType(FLAGS)) {
            for (symbol in attr.symbols) {
                createEnumOrFlagCompletionItem(
                    pck = pck,
                    name = symbol.symbol.name.entry!!,
                )
            }
        }

        if (attr.hasType(REFERENCE)) {
            completeReferences(prefix)
        }
    }
}

fun AttributeResource.hasType(check: FormatFlags): Boolean {
    return hasType(check.number)
}

private fun AttributeResource.hasType(check: Int): Boolean {
    return this.typeMask and check != 0
}

fun Model.completeReferences(prefix: String) {
    for (value in AaptResourceType.entries) {
        if (value == AaptResourceType.UNKNOWN) {
            continue
        }
        addValues(value, prefix)
    }
}

fun Model.addValues(
    type: AaptResourceType,
    prefix: String,
    checkPck: (String) -> Boolean = { true }
) {
    val allNamespaces: Set<Pair<String, String>> = mutableSetOf(
        ANDROID_NS_NAME to ANDROID_URI,
        APP_PREFIX to AUTO_URI,
        TOOLS_NS_NAME to TOOLS_URI
    )
    val entries =
        allNamespaces
            .flatMap { findResourceTables(it.second) }
            .flatMap { table ->
                table.packages.mapNotNull { pck ->
                    if (!checkPck(pck.name)) {
                        return@mapNotNull null
                    }
                    pck.name to
                            pck.findGroup(type)?.findEntries { entryName ->
                                return@findEntries true
                            }
                }
            }
            .toHashSet()

    entries.forEach { pair ->
        pair.second?.forEach {
            createAttrValueCompletionItem(
                pair.first,
                type.tagName,
                it.name
            )

        }
    }
}

fun Model.addResourceTypes(
    pck: String,
    incompleteType: String,
) {
    AppLog.d(TAG, incompleteType)
    listResTypes().forEach {
        createEnumOrFlagCompletionItem(pck, it)
    }
}

fun Model.addPackages(incompletePck: String) {
    val packages =
        findResourceTables(ANDROID_URI).flatMap {
            it.packages.filter { _ -> true }
        }
    packages.forEach {
        val item = createEnumOrFlagCompletionItem(it.name, it.name)
    }
}


private fun listResTypes(): List<String> = AaptResourceType.entries.map { it.tagName }

private fun Model.createAttrValueCompletionItem(
    pck: String = "",
    type: String,
    name: String,
) {
    val sb = StringBuilder()
    sb.append("@")
    if (pck.isNotBlank() && pck == ANDROID_NS_NAME) {
        sb.append(pck)
        sb.append(":")
    }
    sb.append(type)
    sb.append("/")
    sb.append(name)
    codeCompleterCallback.listElementKeywordFound(sb.toString())
    AppLog.d(TAG, sb.toString())
}

private fun Model.createEnumOrFlagCompletionItem(
    pck: String = "",
    name: String,
) {
    codeCompleterCallback.listElementKeywordFound(name)
    AppLog.d("$pck:$name")
}


private fun Model.addConstantDimensionValues(prefix: String) {
    var i = 0
    while (i < prefix.length && prefix[i].isDigit()) {
        ++i
    }
    val dimen = prefix.substring(0, i)
    for (unit in DIMENSION_UNITS) {
        val value = "${dimen}${unit}"
        createEnumOrFlagCompletionItem(name = value)
    }
}


fun findAttr(
    tables: Set<ResourceTable>,
    namespace: String,
    pck: String,
    attr: String
): AttributeResource? {
    if (namespace != NAMESPACE_AUTO && pck == ANDROID_NS_NAME) {
        // AndroidX dependencies include attribute declarations with the 'android' package
        // Those must not be included when completing values
        val attrEntry =
            ResourceUtils.COMPLETION_FRAMEWORK_RES
                ?.findPackage(ANDROID_NS_NAME)
                ?.findGroup(AaptResourceType.ATTR)
                ?.findEntry(attr)
                ?.findValue(ConfigDescription())
                ?.value
        return if (attrEntry is AttributeResource) attrEntry else null
    }
    return if (namespace == NAMESPACE_AUTO) {
        findAttr(tables.flatMap { it.packages }, attr)
    } else {
        findAttr(tables.mapNotNull { it.findPackage(pck) }, attr)
    }
}

private fun findAttr(
    packages: Collection<ResourceTablePackage>,
    attr: String
): AttributeResource? {
    for (pck in packages) {
        val entry =
            pck.findGroup(AaptResourceType.ATTR)?.findEntry(attr)
                ?.findValue(ConfigDescription())?.value ?: continue
        if (entry is AttributeResource) {
            return entry
        }
    }
    return null
}


/** Tags: Resources */
val RESOURCE_TAGS = mutableListOf(
    TAG_RESOURCES,
    TAG_STRING,
    TAG_ARRAY,
    TAG_STYLE,
    TAG_ITEM,
    TAG_GROUP,
    TAG_STRING_ARRAY,
    TAG_PLURALS,
    TAG_INTEGER_ARRAY,
    TAG_COLOR,
    TAG_DIMEN,
    TAG_DRAWABLE,
    TAG_MENU,
    TAG_ENUM,
    TAG_FLAG,
    TAG_ATTR,
    TAG_DECLARE_STYLEABLE,
    TAG_EAT_COMMENT,
    TAG_SKIP,
    TAG_PUBLIC,
    TAG_PUBLIC_GROUP,
    TAG_STAGING_PUBLIC_GROUP,
    TAG_STAGING_PUBLIC_GROUP_FINAL
)

/** Tags: Adaptive icon */
val ADAPTIVE_ICON_TAGS = mutableListOf(
    TAG_ADAPTIVE_ICON,
    TAG_MASKABLE_ICON
)

/** Font family tag */
val FONT_FAMILY_TAGS = mutableListOf(
    TAG_FONT_FAMILY,
    TAG_FONT
)

/** Tags: XML */
val APPWIDGET_TAGS = mutableListOf(
    TAG_HEADER,
    TAG_APPWIDGET_PROVIDER
)

/** Tags: Layouts */
val LAYOUTS_TAGS = mutableListOf(
    VIEW_TAG,
    VIEW_INCLUDE,
    VIEW_MERGE,
    VIEW_FRAGMENT,
    REQUEST_FOCUS,
    TAG
)

/** Tags: Navigation*/
val NAVIGATION_TAGS = mutableListOf(
    TAG_INCLUDE,
    TAG_DEEP_LINK,
    TAG_NAVIGATION,
    TAG_FRAGMENT,
    TAG_ARGUMENT,
    ATTR_MODULE_NAME
)

/**  Tags: Drawables */
val DRAWABLE_TAGS = mutableListOf(
    TAG_ANIMATION_LIST,
    TAG_ANIMATED_IMAGE,
    TAG_ANIMATED_SELECTOR,
    TAG_ANIMATED_VECTOR,
    TAG_BITMAP,
    TAG_CLIP_PATH,
    TAG_GRADIENT,
    TAG_INSET,
    TAG_LAYER_LIST,
    TAG_NINE_PATCH,
    TAG_PATH,
    TAG_RIPPLE,
    TAG_ROTATE,
    TAG_SHAPE,
    TAG_SELECTOR,
    TAG_TRANSITION,
    TAG_VECTOR,
    TAG_LEVEL_LIST
)

/** Tags: Data-Binding */
val DATA_BINDING_TAGS = mutableListOf(
    TAG_LAYOUT,
    TAG_DATA,
    TAG_VARIABLE,
    TAG_IMPORT
)

/** Attributes: Resources */
val ATTRIBUTE_TAGS = mutableListOf(
    ATTR_ATTR,
    ATTR_NAME,
    ATTR_FRAGMENT,
    ATTR_TYPE,
    ATTR_PARENT,
    ATTR_TRANSLATABLE,
    ATTR_COLOR,
    ATTR_DRAWABLE,
    ATTR_VALUE,
    ATTR_QUANTITY,
    ATTR_FORMAT,
    ATTR_PREPROCESSING
)

/** Tags: anim */
val ANIMATION_TAGS = mutableListOf(
    "translate",
    "scale",
    "rotate",
    "alpha",
    "set"
)

/** Tags: animator */
val ANIMATOR_TAGS = mutableListOf(
    "objectAnimator",
    "animator",
    "propertyValuesHolder",
    "set"
)

/** Menus */
val MENU_ATTRIBUTES = mutableListOf(
    ATTR_CHECKABLE,
    ATTR_CHECKABLE_BEHAVIOR,
    ATTR_ORDER_IN_CATEGORY,
    ATTR_SHOW_AS_ACTION,
    ATTR_TITLE,
    ATTR_VISIBLE,
    VALUE_IF_ROOM,
    VALUE_ALWAYS
)

/** PreferenceTags */
val PREFERENCE_TAGS = mutableListOf(
    CHECK_BOX_PREFERENCE,
    EDIT_TEXT_PREFERENCE,
    LIST_PREFERENCE,
    MULTI_CHECK_PREFERENCE,
    MULTI_SELECT_LIST_PREFERENCE,
    PREFERENCE_CATEGORY,
    PREFERENCE_SCREEN,
    RINGTONE_PREFERENCE,
    SEEK_BAR_PREFERENCE,
    SWITCH_PREFERENCE
)

/** dimensionUnits*/
val DIMENSION_UNITS = mutableListOf(
    UNIT_DP,
    UNIT_SP,
    UNIT_PX,
    UNIT_IN,
    UNIT_MM,
    UNIT_PT
)

