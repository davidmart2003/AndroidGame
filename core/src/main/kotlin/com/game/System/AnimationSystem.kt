package com.game.System

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.game.Component.AnimationComponent
import com.game.Component.AnimationComponent.Companion.NO_ANIMATION
import com.game.Component.ImageComponent
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import ktx.app.gdxError
import ktx.collections.map

@AllOf([AnimationComponent::class, ImageComponent::class])
class AnimationSystem(
    private val textureAtlas: TextureAtlas,
    private val animationComponents: ComponentMapper<AnimationComponent>,
    private val imageComponents: ComponentMapper<ImageComponent>
) : IteratingSystem() {

    private val cachedAnimation = mutableMapOf<String, Animation<TextureRegionDrawable>>()


    override fun onTickEntity(entity: Entity) {
        val animationComponent = animationComponents[entity]

        if (animationComponent.nextAnimation == NO_ANIMATION) {
            animationComponent.stateTime += deltaTime

        } else {
            animationComponent.animation = animation(animationComponent.nextAnimation)
            animationComponent.stateTime = 0f
            animationComponent.nextAnimation= NO_ANIMATION

        }
        animationComponent.animation.playMode = animationComponent.playMode
        imageComponents[entity].image.drawable = animationComponent.animation.getKeyFrame(animationComponent.stateTime)
    }

    private fun animation(anyKeyPath:String) : Animation<TextureRegionDrawable>{
        return cachedAnimation.getOrPut(anyKeyPath) {
            val regions = textureAtlas.findRegions(anyKeyPath)
            if(regions.isEmpty){
                gdxError("No hay regiones para esa imagen")
            }
            Animation(DEFAULT_FRAME_DURATION,regions.map { TextureRegionDrawable(it) })
        }

    }

    companion object{
        private const val  DEFAULT_FRAME_DURATION=1/8f
    }
}