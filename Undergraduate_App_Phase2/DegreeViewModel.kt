import javax.lang.model.type.UnionType

class DegreeViewModel (
    private val repository: DegreeRepo
) :ViewModel() {
//
//    private val allMajors = listOf(
//        createDegree_CS(),
//        createDegree_PHIL(),
//        createDegree_SOC()
//    )

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
