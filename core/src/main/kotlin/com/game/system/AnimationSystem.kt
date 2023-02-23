package com.game.system

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.game.component.AnimationComponent
import com.game.component.AnimationComponent.Companion.NO_ANIMATION
import com.game.component.ImageComponent
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import ktx.app.gdxError
import ktx.collections.map
import ktx.log.logger

/**
 * Sistema que se encarga de las animaciones de las entidades
 *
 * @property textureAtlas Atlas que contiene las animaciones
 * @property animationComponents Conjunto de entidades que contiene animationComponente
 * @property imageComponents Conjuntos de entidades que contiene imageComponent
 */
@AllOf([AnimationComponent::class, ImageComponent::class])
class AnimationSystem(
    private val textureAtlas: TextureAtlas,
    private val animationComponents: ComponentMapper<AnimationComponent>,
    private val imageComponents: ComponentMapper<ImageComponent>
) : IteratingSystem() {

    /**
     * Almacen de las animaciones que estan cargadas
     */
    private val cachedAnimation = mutableMapOf<String, Animation<TextureRegionDrawable>>()


    /**
     * Por cada entidad que esta en el sistema cambia su animacion
     *
     * @param Entidad a ejecutar
     */
    override fun onTickEntity(entity: Entity) {
        val animationComponent = animationComponents[entity]
        if (animationComponent.nextAnimation == NO_ANIMATION) {
            animationComponent.stateTime += deltaTime

        } else {
            animationComponent.animation = animation(animationComponent.nextAnimation)
            animationComponent.stateTime = 0f
            animationComponent.nextAnimation = NO_ANIMATION

        }

        animationComponent.animation.playMode = animationComponent.mode
        imageComponents[entity].image.drawable =
            animationComponent.animation.getKeyFrame(animationComponent.stateTime)
    }

    /**
     * Carga la animacion segun el atlas y la guarda en el almacen
     *
     * @param anyKeyPath nombre de la animación en el atlas
     *
     * @return Devuelve la annimación cargada
     */
    private fun animation(anyKeyPath: String): Animation<TextureRegionDrawable> {
        return cachedAnimation.getOrPut(anyKeyPath) {
            log.debug { textureAtlas.findRegions(anyKeyPath).toString() }
            val regions = textureAtlas.findRegions(anyKeyPath)
            if (regions.isEmpty) {
                gdxError("No hay regiones para esa imagen, $anyKeyPath")
            }
            if (anyKeyPath == "Demon/attack") {
                Animation(
                    1/20f,
                    regions.map { TextureRegionDrawable(it) })
            } else {
                if (anyKeyPath == "char_blue_1/attack") {
                    Animation(
                        DEFAULT_FRAME_ATTACK_DURATION,
                        regions.map { TextureRegionDrawable(it) })

                } else {
                    Animation(DEFAULT_FRAME_DURATION, regions.map { TextureRegionDrawable(it) })

                }
            }
        }

    }

    companion object {
        private const val DEFAULT_FRAME_DURATION = 1 / 8f
        private const val DEFAULT_FRAME_ATTACK_DURATION = 1 / 15f
        private val log = logger<AnimationSystem>()

    }
}