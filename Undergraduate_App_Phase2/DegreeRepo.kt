class DegreeRepo(
    private val client: HttpClient)
{

    private val baseURL =
            "https://msd2026.github.io/degreePlans/"

    suspend fun fetchDegreePlans(): DegreePlanList {
        return client.get("${baseURL}degreePlans.json").body()
        }

    suspend fun fetchDegree(path: String): Degree {
        return client.get("$baseUrl$path").body()
    }
}