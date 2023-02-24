package com.game.system

import com.game.component.StateComponent
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem

@AllOf([StateComponent::class])
/**
 * Sistema que se encarga de los diferentes estados que puedan tener las entidasdes
 *
 * @property stateComponents Conjunto de entidades que contiene StateComponent
 */
class StateSystem (
    private val stateComponents: ComponentMapper<StateComponent>,
): IteratingSystem() {

    /**
     * Por cada entidad cambia el estado siguiente si es diferente al actual y lo actualiza
     */
    override fun onTickEntity(entity: Entity) {
        with(stateComponents[entity]){

            if(nextState!=stateMachine.currentState){
                stateMachine.changeState(nextState)
            }
                stateMachine.update()
        }
    }
}