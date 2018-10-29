/*
 * Copyright 2018 nwillc@gmail.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby
 * granted, provided that the above copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER
 * IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 * PERFORMANCE OF THIS SOFTWARE.
 */

package com.github.nwillc.ksvg.attributes

import com.github.nwillc.ksvg.elements.Element
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * A property delegate that checks if the value matches a specified AttributeType's criteria.
 */
internal class TypedAttribute(private val type: AttributeType) : ReadWriteProperty<Any?, String?> {
    @Suppress("UNCHECKED_CAST")
    override operator fun getValue(thisRef: Any?, property: KProperty<*>): String? {
        if (thisRef is Element) {
            return thisRef.attributes[property.name]
        }
        return null
    }

    override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String?) {
        if (value != null && thisRef is Element) {
            if (thisRef.validateAttributes)
                type.verify(value)

            thisRef.attributes[property.name] = value
        }
    }
}