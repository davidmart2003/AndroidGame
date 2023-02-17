package com.game.system

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.game.ai.Action
import com.game.component.ImageComponent
import com.game.component.MoveComponent
import com.game.component.PhysicComponent
import com.game.component.PlayerComponent
import com.game.event.ButtonPressedEvent
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import ktx.log.logger
import ktx.math.component1
import ktx.math.component2

@AllOf([MoveComponent::class, PhysicComponent::class])
class MoveSystem(

    private val moveComponents: ComponentMapper<MoveComponent>,
    private val physicsComponents: ComponentMapper<PhysicComponent>,
    private val imageComponents: ComponentMapper<ImageComponent>,
    private val playerComponents: ComponentMapper<PlayerComponent>

) : IteratingSystem(), EventListener {
    private var playerCos: Float = 0f
    private var playerSin: Float = 0f
    override fun onTickEntity(entity: Entity) {
        val moveComponent = moveComponents[entity]
        val physicComponent = physicsComponents[entity]
        val masa = physicComponent.body.mass
        val (velX, velY) = physicComponent.body.linearVelocity

        if (entity in playerComponents) {
            log.debug { "ACTUALIZANDO" }

            moveComponent.sin = playerSin
            moveComponent.cos = playerCos
        }
        if (moveComponent.cos == 0f && moveComponent.sin == 0f || moveComponent.root) {
            physicComponent.impulse.set(
                masa * (0f - velX),
                masa * (0f - velY)
            )
            return
        }


        physicComponent.impulse.set(
            masa * (moveComponent.speed * moveComponent.cos - velX),
            masa * (moveComponent.speed * moveComponent.sin - velY)

        )

        imageComponents.getOrNull(entity)?.let { imageComponent ->
            if (moveComponent.cos != 0f) {
                imageComponent.image.flipX = moveComponent.cos < 0
            }
        }
    }

    override fun handle(event: Event?): Boolean {
        when (event) {
            is ButtonPressedEvent -> {

                playerCos=event.cos
                playerSin=event.sin
            }

            else -> return false
        }
        return true
    }
    companion object {
        private val log = logger<MoveSystem>()
    }
}