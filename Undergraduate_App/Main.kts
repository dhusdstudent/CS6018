data class Course(
    val department: String,
    val number: Int
)

sealed interface DegreeRequirement

data class Degree(
    val requirements: List<DegreeRequirement>
)

data class CourseRequirement(
    val course: Course
) : DegreeRequirement

data class OneOfRequirement(
    val courses: List<Course>
) : DegreeRequirement

data class Student(
    val major: Degree,
    val courses: List<Course>
)

//NOTE TO SELF: Kotlin calls this 'type inference'
fun setUpCourses() = listOf(
    Course("CS", 1010),
    Course("CS", 1111),
    Course("CS", 1212)
)

fun createDegree_CS(): Degree {
    return Degree(
        requirements = listOf(
            CourseRequirement(
                Course("CS", 1010)
            ),
            CourseRequirement(
                Course("CS", 1111),
            ),
            OneOfRequirement(
                listOf(
                Course("CS", 1212),
                Course("CS", 2222),
                Course("CS", 3333)
                )
            )
        )
    )
}

fun createDegree_SOC(){

}

fun createDegree_PHIL(){

}

fun createError(){

}

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

fun majorReqs(input: Int) {
    return when (input) {
        1 -> createDegree_CS()
        2 -> createDegree_PHIL()
        3 -> createDegree_SOC()
        else -> createError()
    } as Unit
}

fun createStudent(input: Int): Student {
    val you = Student()
}

fun pickYourMajor(){
    print("Enter the number that corresponds to your major...")
    print("1. Computer Science")
    print("2. Philosophy")
    print("3. Sociology")

    val answer = readLine()!!.toInt()
    majorReqs(answer)
}

fun main() {
    val courses = setUpCourses()

}