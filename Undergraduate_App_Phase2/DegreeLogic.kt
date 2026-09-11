fun isReqSatisfied(
    requirement: DegreeRequirement,
    studentCourses: List<Course>
): Boolean {
    return when (requirement) {
        is CourseRequirement ->
            requirement.course in studentCourses //does student list contain course?

        is OneOfRequirement ->
            requirement.courses.any { it in studentCourses }
    }
}

fun reqStatus(
    degree: Degree,
    studentCourse: List<Course>
) :List<Boolean> {
    return degree.requirements.map { requirement -> isReqSatisfied(requirement, studentCourse) }
}