package com.sksamuel.kotest.specs.shouldspec

import io.kotest.core.annotation.Ignored
import io.kotest.core.engine.SpecExecutor
import io.kotest.core.engine.TestEngineListener
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.core.spec.style.scopes.DslState
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.matchers.shouldBe
import kotlin.reflect.KClass

@OptIn(kotlin.time.ExperimentalTime::class)
class ShouldSpecIncompleteUsageTest : FunSpec({

   test("should block without .config should error at runtime") {

      var result: Throwable? = null

      val listener = object : TestEngineListener {
         override fun specFinished(kclass: KClass<out Spec>, t: Throwable?, results: Map<TestCase, TestResult>) {
            result = t
         }
      }

      val executor = SpecExecutor(listener)
      executor.execute(IncompleteShouldSpec::class)
      result!!.message shouldBe "Test 'should incomplete should usage' is incomplete"
      DslState.state = null
   }

})

@Ignored
class IncompleteShouldSpec : ShouldSpec({
   context("foo") {
      should("incomplete should usage")
   }
})
