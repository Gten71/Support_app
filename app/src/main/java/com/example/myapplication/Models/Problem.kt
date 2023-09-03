package com.example.myapplication.Models

import java.io.Serializable

data class Problem(
    val key: String,
    var userId: String? = null,
    var title: String,
    val content: String? = "",
    var employer: String? = null,
    var photoUrl: String? = null
)
: Serializable {
    constructor() : this("", "", "")
}