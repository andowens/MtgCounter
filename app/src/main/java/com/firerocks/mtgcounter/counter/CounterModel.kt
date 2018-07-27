package com.firerocks.mtgcounter.counter

data class Player (var name: String, var health: Int)

fun Player.isValid() = name.isNotEmpty()

fun Player.isDead() = health <= 0