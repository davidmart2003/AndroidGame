package com.game.system

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.game.MyGame
import com.game.ai.Action
import com.game.component.ImageComponent
import com.game.component.MoveComponent
import com.game.component.PhysicComponent
import com.game.component.PlayerComponent
import com.game.event.ButtonPressedEvent
import com.game.event.SpeedEvent
import com.game.event.fire
import com.github.quillraven.fleks.*
import ktx.actors.stage
import ktx.log.logger
import ktx.math.component1
import ktx.math.component2

/**
 * Sistema que se encarga del movimiento
 *
 * @property stage Escenario donde se renderiza el juego
 * @property moveComponents Conjunto de entidades que cojntiene MoveComponent
 * @property physicsComponents Conjunto de entidades que cojntiene PhysicComponent
 * @property imageComponents Conjunto de entidades que cojntiene ImageComponent
 * @property playerComponents Conjunto de entidades que cojntiene PlayerComponent
 */
@AllOf([MoveComponent::class, PhysicComponent::class])
class MoveSystem(
    @Qualifier("gameStage") private val stage : Stage,
    private val moveComponents: ComponentMapper<MoveComponent>,
    private val physicsComponents: ComponentMapper<PhysicComponent>,
    private val imageComponents: ComponentMapper<ImageComponent>,
    private val playerComponents: ComponentMapper<PlayerComponent>

) : IteratingSystem(), EventListener {

    /**
     * Direccion del Player en el eje X
     */
    private var playerCos: Float = 0f

    /**
     * Direccion del player en el eje Y
     */
    private var playerSin: Float = 0f

    /**
     * Por cada entidad genera un impulso para hacerlo mover en ambos ejes
     * Si es root se queda quieto
     *
     * @param entity Entidad a ejecutar
     */
    override fun onTickEntity(entity: Entity) {
        val moveComponent = moveComponents[entity]
        val physicComponent = physicsComponents[entity]
        val masa = physicComponent.body.mass
        val (velX, velY) = physicComponent.body.linearVelocity

        if (entity in playerComponents) {
          //  log.debug { "ACTUALIZANDO" }

            moveComponent.sin = playerSin
            moveComponent.cos = playerCos
            stage.fire(SpeedEvent(moveComponent.speed))
        }
        if (moveComponent.cos == 0f && moveComponent.sin == 0f || moveComponent.root) {
            physicComponent.impulse.set(
                masa * (0f - velX),
                masa * (0f - velY)
            )
            return
        }


        physicComponent.impulse.set(
            masa * (moveComponent.speed * moveComponent.cos - velX),
            masa * (moveComponent.speed * moveComponent.sin - velY)

        )

        imageComponents.getOrNull(entity)?.let { imageComponent ->
            if (moveComponent.cos != 0f) {
                imageComponent.image.flipX = moveComponent.cos < 0
            }
        }
    }

    /**
     * Se ejecuta cuando se lanza un evento
     *
     * @param event Evento lanzado
     */
    override fun handle(event: Event?): Boolean {
        when (event) {
            is ButtonPressedEvent -> {

                playerCos=event.cos
                playerSin=event.sin
            }

            else -> return false
        }
        return true
    }
    companion object {
        private val log = logger<MoveSystem>()
    }
}