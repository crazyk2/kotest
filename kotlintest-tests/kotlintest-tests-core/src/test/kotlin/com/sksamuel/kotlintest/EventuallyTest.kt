package com.sksamuel.kotlintest

import io.kotlintest.eventually
import io.kotlintest.matchers.string.shouldEndWith
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.WordSpec
import java.io.FileNotFoundException
import java.io.IOException
import java.time.Duration

class EventuallyTest : WordSpec() {

  init {
    "eventually" should {
      "pass working tests" {
        eventually(Duration.ofDays(5)) {
          System.currentTimeMillis()
        }
      }
      "pass tests that completed within the time allowed"  {
        val end = System.currentTimeMillis() + 2000
        eventually(Duration.ofSeconds(3)) {
          if (System.currentTimeMillis() < end)
            throw RuntimeException("foo")
        }
      }
      "fail tests that do not complete within the time allowed" {
        shouldThrow<AssertionError> {
          eventually(Duration.ofSeconds(2)) {
            throw RuntimeException("foo")
          }
        }
      }
      "return the result computed inside" {
        val result = eventually(Duration.ofSeconds(2)) {
          1
        }
        result shouldBe 1
      }
      "pass tests that completed within the time allowed, custom exception"  {
        val end = System.currentTimeMillis() + 2000
        eventually(Duration.ofDays(5), AssertionError::class.java) {
          if (System.currentTimeMillis() < end)
            assert(false)
        }
      }
      "fail tests throw unexpected exception type"  {
        shouldThrow<KotlinNullPointerException> {
          eventually(Duration.ofSeconds(2), IOException::class.java) {
            (null as String?)!!.length
          }
        }
      }
      "pass tests that throws FileNotFoundException for some time"  {
        val end = System.currentTimeMillis() + 2000
        eventually(Duration.ofDays(5)) {
          if (System.currentTimeMillis() < end)
            throw FileNotFoundException("foo")
        }
      }
      "fail tests that fail a predicate" {
        shouldThrow<AssertionError> {
          eventually(Duration.ofSeconds(2), { it == 2 }) {
            1
          }
        }
      }
      "pass tests that pass a predicate" {
        val result = eventually(Duration.ofSeconds(2), { it == 1 }) {
          1
        }
        result shouldBe 1
      }
      "display the underlying assertion failure" {
        shouldThrow<AssertionError> {
          eventually(Duration.ofMillis(10), AssertionError::class.java) {
            1 shouldBe 2
          }
        }.message.shouldEndWith("; underlying cause was expected: 2 but was: 1")
      }
    }
  }
}
