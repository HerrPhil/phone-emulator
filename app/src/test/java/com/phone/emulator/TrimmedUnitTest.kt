package com.phone.emulator

import com.phone.emulator.ui.transformation.trimmed
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TrimmedUnitTest {

    @Test
    fun check_length_under_ten_is_not_trimmed() {
        val expected = "xyz"
        val actual = trimmed("xyz")
        assertEquals(expected, actual)
    }

    @Test
    fun check_length_ten_is_not_trimmed() {
        val expected = "abcdefghij"
        val actual = trimmed("abcdefghij")
        assertEquals(expected, actual)
    }

    @Test
    fun check_length_over_ten_is_trimmed() {
        val expected = "abcdefghij"
        val actual = trimmed("abcdefghijz")
        assertEquals(expected, actual)
    }
}
