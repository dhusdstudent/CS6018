package com.example.undergraduateapp_phase2

sealed interface DegreeRequirement

data class Degree(
    val name: String,
    val requirements: List<DegreeRequirement>
)

data class CourseRequirement(
    val course: Course
) : DegreeRequirement

data class OneOfRequirement(
    val courses: List<Course>
) : DegreeRequirement