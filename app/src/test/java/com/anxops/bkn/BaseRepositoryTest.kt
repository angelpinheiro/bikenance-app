package com.anxops.bkn

import com.anxops.bkn.data.repository.BaseRepository
import com.anxops.bkn.data.repository.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class BaseRepositoryTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Test
    fun `test result function with success`() = testScope.runTest {
        val expectedResult = Result.Success(42)

        val block: suspend () -> Int = {
            // Simulate some async work
            delay(100)
            42
        }

        val repository = BaseRepository(testDispatcher)
        val result = repository.result(block)

        Assert.assertEquals(expectedResult, result)
    }

    @Test
    fun `test result function with error`() = testScope.runTest {
        val expectedException = RuntimeException("Test error")

        val block: suspend () -> Int = {
            // Simulate some async work
            delay(100)
            throw expectedException
        }

        val repository = BaseRepository(testDispatcher)
        val result = repository.result(block)

        Assert.assertEquals(expectedException, (result as Result.Error).exception)
    }

}