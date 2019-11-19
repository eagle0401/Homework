package com.example.noodoe.ui.info

import com.example.noodoe.data.model.UpdateTimeAt

/**
 * Authentication result : success (user details) or error message.
 */
data class InfoUpdateResult(
    val success: UpdateTimeAt? = null,
    val error: Int? = null,
    var timezone: Int
)
