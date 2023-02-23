package com.game.component

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable


/**
 * Enumerado que va a representar la accion que hará el modelo de la imagen
 */
enum class AnimationState{
    IDLE,RUN,DEATH,ATTACK,TAKEHIT,JUMP,SHIELD;

    /**
     * Nombre que tiene la accion en el atlas
     */
    val atlasKey:String = this.toString().lowercase()
}

/**
 * Componente que tiene la entidad que requiera animación
 *
 * @property atlasKey Nombre del modelo de la imagen que tiene en el atlas
 * @property stateTime Tiempo que hay entre cada frame de cada animacion
 * @property mode Modo de la animacion
 *
 * @constructor Crea el componente con valores default
 */
data class AnimationComponent(
    var atlasKey: String = "",
    var stateTime: Float = 0f,
    var mode: Animation.PlayMode = Animation.PlayMode.LOOP
) {
    /**
     * Animacion que representará cada modelo en el texture atlas
     */
    lateinit var animation: Animation<TextureRegionDrawable>

    /**
     * Siguiente animacion que se representará, por defecto no hay siguiente animación
     */
    var nextAnimation: String = NO_ANIMATION

    /**
     * Cambia la animación acutal por la siguiente
     *
     * @param  atlasKey NOmbre del modelo que tiene en el atlas
     * @param type Estado de accion que representara el modelo
     */
    fun nextAnimation(atlasKey: String, type: AnimationState) {
        this.atlasKey = atlasKey
        nextAnimation = "$atlasKey/${type.atlasKey}"
    }

    fun nextAnimation(type: AnimationState) {
        nextAnimation = "$atlasKey/${type.atlasKey}"
    }

    fun clearAnimation() {
        nextAnimation = NO_ANIMATION
    }

    /**
     * Devuelve true si la animacion ha sido finalizada
     */
    fun isAnimationFinished() = animation.isAnimationFinished(stateTime)

    companion object {
        const val NO_ANIMATION = ""
    }
}