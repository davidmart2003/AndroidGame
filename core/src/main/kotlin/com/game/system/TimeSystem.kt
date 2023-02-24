package com.game.system

import com.badlogic.gdx.scenes.scene2d.Stage
import com.game.component.DeadComponent
import com.game.component.PlayerComponent
import com.game.component.TimeComponent
import com.game.event.TimeEvent
import com.game.event.fire
import com.github.quillraven.fleks.*
import ktx.log.logger

@AllOf([PlayerComponent::class])
@NoneOf([DeadComponent::class])
/**
 * Sistema que se encarga de tener un medidor de tiempo
 *
 * @property stage Escenario donde se renderiza el juego
 * @property timeComponent Conjunto de entidades que contine timeComponent
 * @property playerComponent Conjunto de entidades que contiene PlayerComponet
 */
class TimeSystem (
    @Qualifier("gameStage") private val stage : Stage,
    private val timeComponent : ComponentMapper<TimeComponent>,
    private val playerComponent : ComponentMapper<PlayerComponent>
): IteratingSystem() {

    /**
     * Cada vezx que se ejecuta suma al timer deltaTime , que es el timepo de ejecucion de la funcion
     *
     * @param entity Entidad a ejecutar
     */
    override fun onTickEntity(entity: Entity) {
        timeComponent[entity].time+=deltaTime
        playerComponent[entity].actualTime+=deltaTime
        stage.fire(TimeEvent(entity))
    }

    companion object {
        private val log = logger<TimeSystem>()
    }
}