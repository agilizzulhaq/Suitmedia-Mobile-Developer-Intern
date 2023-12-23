package com.agilizzulhaq.suitmediainterntest.data.api

import com.agilizzulhaq.suitmediainterntest.data.model.User

data class ApiResponse(
    val page: Int,
    val per_page: Int,
    val total: Int,
    val total_pages: Int,
    val data: List<User>,
    val support: Support
)

data class Support(
    val url: String,
    val text: String
)