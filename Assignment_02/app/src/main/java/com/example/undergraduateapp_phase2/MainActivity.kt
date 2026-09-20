package com.example.undergraduateapp_phase2


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel


//NOTES:
// - ? means it can either contain a thing or be null

@Composable
fun StudentScreen(
    student: Student,
    onAddCourse: (Course) -> Unit,
    onRemoveCourse: (Course) -> Unit
) {
    Column {
        Text(text = student.major.name)

        CourseEntry(
            onAddCourse = onAddCourse
        )

        CourseList(
            courses = student.courses,
            onRemoveCourse = onRemoveCourse
        )

        RequirementList(
            degree = student.major,
            studentCourses = student.courses
        )
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
            student = student,
            onAddCourse = viewModel::addCourse,
            onRemoveCourse = viewModel::removeCourse
        )
    }
}

class MainActivity : ComponentActivity() {
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

@Composable
fun CourseEntry(
    onAddCourse: (Course) -> Unit
) {
    var department by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }

    Row {
        OutlinedTextField(
            value = department,
            onValueChange = { department = it },
            label = { Text("Department") }
        )

        OutlinedTextField(
            value = number,
            onValueChange = { number = it },
            label = { Text("Course #") }
        )

        Button(
            onClick = {
                if (department.isNotBlank() && number.isNotBlank()) {
                    onAddCourse(
                        Course(
                            department = department.trim().uppercase(),
                            number = number.trim()
                        )
                    )

                    department = ""
                    number = ""
                }
            }
        ) {
            Text("Add")
        }
    }
}

@Composable
fun RequirementList(
    degree: Degree,
    studentCourses: List<Course>
){
    val statuses = reqStatus (
        degree = degree,
        studentCourse = studentCourses
    )

    Column {
        Text("Degree Requirements")

        degree.requirements.forEachIndexed {  index, requirement->
            RequirementRow(
                requirement = requirement,
                satisfied = statuses[index]
            )
        }

        if (statuses.all {it}) {
            Text("Your requirements are satisfied!")
        }
    }
}

@Composable
fun RequirementRow(
    requirement: DegreeRequirement,
    satisfied: Boolean
){
    val description = when (requirement) {
        is CourseRequirement ->
            "${requirement.course.department} ${requirement.course.number}"

        is OneOfRequirement -> requirement.courses
            .joinToString(" or "){
                "${it.department} ${it.number}"
            } }

    Text (
        text = if(satisfied) {
            " √ $description" } else {
                "x $description" }
    )
}

@Composable
fun CourseList(
    courses : List<Course>,
    onRemoveCourse: (Course) -> Unit
) {
    Column {
        Text("Planned Courses")

        courses.forEach { course ->
            Row {
                Text("${course.department} ${course.number}")
                Button(
                    onClick = { onRemoveCourse(course) }
                ) {
                    Text("Remove")
                }
            }
        }
    }
}
