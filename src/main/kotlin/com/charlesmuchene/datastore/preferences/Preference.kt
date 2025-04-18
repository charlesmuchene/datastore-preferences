/*
 * Copyright (c) 2024 Charles Muchene
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.charlesmuchene.datastore.preferences

/**
 * A preference.
 *
 * The [value] is treated as a [String] since all use-cases accept a [String] to perform validation.
 */
sealed interface Preference {
    val key: String
    val value: String

    val text: String
        get() =
            when (this) {
                is BooleanPreference -> "Boolean"
                is FloatPreference -> "Float"
                is IntPreference -> "Integer"
                is LongPreference -> "Long"
                is StringPreference -> "String"
                is StringSetPreference -> "String Set"
                is DoublePreference -> "Double"
                is ByteArrayPreference -> "Byte Array"
            }

    fun toPair() = key to value
}

data class BooleanPreference(override val key: String, override val value: String) : Preference

data class FloatPreference(override val key: String, override val value: String) : Preference

data class IntPreference(override val key: String, override val value: String) : Preference

data class LongPreference(override val key: String, override val value: String) : Preference

data class StringPreference(override val key: String, override val value: String) : Preference

data class StringSetPreference(override val key: String, val entries: Set<String>) : Preference {
    override val value: String = entries.joinToString()

    override fun toString(): String = "StringSetPreference(key=$key, value=$value)"
}

data class DoublePreference(override val key: String, override val value: String) : Preference

class ByteArrayPreference(
    override val key: String,
    @Suppress("MemberVisibilityCanBePrivate") val content: ByteArray
) : Preference {
    @OptIn(ExperimentalStdlibApi::class)
    override val value: String get() = content.toHexString()

    override fun toString(): String = "ByteArrayPreference(key=$key, value=$value)"
}
