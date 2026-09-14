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
    priate set

    var availableMajors by mutableStateOf<List<DegreePlan>>(emptyList())
        private set

    fun selectMajor(major: DegreePlan) {
        viewModelScope.launch {
            val degree = repository.fetchDegree(plan.path)

            student = Student(
                major = major,
                courses = emptyList()
            )
        }
    }
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

