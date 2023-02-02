package com.game.system

import com.game.component.AiComponent
import com.game.component.DeadComponent
import com.github.quillraven.fleks.*

@AllOf([AiComponent::class])
@NoneOf([DeadComponent::class])
class AiSystem(
    private val aiComponents : ComponentMapper<AiComponent>,
) : IteratingSystem() {
    override fun onTickEntity(entity: Entity) {
        with(aiComponents[entity]){
            behaviorTree.step()
        }
    }
}