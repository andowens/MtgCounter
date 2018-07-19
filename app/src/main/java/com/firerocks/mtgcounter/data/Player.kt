package com.firerocks.mtgcounter.data

data class Player (private var name: String, private var health: String)

fun Player.validName(name: String) = name.length < 2