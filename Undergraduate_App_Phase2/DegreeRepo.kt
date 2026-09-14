class DegreeRepo(
    private val client: HttpClient ){
    suspend fun fetchDegreePlans(): DegreePlanList {
        //
    }

    suspend fun fetchDegree(path: String): Degree {
        //
    }
}