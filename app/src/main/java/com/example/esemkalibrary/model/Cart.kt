package com.example.esemkalibrary.model

data class Cart(
    var isbn: String? = null,
    var publish: String? = null,
    var available: String? = null,
    var desc: String? = null,
    var book: Book
)
