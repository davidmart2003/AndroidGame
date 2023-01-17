package com.game.System

import com.game.Component.DeadComponent
import com.game.Component.LifeComponent
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem


@AllOf([DeadComponent::class])
class DeadSystem(
    private val deadComponents: ComponentMapper<DeadComponent>,
    private val lifeComponent: ComponentMapper<LifeComponent>
) : IteratingSystem() {
    override fun onTickEntity(entity: Entity) {
        val deadComponent = deadComponents[entity]

        if (deadComponent.reviveTime == 0f) {
            world.remove(entity)
            return
        }

        deadComponent.reviveTime -= deltaTime
        if (deadComponent.reviveTime <= 0f) {
            with(lifeComponent[entity]) {
                life = maxLife
            }
            configureEntity(entity) {
                deadComponents.remove(entity)
            }
        }
    }

}