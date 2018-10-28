/*
 * Copyright 2018 nwillc@gmail.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted, provided that the above copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.github.nwillc.ksvg

private val NUMBER_REGEX = Regex("[+-]?[0-9]*.?[0-9]+")
private val SEPARATOR_REGEX = Regex("\\s*,?\\s+")
private const val UNITS = "em|ex|px|in|cm|mm|pt|pc"
private val LENGTH_REGEX = Regex("$NUMBER_REGEX($UNITS)?")
private val LENGTH_OR_PERCENTAGE_REGEX = Regex("$NUMBER_REGEX($UNITS|%)?")
private val NUMBER_LIST_REGEX = Regex("($NUMBER_REGEX($SEPARATOR_REGEX)?)+")
private val ID_NAME_REGEX = Regex("[^\\s]+")

/**
 *  An enumeration of attribute types and the how to verify if a value is of this type.
 */
enum class AttributeType {
    /**
     * A length value, a number and optional unit.
     */
    Length() {
        override fun verify(value: String) {
            if (!(value matches LENGTH_REGEX))
                throw IllegalArgumentException("Value ($value) is not a valid length or percentage")
        }
    },
    /**
     * A length or percentage, a number and optional unit or percent sign.
     */
    LengthOrPercentage() {
        override fun verify(value: String) {
            if (!(value matches LENGTH_OR_PERCENTAGE_REGEX))
                throw IllegalArgumentException("Value ($value) is not a valid length or percentage")
        }
    },
    /**
     * A list of numbers separated by white space or commas.
     */
    NumberList() {
        override fun verify(value: String) {
            if (!(value matches NUMBER_LIST_REGEX))
                throw IllegalArgumentException("Value ($value) is not a valid number list")
        }
    },
    /**
     * Any non empty character string without whitespace.
     */
    IdName() {
        override fun verify(value: String) {
            if (!(value matches ID_NAME_REGEX))
                throw IllegalArgumentException("Value ($value) is not a valid id")
        }
    };

    /**
     * Verify a value is of the AttributeType.
     */
    abstract fun verify(value: String)
}