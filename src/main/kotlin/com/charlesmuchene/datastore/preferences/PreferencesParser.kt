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

import com.charlesmuchene.datastore.preferences.exceptions.MalformedContentException
import com.google.protobuf.InvalidProtocolBufferException
import com.google.protobuf.kotlin.toByteStringUtf8

/**
 * Parse [content] to a list of [Preference]s.
 *
 * @param content [ByteArray] in protobuf format
 * @return [List] of [Preference]
 *
 * @throws [MalformedContentException] if the [content] is not valid protobuf format
 */
fun parsePreferences(content: ByteArray): List<Preference> {
    val data = try {
        DatastorePreferences.PreferenceMap.parseFrom(content)
    } catch (e: InvalidProtocolBufferException) {
        throw MalformedContentException(cause = e)
    }

    return data.preferencesMap.mapNotNull { (key, value) ->
        when (value.valueCase.number) {
            1 -> BooleanPreference(key = key, value = value.boolean.toString())
            2 -> FloatPreference(key = key, value = value.float.toString())
            3 -> IntPreference(key = key, value = value.integer.toString())
            4 -> LongPreference(key = key, value = value.long.toString())
            5 -> StringPreference(key = key, value = value.string)
            6 -> StringSetPreference(key = key, entries = value.stringSet.stringsList.toSet())
            7 -> DoublePreference(key = key, value = value.double.toString())
            8 -> ByteArrayPreference(key = key, content = value.bytesArray.toByteArray())
            else -> null
        }
    }
}

/**
 * Encode [preferences].
 *
 * @param preferences A [List] of [Preference]s to encode.
 * @return [ByteArray]
 */
fun encodePreferences(preferences: List<Preference>): ByteArray = preferenceMap {
    with(this.preferences) {
        for (preference in preferences) {
            val value = when (preference) {
                is BooleanPreference -> value { boolean = preference.value.toBooleanStrict() }
                is ByteArrayPreference -> value { bytesArray = preference.value.toByteStringUtf8() }
                is DoublePreference -> value { double = preference.value.toDouble() }
                is FloatPreference -> value { float = preference.value.toFloat() }
                is IntPreference -> value { integer = preference.value.toInt() }
                is LongPreference -> value { long = preference.value.toLong() }
                is StringPreference -> value { string = preference.value.toString() }
                is StringSetPreference -> value {
                    stringSet = stringSet { }
                }
            }
            put(key = preference.key, value = value)
        }
    }
}.toByteArray()