package com.example.undergraduateapp_phase2

import org.jetbrains.annotations.TestOnly

@TestOnly
fun test_courseReqSatisfiedByCourse() {
    val course = Course("CS", 1010)

    val req = CourseRequirement(course)

    val answer = isReqSatisfied(
        req, listOf(course)
    )

    assertTrue(answer)
}

@Test
fun test_OneOfReqs(){
    val course1 = Course("CS", "1010")
    val course2 = Course("CS", "1111")

    val req = OneOfRequirement(
        listOf(course1, course2)
    )

    val answer = isReqSatisfied(
        req, listOf(course2)
    )

    assertTrue(answer)
}

@Test
fun test_addingActuallyAdds() {
    val view = DegreeViewModel()

    val degree = createDegree_CS()
    val course = Course("CS", "1010")

    view.selectMajor(degree)
    view.addCourse(course)

    assertTrue(
        view.student?.courses?contains(course) == true
    )
}

@Test
fun test_removingActuallyRemove() {
    val view = DegreeViewModel()

    val degree = createDegree_CS()
    val course = Course("CS", "1010")

    view.selectMajor(degree)
    view.addCourse(course)
    view.removeCourse(course)

    assertFalse(
        view.student?.courses?.contains(course) == true
    )
}

@get::Rule
val testRule = createComposeRule()

@Test
fun test_majorAppearsOnSelection(){
    testRule.setContent {
        App()
    }

    testRule.onNodeWithText("Choose a major...").assertIsDisplayed()
    testRule.onNodeWithText("Computer Science").assertIsDisplayed()
    testRule.onNodeWithText("Philosophy").assertIsDisplayed()
    testRule.onNodeWithText("Sociology").assertIsDisplayed()
}

@Test
fun test_CSOnScreen() {
    testRule.setContent {
        App()
    }

    testRule.onNodeWithText("Computer Science").performClick()
    testRule.onNodeWithText("You picked a major. How exciting!").assertIsDisplayed()
}