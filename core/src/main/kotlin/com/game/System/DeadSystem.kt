package com.game.System

import com.game.component.DeadComponent
import com.game.component.LifeComponent
import com.game.component.StateComponent
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem


@AllOf([DeadComponent::class])
class DeadSystem(
    private val deadComponents: ComponentMapper<DeadComponent>,
    private val lifeComponent: ComponentMapper<LifeComponent>,
    private val stateComponents : ComponentMapper<StateComponent>
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