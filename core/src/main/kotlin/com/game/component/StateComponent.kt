package com.game.component

import com.badlogic.gdx.ai.fsm.DefaultStateMachine
import com.badlogic.gdx.scenes.scene2d.Stage
import com.game.ai.AiEntity
import com.game.ai.DefaultState
import com.game.ai.EntityState
import com.github.quillraven.fleks.ComponentListener
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Qualifier
import com.github.quillraven.fleks.World

/**
 *
 */
data class StateComponent(
    var nextState: EntityState = DefaultState.IDLE,
    val stateMachine: DefaultStateMachine<AiEntity, EntityState> = DefaultStateMachine()

) {
    companion object {
        /**
         * Listener que actúa cada vez el componente es añadido
         *
         * @property world Mundo de entidades
         * @property stage Escenario donde se renderiza el juego
         */
        class StateComponentListener(
            private val world: World,
            @Qualifier("gameStage") private val stage: Stage
        ) : ComponentListener<StateComponent>{
            /**
             *  Cuando el componente es añadido, el dueño stateMachine pasa a ser la AIEntity
             *
             *  @param entity Entidad que fue añadido al componente
             *  @param component Tipo de componente
             */
            override fun onComponentAdded(entity: Entity, component: StateComponent) {
                component.stateMachine.owner = AiEntity(entity,world,stage)
            }

            /**
             *
             */
            override fun onComponentRemoved(entity: Entity, component: StateComponent) =Unit

        }


    }
}