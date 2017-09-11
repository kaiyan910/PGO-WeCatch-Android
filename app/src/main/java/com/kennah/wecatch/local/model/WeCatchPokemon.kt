package com.kennah.wecatch.local.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class WeCatchPokemon(
        @JsonProperty("_id") val id: String?,
        @JsonProperty("atk") val atk: String?,
        @JsonProperty("attack") val attack: Int?,
        @JsonProperty("defense") val defense: Int?,
        @JsonProperty("stamina") val stamina: Int?,
        @JsonProperty("cp") val cp: Int?,
        @JsonProperty("iv") val iv: Int?,
        @JsonProperty("move1") val move1: Int?,
        @JsonProperty("move2") val move2: Int?,
        @JsonProperty("location") val location: Array<Double>?
)