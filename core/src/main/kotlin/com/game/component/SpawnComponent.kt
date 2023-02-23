package com.game.component

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import ktx.math.vec2

/**
 *Configuracion de la entidad que tiene al spawnear
 *
 * @property atlasKey Nombre de la entidad segun el atlas
 * @property speedScaling Escalado de velocidad
 * @property canAttack True si la entidad puede atacar
 * @property attackScaling Escalado de daño
 * @property attackDelay Tiempo de espera para realizar otro ataque
 * @property attackExtraRange Rango extra de ataque
 * @property lifeScaling Escalado de vida
 * @property physicScaling Escalado de físicas
 * @property physicOffset Offset de la física segun el centro del cuerpo
 * @property bodyType Tipo de cuerpo
 * @property aiTreePAth Ruta del archivo .tree
 * @property exp Experiencia de la entidad
 * @property dropExp Cantidad de experiennia a dar
 * @property lvlPlayer nivel del jugador
 * @property nextLevel Experiencia que se neceista para subir al siguiente nivel
 */
data class SpawnConfiguration(
    val atlasKey: String,
    val speedScaling: Float = 1f,
    val canAttack : Boolean = true,
    var attackScaling : Float=1f,
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

/**
 * Constante de velocidad
 */
const val DEFAULT_SPEED = 3f

/**
 * Constante de daño
 */
var DEFAULT_ATTACK_DAMAGE= 5f

/**
 * Constante de vida
 */
const val DEFAULT_LIFE =20f

/**
 * Componente de las entidad que requieren de un sistema de spawn
 *
 * @property type Nombre de la entidad para spawnear
 * @property location Localizacion de la entidad al ser spawneada
 */
data class SpawnComponent(
    var type: String = "",
    var location: Vector2 = vec2()
)