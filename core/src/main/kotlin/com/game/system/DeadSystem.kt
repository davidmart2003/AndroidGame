package com.game.system

import com.badlogic.gdx.scenes.scene2d.Stage
import com.game.component.DeadComponent
import com.game.component.EnemyComponent
import com.game.component.LifeComponent
import com.game.component.StateComponent
import com.game.event.SpawnPortalEvent
import com.game.event.fire
import com.github.quillraven.fleks.*


@AllOf([DeadComponent::class])
class DeadSystem(
    @Qualifier("gameStage") private val stage: Stage,
    private val deadComponents: ComponentMapper<DeadComponent>,
    private val lifeComponent: ComponentMapper<LifeComponent>,
    private val stateComponents : ComponentMapper<StateComponent>
) : IteratingSystem() {
    val enemies = world.family(allOf = arrayOf(EnemyComponent::class))

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