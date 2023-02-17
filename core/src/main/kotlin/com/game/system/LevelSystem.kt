package com.game.system

import com.badlogic.gdx.scenes.scene2d.Stage
import com.game.component.*
import com.game.event.EntityDamageEvent
import com.game.event.fire
import com.github.quillraven.fleks.*

@AllOf([PlayerComponent::class, LevelComponent::class])
class LevelSystem(
    @Qualifier("gameStage") private val stage: Stage,
    private val lifeComponents: ComponentMapper<LifeComponent>,
    private val attackComponents: ComponentMapper<AttackComponent>,
    private val levelComponents: ComponentMapper<LevelComponent>,
    private val playerComponents : ComponentMapper<PlayerComponent>

    ) : IteratingSystem() {
    override fun onTickEntity(entity: Entity) {
        val lifeComponent = lifeComponents[entity]
        var levelComponent = levelComponents[entity]
        var attackComponent = attackComponents[entity]
        val playerComponents = playerComponents[entity]
        if (levelComponent.baseExp < lifeComponent.exp) {
            levelComponent.baseExp = levelComponent.baseExp * 1.5f
            levelComponent.level++
            attackComponent.damage = attackComponent.damage + DEFAULT_ATTACK_DAMAGE.toInt() + 2;
            playerComponents.actualStrenght=attackComponent.damage.toFloat()
            lifeComponent.maxLife = lifeComponent.maxLife + 5
            stage.fire(EntityDamageEvent(entity))
        }
    }
}