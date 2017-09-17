package com.kennah.wecatch.local.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

class Pokemon(
        val pokemonId: Int,
        val latitude: Double,
        val longitude: Double,
        val expireTime: Long,
        val iv: Int,
        val cp: Int,
        val stamina: Int,
        val attack: Int,
        val defense: Int,
        val move1: Int,
        val move2: Int
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readDouble(),
            source.readDouble(),
            source.readLong(),
            source.readInt(),
            source.readInt(),
            source.readInt(),
            source.readInt(),
            source.readInt(),
            source.readInt(),
            source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(pokemonId)
        writeDouble(latitude)
        writeDouble(longitude)
        writeLong(expireTime)
        writeInt(iv)
        writeInt(cp)
        writeInt(stamina)
        writeInt(attack)
        writeInt(defense)
        writeInt(move1)
        writeInt(move2)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Pokemon> = object : Parcelable.Creator<Pokemon> {
            override fun createFromParcel(source: Parcel): Pokemon = Pokemon(source)
            override fun newArray(size: Int): Array<Pokemon?> = arrayOfNulls(size)
        }
    }
}