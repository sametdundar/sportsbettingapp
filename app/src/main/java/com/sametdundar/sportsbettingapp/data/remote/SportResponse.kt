package com.sametdundar.sportsbettingapp.data.remote

data class SportResponse(
    val key: String,
    val group: String,
    val title: String,
    val description: String?,
    val active: Boolean,
    val has_outrights: Boolean
) 