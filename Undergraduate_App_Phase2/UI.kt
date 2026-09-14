//NOTES:
// - ? means it can either contain a thing or be null

@Composable
fun StudentScreen(
    student: Student,
    onAddCourse: (Course) -> Unit,
    onRemoveCourse: (Course) -> Unit
) {

    Column {
        Text("You picked a major. How exciting!")
        Text("Requirements: ${student.major.requirements.size}")
        Text("So far, you've completed these courses: ${student.courses.size}")
    }
}

//NOTE: viewModel.selectMajor is reference to selectMajor

@Composable
fun MajorSelect(
    majors: List<DegreePlan>,
    onMajorSelected: (DegreePlan) -> Unit //takes in degree and returns uit
) {
    Column{
        Text("Choose a major...")

        majors.forEach { major ->
            Button(onClick = { //returns major user clicks
                onMajorSelected(major)
            }){
                Text(major.name)
            }
        }
    }
}

@Composable
fun App() {
    val viewModel: DegreeViewModel = viewModel()
    val student = viewModel.student

//    StudentScreen(
//        student = viewModel.student,
//        onAddCourse = viewModel::addCourse
//    )

    if (student == null) {
        MajorSelect(
            majors = viewModel.availableMajors,
            onMajorSelected = viewModel::selectMajor
        )
    } else {
        StudentScreen(
            student = student,
            onAddCourse = viewModel::addCourse,
            onRemoveCourse = viewModel::removeCourse
        )
    }
}

class Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        setContent{
            App()
        }
    }
}
