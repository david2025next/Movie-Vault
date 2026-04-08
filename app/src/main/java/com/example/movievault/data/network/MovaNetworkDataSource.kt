package com.example.movievault.data.network

import com.example.movievault.data.network.model.NetworkMovie

interface MovaNetworkDataSource {

    suspend fun getMovies(page : Int, language : String = "fr-FR"): List<NetworkMovie>
}