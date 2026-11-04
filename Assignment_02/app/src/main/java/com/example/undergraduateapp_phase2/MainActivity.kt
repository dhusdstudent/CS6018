package com.example.undergraduateapp_phase2


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel


//NOTES:
// - ? means it can either contain a thing or be null

@Composable
fun App(factory: Factory) {
    val viewModel: DegreeViewModel = viewModel(factory = factory)
    val student = viewModel.student

    MaterialTheme(colorScheme = PlannerColors) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Cream
        ) {
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
    }
}

@Composable
fun StudentScreen(
    student: Student,
    onAddCourse: (Course) -> Unit,
    onRemoveCourse: (Course) -> Unit
) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Text(
            "Degree Planner",
            color = Sage,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            student.major.name,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = Navy
        )

        Text(
            "Track your courses and degree progress.",
            color = GrayText
        )

        ProgressCard(
            degree = student.major,
            studentCourses = student.courses
        )

        CourseEntry(onAddCourse)

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
    onMajorSelected: (DegreePlan) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Spacer(Modifier.height(24.dp))

        Text(
            "Degree Planner",
            color = Sage,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            "Plan your degree path!",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Navy
        )

        Text(
            "Choose your major to start with your degree requirements",
            color = GrayText,
            fontSize = 15.sp
        )

        Spacer (Modifier.height(12.dp))

        Text(
            "Available majors:",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Navy
        )

        if (majors.isEmpty()){
            CircularProgressIndicator(
                color = Sage,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Text(
                "Loading available degree plans...",
                color = GrayText
            )
        } else {
            majors.forEach { major ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onMajorSelected(major)},
                    shape = RoundedCornerShape(15.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Row (
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(
                                major.name,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Navy
                            )

                            Text (
                                "View degree plan",
                                fontSize = 12.sp,
                                color = GrayText
                            )
                        }

                        Text(
                            "-->",
                            fontSize = 25.sp,
                            color = Sage
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProgressCard(
    degree: Degree,
    studentCourses: List<Course>
) {
    val statuses = reqStatus(degree, studentCourses)
    val completed = statuses.count {it}
    val total = statuses.size

    val progress = if (total == 0) {
        0f
    } else {
        completed.toFloat() / total
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Navy
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Degree progress",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                "$completed / $total reqs completed",
                color = Color.White,
                fontSize =15.sp
            )

            LinearProgressIndicator(
                progress = {progress},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp),
                color = GrayText,
                trackColor = Color.White.copy(alpha =0.2f)
            )
        }
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

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape (18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ){
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Add course",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color  = Navy
            )

            OutlinedTextField(
                value = department,
                onValueChange = {department = it},
                label = {Text("Department")},
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = number,
                onValueChange = {number = it },
                label = {Text("Course number")},
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Button (
                onClick = {
                    if (department.isNotBlank() && number.isNotBlank()){
                        onAddCourse(
                            Course(
                                department = department.trim().uppercase(),
                                number = number.trim()
                            )
                        )

                        department = ""
                        number = ""
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = department.isNotBlank() && number.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Navy
                )
            ) {
                Text("Add course")
            }
        }
    }
}

@Composable
fun RequirementList(
    degree: Degree,
    studentCourses: List<Course>
){
    val statuses = reqStatus (degree, studentCourses)

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            "Degree requirements",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Navy
        )

        Text(
            "Required courses for your degree.",
            fontSize = 15.sp,
            color = GrayText
        )

        degree.requirements.forEachIndexed{ index, requirement ->
            RequirementRow(
                requirement = requirement,
                satisfied = statuses[index]
            )
        }

        if (statuses.isNotEmpty() && statuses.all {it}) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = LightGreen
                )
            ) {
                Text(
                    "All requirements satisified!",
                    color = Sage,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(15.dp)
                )
            }
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
            }
        }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (satisfied) {
                    " √ $description"
                } else {
                    "x $description"
                }
            )
            Text(
                description,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp),
                fontWeight = FontWeight.Medium,
                color = DarkText
            )

            Text(
                if (satisfied) "Complete" else "Missing",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (satisfied) Sage else GrayText
            )
        }
    }
}

@Composable
fun CourseList(
    courses : List<Course>,
    onRemoveCourse: (Course) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)){
        Text(
            "My future courses",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Navy
        )

        if (courses.isEmpty()){
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Text(
                    "No courses yet. Add one.",
                    modifier = Modifier.padding(18.dp),
                    color = GrayText
                )
            }
        }

        courses.forEach { course->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(15.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ){
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface (
                        shape = RoundedCornerShape(10.dp),
                        color = LightGreen
                    ) {
                        Text(
                            course.department,
                            color = Sage,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(10.dp)
                        )
                    }

                    Text(
                        course.number,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 12.dp),
                        fontWeight = FontWeight.SemiBold,
                        color = Navy
                    )

                    TextButton(
                        onClick = { onRemoveCourse(course)}
                    ) {
                        Text(
                            "Remove",
                            color = Color(0xFFAA5B55)
                        )
                    }
                }
            }
        }
    }
}
