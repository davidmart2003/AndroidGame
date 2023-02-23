package com.game.component


/**
 * Enumerado de los estados de ataque de las entidades
 */
enum class AttackState {
    READY, PREPARE, ATTACKING, DEAL_DAMAGE
}

/**
 * Componente que tiene la entidad que requiera de ataque
 *
 * @property doAttack La entidad quiere hacer un ataque
 * @property state Estado de ataque que se enceuntra la entidad
 * @property damage Daño que tiene la entidad
 * @property delay Tiempo de espera para realizar otro ataque
 * @property maxDelay Tiempo maximo de espera para realizar otro ataque
 * @property extraRange Rango del ataque
 */
class AttackComponent(
    var doAttack: Boolean = false,
    var state: AttackState = AttackState.READY,
    var damage: Int = 0,
    var delay: Float = 0f,
    var maxDelay: Float = 0f,
    var extraRange: Float = 0f
) {
    /**
     * La entidad esta lista para realizar otro ataque
     * Cambia el estado a Ready
     */
    val isReady : Boolean
    get() = state == AttackState.READY

    /**
     * La entidad esta preparado para atacar
     * Cambia el estado a Prepare
     */
    val isPrepared : Boolean
        get() = state == AttackState.PREPARE

    /**
     * La entidad esta atacando
     * Cambia el estado a Attacking
     */
    val isAttacking : Boolean
        get() = state == AttackState.ATTACKING

    /**
     * La entidad empieza a atacar, cambia el estado a Preparado
     */
    fun startAttack(){
        state = AttackState.PREPARE
    }
}