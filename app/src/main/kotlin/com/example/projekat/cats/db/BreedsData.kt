package com.example.projekat.cats.db

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class BreedsData(
    @PrimaryKey val id: String,
    val name: String,
    val temperament: String,
    val alt_names: String? = null,
    val description: String,
    val origin: String,
    val life_span: String,
    val adaptability: Int,
    val affection_level: Int,
    val dog_friendly: Int,
    val wikipedia_url: String?="",

    @Embedded
    val image: BreedsImage? = null,
    val profilePhoto: String? = ""
){
    fun doesMatchSearchQuery(query: String): Boolean {
        return name.contains(query, true)
    }

    fun getListOfTemperaments(): List<String> {
        return temperament.replace(" ", "").split(",")
    }
}
@Serializable
data class BreedsImage(
    val url: String
)
