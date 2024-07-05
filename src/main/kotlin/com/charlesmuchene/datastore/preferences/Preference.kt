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

sealed interface Preference {
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
}

data class BooleanPreference(val key: String, val value: Boolean) : Preference

data class FloatPreference(val key: String, val value: Float) : Preference

data class IntPreference(val key: String, val value: Int) : Preference

data class LongPreference(val key: String, val value: Long) : Preference

data class StringPreference(val key: String, val value: String) : Preference

data class StringSetPreference(val key: String, val value: Set<String>) : Preference

data class DoublePreference(val key: String, val value: Double) : Preference

class ByteArrayPreference(val key: String, val value: ByteArray) : Preference
