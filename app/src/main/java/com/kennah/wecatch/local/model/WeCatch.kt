package com.kennah.wecatch.local.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class WeCatch(
        @JsonProperty("pokemons") val pokemons: List<WeCatchPokemon>,
        @JsonProperty("gyms") val gyms: List<Gym>
)