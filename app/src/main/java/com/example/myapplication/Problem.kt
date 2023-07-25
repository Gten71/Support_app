package com.example.myapplication

import java.io.Serializable

class Problem(
    val key: String,
    var title: String,
    val content: String? = "",
    val userId: String? = ""
): Serializable {
    // Добавьте пустой конструктор, необходимый для работы с Firebase
    constructor() : this("", "", "")
}