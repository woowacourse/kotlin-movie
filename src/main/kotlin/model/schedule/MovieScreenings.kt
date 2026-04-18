package model.schedule

import model.CinemaTime

fun List<MovieScreening>.onDate(date: CinemaTime): List<MovieScreening> = filter { it.isOnDate(date) }
