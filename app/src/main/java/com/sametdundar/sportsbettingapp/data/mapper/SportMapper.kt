package com.sametdundar.sportsbettingapp.data.mapper

import com.sametdundar.sportsbettingapp.data.remote.SportResponse
import com.sametdundar.sportsbettingapp.domain.model.Sport

fun SportResponse.toDomain(): Sport = Sport(
    key = key,
    group = group,
    title = title,
    description = description,
    active = active,
    hasOutrights = has_outrights
) 