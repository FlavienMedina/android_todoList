package com.flavienmedina.mytodolist.network

data class Task(
    val id: String,
    val content: String,
    val completed: Boolean
)