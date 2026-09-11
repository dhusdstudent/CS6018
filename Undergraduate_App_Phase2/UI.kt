//NOTES:
// - ? means it can either contain a thing or be null

@Composable
fun StudentScreen(
    student: Student?,
    onAddCourse: (Course) -> Unit
) {

    if (student == null) {
        Text("You need to pick your major first!")
        return
    }

    Column {
        Text("You picked a major. How exciting!")
        Text("Requirements: ${student.major.requirements.size}")
        Text("So far, you've completed these courses: ${student.courses.size}")
    }
}

//NOTE: viewModel.selectMajor is reference to selectMajor

@Composable
fun MajorSelect(
    majors: List<Pair<String,Degree>>,
    onMajorSelected: (Degree) -> Unit //takes in degree and returns uit
) {
    Column{
        Text("Choose a major...")

        majors.forEach { (name, degree) ->
            Button(onClick = { //returns major user clicks
                onMajorSelected(degree)
            }){
                Text(name)
            }
        }
    }
}

@Composable
fun App() {
    val viewModel: DegreeViewModel = viewModel()

//    StudentScreen(
//        student = viewModel.student,
//        onAddCourse = viewModel::addCourse
//    )

    MajorSelect(
        majors = viewModel.availableMajors,
        onMajorSelected = viewModel::selectMajor
    )
}

class Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        setContent{
            App()
        }
    }
}
