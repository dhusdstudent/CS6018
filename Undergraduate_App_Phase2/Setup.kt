

//NOTE TO SELF: Kotlin calls this 'type inference'
fun setUpCourses() = listOf(
    Course("CS", 1010),
    Course("CS", 1111),
    Course("CS", 1212),
    Course("CS", 2222),
    Course("CS", 3333),
    Course("SOC", 1011),
    Course("SOC", 1111),
    Course("SOC", 1212),
    Course("SOC", 3010),
    Course("SOC", 3020),
    Course("SOC", 3030),
    Course("PHIL", 1011),
    Course("PHIL", 1111),
    Course("PHIL", 2211),
    Course("PHIL", 2212),
    Course("PHIL", 3333),
    Course("PHIL", 2000),
    Course("PHIL", 3000)
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

fun createDegree_SOC(): Degree {
    return Degree(
        requirements = listOf(
            CourseRequirement(
                Course("SOC", 1011),
            ),
            OneOfRequirement(
                listOf(
                    Course("SOC", 1111),
                    Course("SOC", 1212),
                )
            ),
            OneOfRequirement(
                listOf(
                    Course("SOC", 3010),
                    Course("SOC", 3020),
                    Course("SOC", 3030),
                )
            )
        )
    )
}

fun createDegree_PHIL(): Degree {
    return Degree(
        requirements = listOf(
            OneOfRequirement(
                listOf(
                    Course("PHIL", 1011),
                    Course("PHIL", 1111),
                )
            ),
            OneOfRequirement(
                listOf(
                    Course("PHIL", 2211),
                    Course("PHIL", 2212),
                    Course("PHIL", 3333)
                )
            ),
            OneOfRequirement(
                listOf(
                    Course("PHIL", 2000),
                    Course("PHIL", 3000)
                )
            )
        )
    )
}

fun majorReqs(input: Int) : Degree {
    return when (input) {
        1 -> createDegree_CS()
        2 -> createDegree_PHIL()
        3 -> createDegree_SOC()
        else -> error("Invalid major")
    }
}



fun createStudent(input: Int): Student {
    val degree = majorReqs(input)

    return Student(
        major = degree,
        courses = emptyList())
}

fun pickYourMajor(){
    print("Enter the number that corresponds to your major...")
    print("1. Computer Science")
    print("2. Philosophy")
    print("3. Sociology")

    val answer = readLine()!!.toInt()
    majorReqs(answer)
}

