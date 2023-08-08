package com.anxops.bkn

import com.anxops.bkn.data.network.ApiResponse
import com.anxops.bkn.data.repository.Result
import com.anxops.bkn.data.repository.asResult
import com.anxops.bkn.data.repository.onError
import com.anxops.bkn.data.repository.onSuccess
import com.anxops.bkn.data.repository.onSuccessNotNull
import com.anxops.bkn.data.repository.runCatchingResult
import com.anxops.bkn.data.repository.successOrException
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class ResultTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Test
    fun `test successOrException function with Success`() {
        val result: Result<Int> = Result.Success(42)
        val extractedData = result.successOrException { it * 2 }
        Assert.assertEquals(84, extractedData)
    }

    @Test(expected = Exception::class)
    fun `test successOrException function with Error`() {
        val exception = RuntimeException("Test error")
        val result: Result<Int> = Result.Error(exception)
        result.successOrException { it * 2 }
    }

    @Test
    fun `test onSuccess function with Success`() {
        val result: Result<Int> = Result.Success(42)
        var extractedData = 0
        result.onSuccess { extractedData = it }
        Assert.assertEquals(42, extractedData)
    }

    @Test
    fun `test onSuccess function with Error`() {
        val result: Result<Int> = Result.Error()
        var extractedData = 0
        result.onSuccess { extractedData = it }
        Assert.assertEquals(0, extractedData)
    }

    @Test
    fun `test onSuccessNotNull function with Success and non-null data`() {
        val result: Result<String> = Result.Success("Hello")
        var extractedData = ""
        result.onSuccessNotNull { extractedData = it }
        Assert.assertEquals("Hello", extractedData)
    }

    @Test
    fun `test onSuccessNotNull function with Success and null data`() {
        val result: Result<String?> = Result.Success(null)
        var extractedData = ""
        result.onSuccessNotNull { extractedData = it }
        Assert.assertEquals("", extractedData)
    }

    @Test
    fun `test onError function with Error`() {
        val exception = RuntimeException("Test error")
        val result: Result<Int> = Result.Error(exception)
        var capturedException: Throwable? = null
        result.onError { capturedException = it }
        Assert.assertEquals(exception, capturedException)
    }

    @Test
    fun `test onError function with Success`() {
        val result: Result<Int> = Result.Success(42)
        var capturedException: Throwable? = RuntimeException("Not expected")
        result.onError { capturedException = it }
        Assert.assertEquals("Not expected", capturedException?.message)
    }

    @Test
    fun `test runCatchingResult function with success`() = testScope.runTest {
        val extractedData = runCatchingResult { 42 }
        Assert.assertTrue(extractedData is Result.Success)
        Assert.assertEquals(42, (extractedData as Result.Success).data)
    }

    @Test
    fun `test runCatchingResult function with error`() = testScope.runTest {
        val exception = RuntimeException("Test error")
        val extractedData = runCatchingResult { throw exception }
        Assert.assertTrue(extractedData is Result.Error)
        Assert.assertEquals(exception, (extractedData as Result.Error).exception)
    }

    @Test
    fun `test ApiResponse asResult function with Success`() {
        val apiResponse: ApiResponse<Int> = ApiResponse.Success(42)
        val result = apiResponse.asResult()
        Assert.assertTrue(result is Result.Success)
        Assert.assertEquals(42, (result as Result.Success).data)
    }

    @Test
    fun `test ApiResponse asResult function with Error`() {
        val message = "Test error"
        val apiResponse: ApiResponse<Int> = ApiResponse.Error(message)
        val result = apiResponse.asResult()
        Assert.assertTrue(result is Result.Error)
    }
}
