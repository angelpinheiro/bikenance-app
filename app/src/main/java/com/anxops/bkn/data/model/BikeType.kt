package com.anxops.bkn.data.model

sealed class BikeType(val name: String, val extendedType: String) {
    object Mtb : BikeType("MTB", "MTB")
    object Road : BikeType("Road", "Road Bike")
    object EBike : BikeType("E-Bike", "Electric Bike")
    object Gravel : BikeType("Gravel", "Gravel Bike")
    object Stationary : BikeType("Stationary", "Stationary Bike")
    object Unknown : BikeType("Unknown", "Unknown bike type")

    companion object {
        private val allKnownTypes by lazy {
            listOf(Mtb, Road, EBike, Gravel, Stationary)
        }

        private val allTypes by lazy {
            allKnownTypes.plus(Unknown)
        }

        fun getByName(name: String): BikeType = allTypes.find { it.name == name } ?: Unknown

        fun getAll() = allTypes

        fun getAllKnown() = allKnownTypes
    }
}
