package com.kennah.wecatch.local.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class Gym(
        @JsonProperty("_id") val id: String?,
        @JsonProperty("details") val details: List<GymDetails>?,
        @JsonProperty("name") val name: String?,
        @JsonProperty("prestige") val prestige: Int?,
        @JsonProperty("raid_battle_ms") val raidBattleMs: Long?,
        @JsonProperty("raid_completed") val raidCompleted: Boolean?,
        @JsonProperty("raid_end_ms") val raidEndMs: Long?,
        @JsonProperty("raid_level") val raidLevel: Int?,
        @JsonProperty("raid_pokemon_cp") val raidPokemonCp: Int?,
        @JsonProperty("raid_pokemon_id") val raidPokemonId: Int?,
        @JsonProperty("team") val team: Int?,
        @JsonProperty("location") val location: Array<Double>?,
        @JsonProperty("last_modified") val lastModified: Array<Long>?,
        @JsonProperty("sponsor") val sponsor: Int?
)