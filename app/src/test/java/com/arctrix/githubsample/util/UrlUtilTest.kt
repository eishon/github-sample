package com.arctrix.githubsample.util

import org.junit.Assert.assertEquals
import org.junit.Test

class UrlUtilTest {

    @Test
    fun testEncodeUrl() {
        // given
        val url = "https://test.com/search?q=hello world"
        val expectedEncodedUrl = "https%3A%2F%2Ftest.com%2Fsearch%3Fq%3Dhello+world"

        // when
        val actualEncodedUrl = UrlUtil.encodeUrl(url)

        // then
        assertEquals(expectedEncodedUrl, actualEncodedUrl)
    }
}