package com.game.system

import com.badlogic.gdx.scenes.scene2d.Stage
import com.game.component.*
import com.game.event.DeadEvent
import com.game.event.SpawnPortalEvent
import com.game.event.WinEvent
import com.game.event.fire
import com.game.widget.Win
import com.github.quillraven.fleks.*
import ktx.log.logger


/**
 * Sistema que se encanga de la muerte de las entidades
 *
 * @property stage Escenario donde se renderiza el juego
 * @property deadComponents Conjunto de entidades que contiene DeadComponent
 * @property playerComponents Conjunto de entidades que contiene PlayerComponent
 * @property enemyComponents Conjunto de entidades que contiene EnemyComponent
 */
@AllOf([DeadComponent::class])
class DeadSystem(
    @Qualifier("gameStage") private val stage: Stage,
    private val deadComponents: ComponentMapper<DeadComponent>,
    private val playerComponents: ComponentMapper<PlayerComponent>,
    private val enemyComponents: ComponentMapper<EnemyComponent>
) : IteratingSystem() {
    /**
     * Tiempo de espera
     */
    var cont: Float = 0f

    /**
     * Por cada entidad que este muerta la elimina del mundo de entidades
     */
    override fun onTickEntity(entity: Entity) {
        val deadComponent = deadComponents[entity]

        if (deadComponent.reviveTime == 0f) {
            return
        }

        deadComponent.reviveTime -= deltaTime
        if (entity in playerComponents) {

            cont += deltaTime
            if (cont > 3) {
                stage.fire(DeadEvent())
            }
        }

        if (entity in enemyComponents) {

            if (enemyComponents[entity].name == "Demon") {
                cont += deltaTime
                if (cont > 3) {
                    stage.fire(WinEvent())
                }
            } else {

                world.remove(entity)
            }
        }
    }

    companion object {
        private val log = logger<DeadSystem>()
    }
}