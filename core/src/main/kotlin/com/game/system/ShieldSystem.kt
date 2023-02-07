package com.game.system

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.game.component.*
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem

@AllOf([ShieldComponents::class])
class ShieldSystem(
    private val animationComponents: ComponentMapper<AnimationComponent>,
    private val shieldComponents: ComponentMapper<ShieldComponents>
) : IteratingSystem() {
    override fun onTickEntity(entity: Entity) {
        val shieldComponent = shieldComponents[entity]

        if(shieldComponent.doShield ){
            animationComponents.getOrNull(entity)?.let { animation ->
            animation.nextAnimation(AnimationState.SHIELD)
                animation.playMode=PlayMode.NORMAL

            }
            val isDone = animationComponents.getOrNull(entity)?.isAnimationDone ?: true
            if (isDone) {
                shieldComponent.holdingShield = true
            }
        }

    }

}