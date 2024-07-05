package com.charlesmuchene.datastore.preferences

import com.charlesmuchene.datastore.preferences.exceptions.MalformedContentException
import java.nio.file.Paths
import kotlin.io.path.readBytes
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class LibraryTest {

    @Test
    fun `malformed content throws exception`() {
        assertFailsWith<MalformedContentException> {
            parse(ByteArray(size = 1))
        }
    }

    @Test
    fun `content is parsed successfully`() {
        val content = Paths.get("src", "test", "resources", "preferences.preferences_pb").readBytes()
        val preferences = parse(content)
        println(preferences)

        val intPreference = preferences[0]
        assertTrue(intPreference is IntPreference)
        assertEquals(expected = 5, actual = intPreference.value)
        assertEquals(expected = "integer", actual = intPreference.key)

        val stringPreference = preferences[1]
        assertTrue(stringPreference is StringPreference)
        assertEquals(expected = "three", actual = stringPreference.value)
        assertEquals(expected = "string", actual = stringPreference.key)

        val booleanPreference = preferences[2]
        assertTrue(booleanPreference is BooleanPreference)
        assertEquals(expected = true, actual = booleanPreference.value)
        assertEquals(expected = "boolean", actual = booleanPreference.key)

        val floatPreference = preferences[3]
        assertTrue(floatPreference is FloatPreference)
        assertEquals(expected = 1.3f, actual = floatPreference.value)
        assertEquals(expected = "float", actual = floatPreference.key)

        val longPreference = preferences[4]
        assertTrue(longPreference is LongPreference)
        assertEquals(expected = 7, actual = longPreference.value)
        assertEquals(expected = "long", actual = longPreference.key)

        val doublePreference = preferences[5]
        assertTrue(doublePreference is DoublePreference)
        assertEquals(expected = 0.7, actual = doublePreference.value)
        assertEquals(expected = "double", actual = doublePreference.key)

        val stringSetPreference = preferences[6]
        assertTrue(stringSetPreference is StringSetPreference)
        assertEquals(expected = setOf("string", "set"), actual = stringSetPreference.value)
        assertEquals(expected = "string-set", actual = stringSetPreference.key)

        val byteArrayPreference = preferences[7]
        assertTrue(byteArrayPreference is ByteArrayPreference)
        val zip = byteArrayOf(0x0A, 0x0A).zip(byteArrayPreference.value)
        assertEquals(expected = 2, actual = zip.size)
        assertTrue { zip.all { it.first == it.second } }
        assertEquals(expected = "byte-array", actual = byteArrayPreference.key)
    }
}
