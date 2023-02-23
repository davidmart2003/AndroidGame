package com.game.component

/**
 * Componente de las entidad que requieran de un sistema de escudo
 *
 * @property doShield True Si la entidad quiere usar el escudo
 * @property holdingShield True si la entidad esta sujetando el escudo
 */
class ShieldComponents(
    var doShield:Boolean=false,
    var holdingShield: Boolean=false

) {
}