package com.arctrix.githubsample.util

import com.arctrix.githubsample.util.TextUtil.sanitizeSearchInput
import org.junit.Assert.*
import org.junit.Test

class TextUtilTest {

    @Test
    fun `test null input`() {
        // Given
        val input: String? = null

        // When
        val result = sanitizeSearchInput(input)

        // Then
        val expected = ""
        assertEquals(expected, result)
    }

    @Test
    fun `test empty string input`() {
        // Given
        val input = ""

        // When
        val result = sanitizeSearchInput(input)

        // Then
        val expected = ""
        assertEquals(expected, result)
    }

    @Test
    fun `test input with leading and trailing whitespace`() {
        // Given
        val input = "   username   "

        // When
        val result = sanitizeSearchInput(input)

        // Then
        val expected = "username"
        assertEquals(expected, result)
    }

    @Test
    fun `test input with special characters`() {
        // Given
        val input = "user@name!#2023"

        // When
        val result = sanitizeSearchInput(input)

        // Then
        val expected = "username2023"
        assertEquals(expected, result)
    }

    @Test
    fun `test input with multiple hyphens`() {
        // Given
        val input = "user--name--2023"

        // When
        val result = sanitizeSearchInput(input)

        // Then
        val expected = "user-name-2023"
        assertEquals(expected, result)
    }

    @Test
    fun `test input with leading hyphen`() {
        // Given
        val input = "-username"

        // When
        val result = sanitizeSearchInput(input)

        // Then
        val expected = "username"
        assertEquals(expected, result)
    }

    @Test
    fun `test input with trailing hyphen`() {
        // Given
        val input = "username-"

        // When
        val result = sanitizeSearchInput(input)

        // Then
        val expected = "username"
        assertEquals(expected, result)
    }

    @Test
    fun `test input exceeding max length`() {
        // Given
        val input = "a-very-long-username-that-exceeds-the-maximum-length"

        // When
        val result = sanitizeSearchInput(input)

        // Then
        val expected = "a-very-long-username-that-exceeds-the-m"
        assertEquals(expected, result)
    }

    @Test
    fun `test valid input`() {
        // Given
        val input = "valid-username-123"

        // When
        val result = sanitizeSearchInput(input)

        // Then
        val expected = "valid-username-123"
        assertEquals(expected, result)
    }
}
