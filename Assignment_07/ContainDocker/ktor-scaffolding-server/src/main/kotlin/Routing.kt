package com.example

import com.unboundid.ldap.sdk.Filter
import com.unboundid.ldap.sdk.LDAPConnection
import com.unboundid.ldap.sdk.LDAPException
import com.unboundid.ldap.sdk.ResultCode
import com.unboundid.ldap.sdk.SearchRequest
import com.unboundid.ldap.sdk.SearchScope
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable


@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)


@Serializable
data class LoginResponse(
    val success: Boolean,
    val message: String,
    val customToken: String? = null
)


fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello, World!")
        }
        get("/json/kotlinx-serialization") {
            call.respond(mapOf("hello" to "world"))
        }

        post("/login"){
            val request = try {
                call.receive<LoginRequest>()
            } catch (e : Exception){
                call.respond(
                    HttpStatusCode.BadRequest,
                    LoginResponse(false, "Invalid login")
                )
                return@post
            }

            val ldapHost = System.getenv("LDAP_HOST") ?:  "localhost"
            val ldapPort = (System.getenv("LDAP_PORT") ?: "389").toInt()
            val adminPassword = System.getenv("LDAP_ADMIN_PASSWORD")

            if (adminPassword.isNullOrBlank()){
                application.log.error("LDAP_ADMIN_PASSWORD is not configured")
                call.respond(
                    HttpStatusCode.InternalServerError,
                    LoginResponse(false, "Authentication service unconfigured")
                )
                return@post
            }

            try {
                val userDn = findUserDn(
                    host = ldapHost,
                    port = ldapPort,
                    adminPassword = adminPassword,
                    username = request.username
                )

                if (userDn == null){
                    call.respond(
                        HttpStatusCode.Unauthorized,
                        LoginResponse(false, "Invalid credentials")
                    )
                    return@post
                }

                LDAPConnection(ldapHost, ldapPort).use { userConnection ->
                    userConnection.bind(userDn, request.password)
                }
//
//                call.respond(
//                    HttpStatusCode.OK,
//                    LoginResponse(true, "Login successful")
//                )

                val firebaseUid = "ldap_${request.username}"
                val customToken = try {
                    FirebaseAdmin.auth.createCustomToken(firebaseUid)
                } catch (e: Exception) {
                    application.log.error(
                    "Firebase custom token creation failed", e
                    )

                    call.respond(
                        HttpStatusCode.InternalServerError,
                        LoginResponse(
                            false,
                            "Authentication service not available"
                        )
                    )
                    return@post
                }

                call.respond(
                    HttpStatusCode.OK,
                    LoginResponse(
                        success = true,
                        message = "Login succesful",
                        customToken = customToken
                    )
                )

            } catch (e: LDAPException){
                if (e.resultCode == ResultCode.INVALID_CREDENTIALS){
                    call.respond(
                        HttpStatusCode.Unauthorized,
                        LoginResponse(false, "Invalid credentials")
                    )
                } else {
                    application.log.error("LDAP authentication failed", e)
                    call.respond(
                        HttpStatusCode.ServiceUnavailable,
                        LoginResponse(false, "Authentication service not available")

                    )
                }
            } catch (e: Exception){
                application.log.error("Login failed", e)
                call.respond(
                    HttpStatusCode.ServiceUnavailable,
                    LoginResponse(false, "Authentication not available")
                )
            }
        }
    }
}

private fun findUserDn(
    host: String,
    port: Int,
    adminPassword: String,
    username: String
) : String? {
    LDAPConnection(host, port).use {
        connection ->
        connection.bind("cn=admin,dc=example,dc=org", adminPassword)

        val searchRequest = SearchRequest(
            "ou=people,dc=example,dc=org",
            SearchScope.SUB,
            Filter.createEqualityFilter("uid", username)
        )

        val entries = connection.search(searchRequest).searchEntries

        return entries.firstOrNull()?.dn
    }
}