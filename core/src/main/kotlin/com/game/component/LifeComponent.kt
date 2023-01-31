package com.game.component

class LifeComponent(
    var life: Float = 100f,
    var maxLife: Float = 100f,
    var regeneration: Float = 0f,
    var takeDamage: Float = 0f,
    var isTakingDamage : Boolean=false,
    var exp:Int =0,
) {
    val isDead: Boolean
    get() = life<=0f
}