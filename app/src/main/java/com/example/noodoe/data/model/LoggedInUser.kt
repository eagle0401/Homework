package com.example.noodoe.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 *
 *  loginTest: String = {
 *      "objectId":"WkuKfCAdGq",
 *      "username":"test2@qq.com",
 *      "code":"4wtmah5h",
 *      "isVerifiedReportEmail":true,
 *      "reportEmail":"test2@qq.com",
 *      "createdAt":"2019-07-12T07:07:18.027Z",
 *      "updatedAt":"2019-11-15T19:33:24.883Z",
 *      "timezone":12,
 *      "ACL":{
 *          "WkuKfCAdGq":{
 *              "read":true,
 *              "write":true
 *          }
 *      },
 *      "sessionToken":"r:b7d4510a1d39a6a3febbb74d3a725d83"
 *  }
 */

data class LoggedInUser(
    val objectId: String,
    val username: String,
    val code: String,
    val isVerifiedReportEmail: Boolean,
    val reportEmail: String,
    val createdAt: String,
    var updatedAt: String,
    var timezone: Int,
    val sessionToken: String
)