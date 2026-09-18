package com.example.undergraduateapp_phase01

import Course
import Degree
import Student
import android.R.attr.onClick
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import isReqSatisfied
import majorReqs
import reqStatus
import setUpCourses

class MainActivity  : ComponentActivity() {
    override fun onCreate(savedInstancesState: Bundle?) {
        super.onCreate(savedInstancesState)
        setContent {
            DegreeApp()
        }
    }
}

@Composable
fun DegreeApp(){
    var selectedMajor by remember { mutableStateOf(0) }
    var selectedCourses by remember {mutableStateOf<List<Course>>(emptyList())}
    val roster = setUpCourses()

    Column(
        modifier = Modifier.fillMaxSize().padding(25.dp),
        verticalArrangement = Arrangement.Center
    ){
        Text ("Pick a major:")
        Spacer(modifier = Modifier.height(16.dp))


        Button(onClick = {
            selectedMajor = 1
        selectedCourses = emptyList()}) {
            Text("Computer Science")
        }

        Button(onClick = {
            selectedMajor = 2
            selectedCourses = emptyList()}) {
            Text("Philosophy")
        }

        Button(onClick = {
            selectedMajor = 3
            selectedCourses = emptyList()}) {
            Text("Sociology")
        }

        Spacer(modifier = Modifier.height(25.dp))

        if (selectedMajor != 0){
            val degree = majorReqs(selectedMajor)
            Text("Course Roster")
            Spacer(modifier = Modifier.height(10.dp))

            roster.forEach { course ->
                val isSelected = course in selectedCourses
                Button(
                    onClick = {
                        selectedCourses =
                            if (isSelected){
                                selectedCourses - course
                            } else {
                                selectedCourses + course
                            }
                    }) {
                    Text(
                        if (isSelected){
                            "√ ${course.department} ${course.number}"
                        } else {
                            "${course.department} ${course.number}"
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(25.dp))
            Text("Your Courses:")

            if (selectedCourses.isEmpty()) {
                Text("You haven't selected any courses")
            } else {
                selectedCourses.forEach { course->
                    Text("${course.department} ${course.number}")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            val graduated = isDegreeComplete(degree, selectedCourses)
            Text(
                if (graduated) {
                    "You're ready to graduate!"
                    } else {
                        "You need to take some more classes before you graduate."
                }
            )
        }
    }
}

fun isDegreeComplete(
    degree: Degree,
    studentCourses: List<Course>
) : Boolean {
    return degree.requirements.all { requirement ->
        isReqSatisfied(requirement, studentCourses)
    }
}