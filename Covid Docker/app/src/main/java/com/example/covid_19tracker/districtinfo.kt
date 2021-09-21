package com.example.covid_19tracker

import java.io.Serializable

class districinfo (
            val districtName: String,
            val active: String,
            val deceased: String,
            val confirmed: String,
            val recovered: String
        ): Serializable