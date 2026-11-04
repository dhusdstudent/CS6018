package com.example.undergraduateapp_phase2

import org.junit.Test
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse

class Test_DegreeLogic {

    @Test
    fun test_courseReqSatisfiedByCourse() {
        val course = Course("CS", "1010")
        val req = CourseRequirement(course)

        val answer = isReqSatisfied(req, listOf(course))

        assertTrue(answer)
    }

    @Test
    fun test_courseReqNotSatisfied() {
        val course = Course("CS", "1010")
        val req = CourseRequirement(course)

        val answer = isReqSatisfied(req, emptyList())

        assertFalse(answer)
    }

    @Test
    fun test_OneOfReqs() {
        val course1 = Course("CS", "1010")
        val course2 = Course("CS", "1111")

        val req = OneOfRequirement(listOf(course1, course2))

        val answer = isReqSatisfied(req, listOf(course2))

        assertTrue(answer)
    }

    @Test
    fun test_OneOfReqsNotSatisfied() {
        val course1 = Course("CS", "1010")
        val course2 = Course("CS", "1111")

        val req = OneOfRequirement(listOf(course1, course2))

        val answer = isReqSatisfied(req, emptyList())

        assertFalse(answer)
    }
}