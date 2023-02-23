package com.game.component

/**
 * Componente de las entidades que requieran de un sistema de vida
 *
 * @property life Vida de la entidad
 * @property maxLife vida máxima de la entidad
 * @property regeneration Regeneración de vida de la entidad
 * @property takeDamage Cantidad de daño que ha recibido la entidad
 * @property isTakingDamage True si la entidad esta recibiendo daño
 * @property exp Experiencia que contiene la entidad
 */
class LifeComponent(
    var life: Float = 100f,
    var maxLife: Float = 100f,
    var regeneration: Float = 0f,
    var takeDamage: Float = 0f,
    var isTakingDamage : Boolean=false,
    var exp:Int =0,
) {
    /**
     * True si la entidad tiene la vida menor que 0
     */
    val isDead: Boolean
    get() = life<=0f
}