package com.game.component

/**
 * Componente de las entidades que requieran un sistema de movimiento manual
 *
 * @property speed Velocidad a la que se mueve la entidad
 * @property cos La direccion del movimineto en el eje X
 * @property sin La direccion del movimiento en el eje Y
 * @property root True si la entidad es el root del .tree
 */
data class MoveComponent(
    var speed : Float = 0f,
    var cos: Float = 0f,
    var sin: Float = 0f,
    var root : Boolean =false
    ){
}