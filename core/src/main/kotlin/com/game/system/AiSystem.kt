package com.game.system

import com.game.component.AiComponent
import com.game.component.DeadComponent
import com.github.quillraven.fleks.*

/**
 * Sistema que se encarga de la IA del juego
 *
 * @property aiComponents Conjunto de entidades que contiene el AIComponent
 */
@AllOf([AiComponent::class])
@NoneOf([DeadComponent::class])
class AiSystem(
    private val aiComponents : ComponentMapper<AiComponent>,
) : IteratingSystem() {
    /**
     * Por cada entidad que esta en el sistema hace funcionar el comportamiento de la IA
     *
     * @param entity Entidad a ejecutar
     */
    override fun onTickEntity(entity: Entity) {
        with(aiComponents[entity]){
            behaviorTree.step()
        }
    }
}