/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.itsaky.androidide.lsp.xml.providers.completion

import io.github.zeroaicy.aide.aaptcompiler.MANIFEST_TAG_PREFIX

/**
 * Transforms entry name to tag name.
 *
 * For example: `AndroidManifestUsesPermission` -> `uses-permission`
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
 * Transforms tag name to entry name.
 *
 * For example: `uses-permission` -> `AndroidManifestUsesPermission`
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
