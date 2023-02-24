package com.game.ai

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.Stage
import com.game.component.*
import com.game.event.fire
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Qualifier
import com.github.quillraven.fleks.World
import ktx.math.component1
import ktx.math.component2
import ktx.math.vec2

/**
 * Rectangulo para hacer la colision de daño
 */
private val TMP_RECT = Rectangle()

/**
 * Conjuntos de las entidades con inteligencia artificial
 *
 * @property entity Entidad a ejecutar
 * @property world Mundo de entidades
 * @property stage Escenario donnde se renderiza el juego
 * @property animationComponents Conjuntos de entidades que contiene AnimationComponent
 * @property playerComponents Conjuntos de entidades que contiene PlayerComponent
 * @property moveComponents Conjuntos de entidades que contiene MoveComponent
 * @property attackComponent Conjuntos de entidades que contiene AttackComponent
 * @property stateComponents Conjuntos de entidades que contiene StateComponent
 * @property physicComponents Conjuntos de entidades que contiene PhysicComponent
 * @property aiComponents Conjuntos de entidades que contiene AiComponent
 * @property lifeComponents Conjuntos de entidades que contiene LifeComponent
 */
data class AiEntity(
    val entity: Entity,
    private val world: World,
    private val stage:Stage,
    private val animationComponents: ComponentMapper<AnimationComponent> = world.mapper(),
    private val moveComponents: ComponentMapper<MoveComponent> = world.mapper(),
    private val attackComponents: ComponentMapper<AttackComponent> = world.mapper(),
    private val stateComponents: ComponentMapper<StateComponent> = world.mapper(),
    private val physicComponents: ComponentMapper<PhysicComponent> = world.mapper(),
    private val aiComponents: ComponentMapper<AiComponent> = world.mapper(),
    private val lifeComponents: ComponentMapper<LifeComponent> = world.mapper(),
    private val playerComponents: ComponentMapper<PlayerComponent> = world.mapper(),
) {

    /**
     * Localizacion  del cuerpo de la entidad
     */
    val location: Vector2
        get() = physicComponents[entity].body.position

    /**
     * True si la entidad quiere atacar
     */
    val wantsToAttack: Boolean
        get() = attackComponents.getOrNull(entity)?.doAttack ?: false

    /**
     * True si la entidad esta recibiendo daño
     */
    val takehit: Boolean
        get() = lifeComponents.getOrNull(entity)?.isTakingDamage ?: false

    /**
     * True si la entidas quiera correr
     */
    val wantsToRun: Boolean
        get() {
            val moveComponent = moveComponents[entity]
            return moveComponent.cos != 0f || moveComponent.sin != 0f
        }

    val attackComponent: AttackComponent
        get() = attackComponents[entity]

    /**
     * True si la animacion ha finalizado
     */
    val isAnimationDone: Boolean
        get() = animationComponents[entity].isAnimationFinished()

    /**
     * True si la entidad esta muerta
     */
    val isDead: Boolean
        get() = lifeComponents[entity].isDead

    /**
     * Cambia la animacion de la entidad
     *
     * @param type Accion de la animacion a hacer
     * @param mode Modo de la animacion
     * @param resetAnimation Si la animacion se reinicia
     */
    fun animation(type: AnimationState, mode: PlayMode = PlayMode.LOOP, resetAnimation: Boolean = false) {
        with(animationComponents[entity]) {
            nextAnimation(type)
            this.mode = mode

            if (resetAnimation) {
                stateTime = 0f
            }
        }
    }

    /**
     * Funcion que sigue al jugador principal en el ejeX
     *
     * @return Devuelve la posicion del cuerpo del jugador ejeX
     */
    fun followPlayerX(): Float {
        val playerEntities = world.family(allOf = arrayOf(PlayerComponent::class))
        playerEntities.forEach { player ->
            with(physicComponents[player]) {
                return this.body.position.x
            }
        }
        return 0f
    }

    /**
     * Funcion que sigue al jugador principal en el ejeY
     *
     * @return Devuelve la posicion del cuerpo del jugador ejeY
     */
    fun followPlayerY(): Float {
        val playerEntities = world.family(allOf = arrayOf(PlayerComponent::class))
        playerEntities.forEach { player ->
            with(physicComponents[player]) {
                return this.body.position.y
            }
        }
        return 0f
    }

    /**
     * Estado en el que se encuentra la entidad
     *
     * @param next Estado de la entidad
     * @param inmediateChange Si el mestado se cambiad inmediatamente
     */
    fun state(next: EntityState, inmediateChange: Boolean = false) {
        with(stateComponents[entity]) {
            nextState = next

            if (inmediateChange) {
                stateMachine.changeState(nextState)
            }
        }
    }

    /**
     * Funcion que cambia al estado anterior de la entidad
     */
    fun changeToPreviousState() {
        with(stateComponents[entity]) {
            nextState = (stateMachine.previousState)
        }
    }

    /**
     * Funcion que cambia el root de la entidad
     *
     * @param enabled True si activas el root, false sino
     */
    fun root(enabled: Boolean) {
        with(moveComponents[entity]) {
            root = enabled
        }
    }

    /**
     * Funcion para empezaer un ataque
     */
    fun startAttack() {
        with(attackComponents[entity]) {
            startAttack()
        }
    }

    /**
     * Funcion para hacer y empezar un ataque
     */
    fun doAndStartAttack() {
        with(attackComponents[entity]) {
            doAttack = true
            startAttack()
        }
    }

    /**
     * Si esta true el parametro, pone el estado global a un estado default sino pone el estado nulo
     *
     * @param enable True si estado globar es Default, flase si es nulo
     */
    fun enableGlobalState(enable: Boolean) {
        with(stateComponents[entity]) {
            if (enable) {
                stateMachine.globalState = DefaultGlobalState.CHECK_ALIVE
            } else {
                stateMachine.globalState = null
            }
        }
    }

    /**
     * Fucnion que mueve la entidad a la posicion deseada
     *
     * @param target Posicion deseada
     */
    fun moveTo(target: Vector2) {
        val (targetX, targetY) = target
        val physicComponent = physicComponents[entity]
        val (sourceX, sourceY) = physicComponent.body.position
        with(moveComponents[entity]) {

            val angleRad = MathUtils.atan2(targetY - sourceY, targetX - sourceX)
            cos = MathUtils.cos(angleRad)
            sin = MathUtils.sin(angleRad)
        }
    }

    /**
     * Funcion que crea un rectagunlo con colision si esta en el rango deseado
     *
     * @param range Rango maximo para crear el rectangulo
     * @param target Posicion deseada
     */
    fun inRange(range: Float, target: Vector2): Boolean {
        val physicComponent = physicComponents[entity]
        val (sourceX, sourceY) = physicComponent.body.position
        val (offsetX, offsetY) = physicComponent.offset
        var (sizeX, sizeY) = physicComponent.size
        sizeX += range
        sizeY += range

        TMP_RECT.set(
            sourceX + offsetX - sizeX * 0.5f,
            sourceY + offsetY - sizeY * 0.5f,
            sizeX + offsetX,
            sizeY + offsetY

        )

        return TMP_RECT.contains(target)
    }

    /**
     * Funcion que para el movimiento de la entidad
     */
    fun stopMovement() {
        with(moveComponents[entity]) {
            cos = 0f
            sin = 0f
        }
    }

    /**
     * True si la entidad puede atacar
     */
    fun canAttack(): Boolean {
        val attackComponent = attackComponents[entity]

        if (!attackComponent.isReady) {
            return false
        }

        val enemy = nearbyEnemies().firstOrNull() ?: return false

        val enemyPhysicComponent = physicComponents[enemy]
        val (sourceX, sourceY) = enemyPhysicComponent.body.position
        val (offsetX, offsetY) = enemyPhysicComponent.offset
        return inRange(1.5f + attackComponent.extraRange, vec2(sourceX + offsetX, sourceY + offsetY))
    }

    /**
     * funcion que mira si la IA tiene enemeigos cerca, en este caso si el jugador esta cerca
     *
     * @return Lista de las entidad que estan cerca
     */
    private fun nearbyEnemies(): List<Entity> {
        val aiComponent = aiComponents[entity]
        return aiComponent.nearbyEntities
            .filter { it in playerComponents && !lifeComponents[it].isDead }
    }

    /**
     * Funmcion que mira si la lista de enemigos cercanos esta vacia, devuelve true si la lista no esta vacia
     */
    fun hasEnemyNearby() = nearbyEnemies().isNotEmpty()

    /**
     * Funcion para lanzar eventos
     *
     * @param event Evento a lanzar
     */
    fun fireEvent(event : Event) {
        stage.fire(event)
    }


}