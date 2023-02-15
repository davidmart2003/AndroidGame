package com.game.component

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable



enum class AnimationState{
    IDLE,RUN,DEATH,ATTACK,TAKEHIT,JUMP,SHIELD;

    val atlasKey:String = this.toString().lowercase()
}
data class AnimationComponent(
    var atlasKey: String = "",
    var stateTime: Float = 0f,
    var mode: Animation.PlayMode = Animation.PlayMode.LOOP
) {
    lateinit var animation: Animation<TextureRegionDrawable>
    var nextAnimation: String = NO_ANIMATION

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

    fun isAnimationFinished() = animation.isAnimationFinished(stateTime)

    companion object {
        const val NO_ANIMATION = ""
    }
}