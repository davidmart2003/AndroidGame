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
class TimeSystem (
    @Qualifier("gameStage") private val stage : Stage,
    private val timeComponent : ComponentMapper<TimeComponent>,
    private val playerComponent : ComponentMapper<PlayerComponent>
): IteratingSystem() {


    override fun onTickEntity(entity: Entity) {
        timeComponent[entity].time+=deltaTime
        playerComponent[entity].actualTime+=deltaTime
        stage.fire(TimeEvent(entity))
        log.debug { timeComponent[entity].time.toString() }
    }

    companion object {
        private val log = logger<TimeSystem>()
    }
}