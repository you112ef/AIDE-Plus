package io.github.zeroaicy.aide.utils

object PathUtils {

    fun ensureTrailingSlash(path: String): String {
        return if (path.isNotEmpty() && path.last() != '/') {
            "$path/"
        } else {
            path
        }
    }

    fun splitIgnoringEmpty(input: String, delimiter: Char): Array<String> {
        val result = mutableListOf<String>()
        var start = 0
        while (true) {
            val index = input.indexOf(delimiter, start)
            if (index == -1) break
            if (start != index) {
                result.add(input.substring(start, index))
            }
            start = index + 1
        }
        if (start < input.length) {
            result.add(input.substring(start))
        }
        return result.toTypedArray()
    }


}