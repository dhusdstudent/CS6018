import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.undergraduateapp_phase2.Course
import com.example.undergraduateapp_phase2.DegreePlan
import com.example.undergraduateapp_phase2.DegreeRepo
import com.example.undergraduateapp_phase2.DegreeViewModel
import com.example.undergraduateapp_phase2.Factory
import com.example.undergraduateapp_phase2.Student
import com.example.undergraduateapp_phase2.createHttpClient

//NOTES:
// - ? means it can either contain a thing or be null

@Composable
fun StudentScreen(student: Student) {

    Column {
        Text("You picked a major. How exciting!")
        Text("Requirements: ${student.major.requirements.size}")
        Text("So far, you've completed these courses: ${student.courses.size}")
    }
}

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
fun App(factory: Factory) {
    val viewModel: DegreeViewModel = viewModel(
        factory = factory
    )
    val student = viewModel.student

    if (student == null) {
        MajorSelect(
            majors = viewModel.availableMajors,
            onMajorSelected = viewModel::selectMajor
        )
    } else {
        StudentScreen(
            student = student
        )
    }
}

class Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        val client = createHttpClient()
        val repo = DegreeRepo(client)
        val factory = Factory(repo)

        setContent{
            App(factory)
        }
    }
}
