package com.example.undergraduateapp_phase2

fun DegreeDTO.toDomain(): Degree {
    return Degree(
        name = name,
        requirements = requirements.map { requirement ->
            when (requirement.type) {
                "requiredCourse" -> {
                    CourseRequirement(
                        course = requireNotNull(requirement.course)
                    )
                }

                "oneOf" -> {
                    OneOfRequirement(
                        courses = requireNotNull(requirement.courses)
                    )
                }

                else -> {
                    error("I don't recognize ${requirement.type}")
                }
            }
        }
    )
}