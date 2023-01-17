package com.game.System

import com.game.Component.ImageComponent
import com.game.Component.MoveComponent
import com.game.Component.PhysicComponent
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import ktx.math.component1
import ktx.math.component2

@AllOf([MoveComponent::class, PhysicComponent::class])
class MoveSystem(

    private val moveComponents: ComponentMapper<MoveComponent>,
    private val physicsComponents: ComponentMapper<PhysicComponent>,
    private val imageComponents : ComponentMapper<ImageComponent>

) : IteratingSystem() {
    override fun onTickEntity(entity: Entity) {
        val moveComponent = moveComponents[entity]
        val physicComponent = physicsComponents[entity]
        val masa = physicComponent.body.mass
        val (velX, velY) = physicComponent.body.linearVelocity

        if (moveComponent.cos == 0f && moveComponent.sin == 0f) {
            physicComponent.impulse.set(
                masa * (0f - velX),
                masa * (0f - velY)
            )
            return
        }

        physicComponent.impulse.set(
            masa * ( moveComponent.speed * moveComponent.cos -velX),
            masa * ( moveComponent.speed * moveComponent.sin -velY)

        )

        imageComponents.getOrNull(entity)?.let { imageComponent ->
            if(moveComponent.cos !=0f){
                imageComponent.image.flipX=moveComponent.cos <0
            }
        }
    }
}