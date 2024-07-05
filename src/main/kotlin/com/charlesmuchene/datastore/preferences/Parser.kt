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