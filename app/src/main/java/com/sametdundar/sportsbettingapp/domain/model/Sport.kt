package com.sametdundar.sportsbettingapp.domain.model

data class Sport(
    val key: String,
    val group: String,
    val title: String,
    val description: String?,
    val active: Boolean,
    val hasOutrights: Boolean
) 