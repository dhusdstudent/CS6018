package com.example.undergraduateapp_phase2

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class Test_MajorSelect{

    @get:Rule
    val testRule = createComposeRule()

    @Test
    fun test_majorAppearsOnSelection() {

        val majors = listOf(
            DegreePlan("Computer Science", "cs_json"),
            DegreePlan("Philosophy", "philosophy.json"),
            DegreePlan("Sociology", "sociology.json")
        )

        testRule.setContent {
            MajorSelect(
                majors = majors,
                onMajorSelected= {}
            )
        }

        testRule.onNodeWithText("Choose a major...").assertIsDisplayed()
        testRule.onNodeWithText("Computer Science").assertIsDisplayed()
        testRule.onNodeWithText("Philosophy").assertIsDisplayed()
        testRule.onNodeWithText("Sociology").assertIsDisplayed()
    }

    @Test
    fun test_clickingMajorCallsCallback() {

        val majors = listOf(DegreePlan("Computer Science", "cs.json")
        )

        var selectedMajor: DegreePlan? = null

        testRule.setContent {
            MajorSelect(
                majors = majors,
                onMajorSelected = {
                    selectedMajor = it
                }
            )
        }

        testRule.onNodeWithText("Computer Science").performClick()
        assert(selectedMajor == majors[0])
    }
}