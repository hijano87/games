package com.hijano.games.api

import com.api.igdb.utils.ImageSize
import com.api.igdb.utils.ImageType
import com.api.igdb.utils.imageBuilder
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CoverResponse(
    val id: Long,
    val image_id: String
)

fun CoverResponse.toImageUrl(size: ImageSize, type: ImageType): String {
    return imageBuilder(image_id, size, type)
}