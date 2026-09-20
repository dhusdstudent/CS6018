package com.example.undergraduateapp_phase2

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class DegreeViewModel (
    private val repository: DegreeRepo
) : ViewModel() {

    var student by mutableStateOf<Student?>(null)
        private set

    var availableMajors by mutableStateOf<List<DegreePlan>>(emptyList())
        private set

    init {
        loadTheDegrees()
    }

    private fun loadTheDegrees() {
        viewModelScope.launch {
            availableMajors = repository.fetchDegreePlans().plans
        }
    }

    fun selectMajor(major: DegreePlan) {
        viewModelScope.launch {
            val degree = repository.fetchDegree(major.path)

            student = Student(
                major = degree,
                courses = emptyList()
            )
        }
    }


    fun addCourse(course: Course) {
        val currentStudent = student ?: return

        student = currentStudent.copy(
            courses = currentStudent.courses + course
        )
    }

    fun removeCourse(course: Course) {
        val currentStudent = student ?: return

        student = currentStudent.copy(
            courses = currentStudent.courses - course
        )
    }
}
