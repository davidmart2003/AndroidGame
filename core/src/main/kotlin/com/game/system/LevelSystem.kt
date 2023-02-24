package com.game.system

import com.badlogic.gdx.scenes.scene2d.Stage
import com.game.component.*
import com.game.event.*
import com.github.quillraven.fleks.*

/**
 * Sistema que se encargar del sistema de niveles
 *
 * @property stage Escenario donde se renderiza el juego
 * @property lifeComponents Conjuntos de entidades que contiene LifeComponent
 * @property attackComponents Conjuntos de entidades que contiene AttackComponent
 * @property levelComponents Conjuntos de entidades que contiene LevelComponent
 * @property playerComponents Conjuntos de entidades que contiene PlayerComponent
 * @property moveComponents Conjuntos de entidades que contiene MoveComponent
 */
@AllOf([PlayerComponent::class, LevelComponent::class])
class LevelSystem(
    @Qualifier("gameStage") private val stage: Stage,
    private val lifeComponents: ComponentMapper<LifeComponent>,
    private val attackComponents: ComponentMapper<AttackComponent>,
    private val levelComponents: ComponentMapper<LevelComponent>,
    private val playerComponents: ComponentMapper<PlayerComponent>,
    private val moveComponents: ComponentMapper<MoveComponent>

) : IteratingSystem() {

    /**
     *  Por cada entidad sube de nivel segun la expereincia que este tenga
     *  Cada vez que sube de nivel le sube las estadísticas
     */
    override fun onTickEntity(entity: Entity) {
        val lifeComponent = lifeComponents[entity]
        var levelComponent = levelComponents[entity]
        var attackComponent = attackComponents[entity]
        val playerComponents = playerComponents[entity]
        val moveComponents = moveComponents[entity]
        if (levelComponent.baseExp < lifeComponent.exp) {
            lifeComponent.exp = 0
            levelComponent.baseExp = levelComponent.baseExp * 1.5f
            playerComponents.actualBasexp = levelComponent.baseExp
            levelComponent.level++
            playerComponents.actualLevel = levelComponent.level.toFloat()
            // stage.fire(SpeedEven    t())
            attackComponent.damage = attackComponent.damage + DEFAULT_ATTACK_DAMAGE.toInt() + 2;
            playerComponents.actualStrenght = attackComponent.damage.toFloat()
            lifeComponent.maxLife = lifeComponent.maxLife + 5
            stage.fire(LevelUpEvent(levelComponent.level))
            stage.fire(AttackEvent(attackComponent.damage.toFloat()))
            stage.fire(SpeedEvent(moveComponents.speed))
            stage.fire(EntityDamageEvent(entity))
        }
    }
}