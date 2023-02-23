package com.game.component

/**
 * Componente de las entidades que requieran de un sistema de niveles
 *
 * @property level Nivel de la entidad
 * @property baseExp Experiencia requerida para subir de nivel
 */
class LevelComponent (
    var level : Int=1,
    var baseExp : Float=10f,

){
}