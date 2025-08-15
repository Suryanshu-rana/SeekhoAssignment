package com.suryanshu.seekhoassignment.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken

// API Response Models
data class TopAnimeResponse(
    @SerializedName("data")
    val data: List<AnimeData> = emptyList(),
    @SerializedName("pagination")
    val pagination: Pagination? = null
)

data class AnimeDetailsResponse(
    @SerializedName("data")
    val data: AnimeData? = null
)

data class Pagination(
    @SerializedName("last_visible_page")
    val lastVisiblePage: Int = 0,
    @SerializedName("has_next_page")
    val hasNextPage: Boolean = false
)

data class AnimeData(
    @SerializedName("mal_id")
    val malId: Int = 0,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("episodes")
    val episodes: Int? = null,
    @SerializedName("score")
    val score: Double? = null,
    @SerializedName("images")
    val images: Images? = null,
    @SerializedName("synopsis")
    val synopsis: String? = null,
    @SerializedName("genres")
    val genres: List<Genre>? = null,
    @SerializedName("trailer")
    val trailer: Trailer? = null,
    @SerializedName("studios")
    val studios: List<Studio>? = null
)

data class Images(
    @SerializedName("jpg")
    val jpg: ImageFormat? = null,
    @SerializedName("webp")
    val webp: ImageFormat? = null
)

data class ImageFormat(
    @SerializedName("image_url")
    val imageUrl: String? = null,
    @SerializedName("large_image_url")
    val largeImageUrl: String? = null
)

data class Genre(
    @SerializedName("mal_id")
    val malId: Int = 0,
    @SerializedName("name")
    val name: String = ""
)

data class Trailer(
    @SerializedName("youtube_id")
    val youtubeId: String? = null,
    @SerializedName("url")
    val url: String? = null,
    @SerializedName("embed_url")
    val embedUrl: String? = null
)

data class Studio(
    @SerializedName("mal_id")
    val malId: Int = 0,
    @SerializedName("name")
    val name: String = ""
)

// Room Database Entity
@Entity(tableName = "anime")
data class AnimeEntity(
    @PrimaryKey
    val malId: Int,
    val title: String,
    val episodes: Int?,
    val score: Double?,
    val imageUrl: String?,
    val synopsis: String?,
    @TypeConverters(GenreConverter::class)
    val genres: List<Genre>?,
    val trailerUrl: String?,
    @TypeConverters(StudioConverter::class)
    val studios: List<Studio>?,
    val lastUpdated: Long = System.currentTimeMillis()
)

// Type Converters for Room
class GenreConverter {
    private val gson = Gson()
    
    @TypeConverter
    fun fromGenreList(genres: List<Genre>?): String? {
        return genres?.let { gson.toJson(it) }
    }
    
    @TypeConverter
    fun toGenreList(genresString: String?): List<Genre>? {
        if (genresString == null) return null
        val type = object : TypeToken<List<Genre>>() {}.type
        return gson.fromJson(genresString, type)
    }
}

class StudioConverter {
    private val gson = Gson()
    
    @TypeConverter
    fun fromStudioList(studios: List<Studio>?): String? {
        return studios?.let { gson.toJson(it) }
    }
    
    @TypeConverter
    fun toStudioList(studiosString: String?): List<Studio>? {
        if (studiosString == null) return null
        val type = object : TypeToken<List<Studio>>() {}.type
        return gson.fromJson(studiosString, type)
    }
}

// Mappers
fun AnimeData.toEntity(): AnimeEntity {
    return AnimeEntity(
        malId = malId,
        title = title,
        episodes = episodes,
        score = score,
        imageUrl = images?.jpg?.imageUrl,
        synopsis = synopsis,
        genres = genres,
        trailerUrl = trailer?.url,
        studios = studios
    )
}

fun AnimeEntity.toAnimeData(): AnimeData {
    return AnimeData(
        malId = malId,
        title = title,
        episodes = episodes,
        score = score,
        images = Images(jpg = ImageFormat(imageUrl = imageUrl, largeImageUrl = imageUrl)),
        synopsis = synopsis,
        genres = genres,
        trailer = trailerUrl?.let { Trailer(url = it, youtubeId = it.substringAfterLast("v=", ""), embedUrl = it) },
        studios = studios
    )
}