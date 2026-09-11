import javax.lang.model.type.UnionType

class DegreeViewModel : ViewModel() {
    val allMajors = listOf(
        "Computer Science" to createDegree_CS(),
        "Philosophy" to createDegree_PHIL(),
        "Sociology" to createDegree_SOC()
    )

    var student by mutableStateOf<Student?>(null)
        private set

    fun selectMajor(major: Degree) {
        student = Student(
            major = major,
            courses = emptyList()
        )
    }

    fun addCourse(course: Course) {
        val currentStudent = student ?: return

        student = currentStudent.copy(
            courses = currentStudent.courses + course
        )
    }

    fun removeCourse(course: Course){
        val currentStudent = student ?: return

        student = currentStudent.copy(
            courses = currentStudent.courses - course
        )
    }
}
