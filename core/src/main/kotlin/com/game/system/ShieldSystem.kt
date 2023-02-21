package com.game.system

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.game.component.*
import com.game.event.ButtonPressedEvent
import com.game.event.ButtonShieldPressed
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import ktx.log.logger

@AllOf([ShieldComponents::class])
class ShieldSystem(
    private val animationComponents: ComponentMapper<AnimationComponent>,
    private val shieldComponents: ComponentMapper<ShieldComponents>,
    private val playerComponents: ComponentMapper<PlayerComponent>,
) : IteratingSystem(), EventListener {

    private var playerShield: Boolean = false
    private var isDone: Boolean = false
    override fun onTickEntity(entity: Entity) {
        val shieldComponent = shieldComponents[entity]
        if (entity in playerComponents) {


            shieldComponent.doShield = playerShield

            if (!playerShield) {
                shieldComponent.holdingShield = playerShield
                isDone=false
            }

            if (shieldComponent.doShield) {
                if (!isDone) {

                    animationComponents.getOrNull(entity)?.let { animation ->
                        animation.nextAnimation(AnimationState.SHIELD)
                        animation.mode = PlayMode.NORMAL
                        isDone=true
                    }
                }


                shieldComponent.holdingShield = true
            }
        }
    }

    override fun handle(event: Event?): Boolean {
        when (event) {
            is ButtonShieldPressed -> {
                playerShield = event.shield
            }
        }
        return true
    }

    companion object {
        private val log = logger<ShieldSystem>()
    }

}