package io.github.yeh35.channelopenapi.schma.block

data class WebPage(
    val id: String,
    val url: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val videoUrl: String,
    val publisher: String,
    val author: String,
    val width: Int,
    val height: Int,
    val bucket: String,
    val previewKey: String,
    val logo: String,
    val name: String,
)