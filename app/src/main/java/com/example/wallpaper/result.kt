package com.example.wallpaper

data class result(
    val next_page: String,
    val page: Int,
    val per_page: Int,
    val photos: ArrayList<Photo>,
    val total_results: Int
)