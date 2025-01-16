/**
 * @Author ZeroAicy
 * @AIDE AIDE+
*/
package org.eclipse.jdt.internal.core.util;

import java.util.HashSet;
import java.util.Iterator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.compiler.util.SuffixConstants;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.jface.text.BadLocationException;

public class Util{

	private static char[][] JAVA_LIKE_EXTENSIONS;

	public static void log(Throwable e, String message) {
		
	}
	/**
	 * Finds the first line separator used by the given text.
	 *
	 * @return <code>"\n"</code> or <code>"\r"</code> or  <code>"\r\n"</code>,
	 *			or <code>null</code> if none found
	 */
	public static String findLineSeparator(char[] text) {
		// find the first line separator
		int length = text.length;
		if (length > 0) {
			char nextChar = text[0];
			for (int i = 0; i < length; i++) {
				char currentChar = nextChar;
				nextChar = i < length-1 ? text[i+1] : ' ';
				switch (currentChar) {
					case '\n': return "\n"; //$NON-NLS-1$
					case '\r': return nextChar == '\n' ? "\r\n" : "\r"; //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
		// not found
		return null;
	}
	/**
	* Apply the given edit on the given string and return the updated string.
	 * Return the given string if anything wrong happen while applying the edit.
	 *
	 * @param original the given string
	* @param edit the given edit
		*
		* @return the updated string
	*/
	public final static String editedString(String original, TextEdit edit) {
	if (edit == null) {
	 return original;
	 }
	 SimpleDocument document = new SimpleDocument(original);
	try {
		edit.apply(document, TextEdit.NONE);
		return document.get();
		} catch (MalformedTreeException | BadLocationException e) {}
		return original;
	}
				
	/**
	 * Returns true if the given name ends with one of the known java like extension.
	 * (implementation is not creating extra strings)
	 */
	public final static boolean isJavaLikeFileName(String name) {
		if (name == null) return false;
		return indexOfJavaLikeExtension(name) != -1;
	}

	/**
	 * Returns true if the given name ends with one of the known java like extension.
	 * (implementation is not creating extra strings)
	 */
	public final static boolean isJavaLikeFileName(char[] fileName) {
		if (fileName == null) return false;
		int fileNameLength = fileName.length;
		char[][] javaLikeExtensions = getJavaLikeExtensions();
		extensions: for (char[] extension : javaLikeExtensions) {
			int extensionLength = extension.length;
			int extensionStart = fileNameLength - extensionLength;
			if (extensionStart-1 < 0) continue;
			if (fileName[extensionStart-1] != '.') continue;
			for (int j = 0; j < extensionLength; j++) {
				if (fileName[extensionStart + j] != extension[j])
					continue extensions;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Returns the registered Java like extensions.
	 */
	public static char[][] getJavaLikeExtensions() {
		if (JAVA_LIKE_EXTENSIONS == null) {
			IContentType javaContentType = Platform.getContentTypeManager().getContentType(JavaCore.JAVA_SOURCE_CONTENT_TYPE);
			HashSet fileExtensions = new HashSet();
			// content types derived from java content type should be included (https://bugs.eclipse.org/bugs/show_bug.cgi?id=121715)
			IContentType[] contentTypes = Platform.getContentTypeManager().getAllContentTypes();
			for (IContentType contentType : contentTypes) {
				if (contentType.isKindOf(javaContentType)) { // note that javaContentType.isKindOf(javaContentType) == true
					String[] fileExtension = contentType.getFileSpecs(IContentType.FILE_EXTENSION_SPEC);
					for (String extension : fileExtension) {
						fileExtensions.add(extension);
					}
				}
			}
			int length = fileExtensions.size();
			// note that file extensions contains "java" as it is defined in JDT Core's plugin.xml
			char[][] extensions = new char[length][];
			extensions[0] = SuffixConstants.EXTENSION_java.toCharArray(); // ensure that "java" is first
			int index = 1;
			Iterator iterator = fileExtensions.iterator();
			while (iterator.hasNext()) {
				String fileExtension = (String) iterator.next();
				if (SuffixConstants.EXTENSION_java.equals(fileExtension))
					continue;
				extensions[index++] = fileExtension.toCharArray();
			}
			JAVA_LIKE_EXTENSIONS = extensions;
		}
		return JAVA_LIKE_EXTENSIONS;
	}
	
	/*
	 * Returns the index of the Java like extension of the given file name
	 * or -1 if it doesn't end with a known Java like extension.
	 * Note this is the index of the '.' even if it is not considered part of the extension.
	 */
	public static int indexOfJavaLikeExtension(String fileName) {
		int fileNameLength = fileName.length();
		char[][] javaLikeExtensions = getJavaLikeExtensions();
		extensions: for (char[] extension : javaLikeExtensions) {
			int extensionLength = extension.length;
			int extensionStart = fileNameLength - extensionLength;
			int dotIndex = extensionStart - 1;
			if (dotIndex < 0) continue;
			if (fileName.charAt(dotIndex) != '.') continue;
			for (int j = 0; j < extensionLength; j++) {
				if (fileName.charAt(extensionStart + j) != extension[j])
					continue extensions;
			}
			return dotIndex;
		}
		return -1;
	}
}
