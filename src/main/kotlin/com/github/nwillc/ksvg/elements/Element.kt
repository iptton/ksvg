/*
 * Copyright 2019 nwillc@gmail.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL
 * WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL
 * THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR
 * CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING
 * FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT,
 * NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION
 * WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 *
 */

package com.github.nwillc.ksvg.elements

import com.github.nwillc.ksvg.attributes.AttributeType
import com.github.nwillc.ksvg.attributes.AttributeProperty
import com.github.nwillc.ksvg.escapeHTML
import java.io.StringWriter

/**
 * Abstract SVG named element with attributes and child elements.
 * @param name The svg tag of the element.
 * @param validation Should attribute and other validations be performed?
 */
abstract class Element(private val name: String, var validation: Boolean) {
    /**
     * A Map of attributes associated with the element.
     */
    val attributes = hashMapOf<String, String?>()

    /**
     * Child Element contained in this Element.
     */
    val children = arrayListOf<Element>()

    /**
     * The id attribute of the Element.
     */
    var id: String? by AttributeProperty(type = AttributeType.IdName)

    /**
     * The CSS class.
     */
    var cssClass: String? by AttributeProperty("class", AttributeType.CssClass)

    /**
     * Raw text body of the Element.
     */
    var body: String = ""

    /**
     * Add a child element.
     */
    protected fun <E : Element> add(element: E, block: E.() -> Unit): E = element.also {
        it.block()
        children.add(it)
    }

    /**
     * Get the attributes specific to a render mode. Allows tags to modify their attributes during rendering
     * based on the rendering mode. Defaults to the basic Element attributes but can be overridden by Elements to return
     * differing attribute based on mode.
     * @param renderMode which mode we are rendering in
     */
    open fun getAttributes(renderMode: SVG.RenderMode): Map<String, String?> = attributes

    /**
     * Render the Element as SVG.
     * @param appendable A Writer to append the SVG to.
     * @param renderMode Should the Elements render for inline SVG or file SVG.
     */
    open fun render(appendable: Appendable, renderMode: SVG.RenderMode) {
        appendable.append("<$name")
        getAttributes(renderMode).entries.forEach { entry ->
            appendable.append(' ')
            appendable.append(entry.key)
            appendable.append("=\"")
            appendable.append(entry.value)
            appendable.append('"')
        }
        if (!hasContent()) {
            appendable.append("/>\n")
        } else {
            appendable.append('>')
            if (hasBody()) {
                appendable.escapeHTML(body)
            } else {
                appendable.append('\n')
            }
            children.forEach {
                it.render(appendable, renderMode)
            }
            appendable.append("</$name>\n")
        }
    }

    /**
     * Has raw text body.
     */
    fun hasBody(): Boolean = body.isNotEmpty()

    /**
     * Has Children.
     */
    fun hasChildren(): Boolean = children.isNotEmpty()

    /**
     * Has any content, i.e. a body and/or children.
     */
    fun hasContent(): Boolean = hasBody() || hasChildren()

    /**
     * Returns the rendered inline SVG as a String.
     */
    override fun toString(): String = StringWriter().use { writer ->
        render(writer, SVG.RenderMode.INLINE)
        writer.toString()
    }
}
