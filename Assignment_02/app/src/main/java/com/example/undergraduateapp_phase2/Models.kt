package com.example.undergraduateapp_phase2

import kotlinx.serialization.Serializable


@Serializable
data class DegreePlan(
    val name: String,
    val path: String
)

@Serializable
data class DegreePlanList(
    val plans: List<DegreePlan>
)

@Serializable
data class DegreeDTO(
    val name: String,
    val requirements: List<DegreeReqDTO>
)

@Serializable
data class DegreeReqDTO(
    val type: String,
    val course: Course? = null,
    val courses: List<Course>? = null
)