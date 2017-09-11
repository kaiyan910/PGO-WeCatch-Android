package com.kennah.wecatch.local.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class GymDetails(
        @JsonProperty("iv") val iv: Int?,
        @JsonProperty("move_1") val move1: Int?,
        @JsonProperty("move_2") val move2: Int?,
        @JsonProperty("player_level") val playerLevel: Int?,
        @JsonProperty("player_name") val player: String?,
        @JsonProperty("pokemon_cp") val pokemonCp: Int?,
        @JsonProperty("pokemon_id") val pokemonId: Int?
)