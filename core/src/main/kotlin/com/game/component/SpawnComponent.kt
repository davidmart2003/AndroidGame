package com.game.component

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import ktx.math.vec2

data class SpawnConfiguration(
    val model: AnimationType,
    val speedScaling: Float = 1f,
    val canAttack : Boolean = true,
    val attackScaling : Float=1f,
    val attackDelay: Float = 0.2f,
    val attackExtraRange : Float = 0f,
    val lifeScaling : Float = 1f,
    val physicScaling: Vector2 = vec2(1f, 1f),
    val physicOffset: Vector2 = vec2(1f, 1f),
    val bodyType : BodyDef.BodyType = BodyDef.BodyType.DynamicBody,
    val aiTreePAth:String="",
    val exp:Int=0,
    val dropExp:Int=0,
    val lvlPlayer:Int=1,
    val nextLevel:Int=10
)

const val DEFAULT_SPEED = 3f
const val DEFAULT_ATTACK_DAMAGE= 5f
const val DEFAULT_LIFE =20f

data class SpawnComponent(
    var type: String = "",
    var location: Vector2 = vec2()
)