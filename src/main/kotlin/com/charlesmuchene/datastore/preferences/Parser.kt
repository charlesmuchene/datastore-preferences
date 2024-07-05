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

/**
 * Parse [content] to a list of [Preference]s.
 *
 * @param content [ByteArray] in protobuf format
 * @return [List] of [Preference]
 *
 * @throws [MalformedContentException] if the [content] is not valid protobuf format
 */
fun parse(content: ByteArray): List<Preference> {
    val data = try {
        DatastorePreferences.PreferenceMap.parseFrom(content)
    } catch (e: InvalidProtocolBufferException) {
        throw MalformedContentException()
    }
    return data.preferencesMap.mapNotNull { (key, value) ->
        when (value.valueCase.number) {
            1 -> BooleanPreference(key = key, value = value.boolean)
            2 -> FloatPreference(key = key, value = value.float)
            3 -> IntPreference(key = key, value = value.integer)
            4 -> LongPreference(key = key, value = value.long)
            5 -> StringPreference(key = key, value = value.string)
            6 -> StringSetPreference(key = key, value = value.stringSet.stringsList.toSet())
            7 -> DoublePreference(key = key, value = value.double)
            8 -> ByteArrayPreference(key = key, value = value.bytesArray.toByteArray())
            else -> null
        }
    }
}