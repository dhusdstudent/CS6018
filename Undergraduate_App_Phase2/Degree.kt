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

@Serializable
data class DegreePlan(
    val name: String,
    val path: String
)

@Serializable
data class DegreePlanList(
    val plans: List<DegreePlan>
)