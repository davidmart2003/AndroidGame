package com.game.component

/**
 * Componente que representa la entidad jugador
 *
 * @property actualLife Vida actual del jugador
 * @property actualStrenght Daño actual del jugador
 *
 */
class PlayerComponent(
    var actualLife:Float=100f,
    var actualStrenght:Float = DEFAULT_ATTACK_DAMAGE*3.75f,
    var actualTime:Float = 0f,
    var actualLevel:Float = 0f,
) {


}