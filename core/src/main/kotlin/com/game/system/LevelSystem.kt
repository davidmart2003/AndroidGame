package com.game.system

import com.game.component.*
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem

@AllOf([PlayerComponent::class, LevelComponent::class])
class LevelSystem(
    private val lifeComponents: ComponentMapper<LifeComponent>,
    private val attackComponents: ComponentMapper<AttackComponent>,
    private val levelComponents: ComponentMapper<LevelComponent>,

    ) : IteratingSystem() {
    override fun onTickEntity(entity: Entity) {
        val lifeComponent = lifeComponents[entity]
        var levelComponent = levelComponents[entity]
        var attackComponent = attackComponents[entity]

        if (levelComponent.baseExp < lifeComponent.exp) {
            levelComponent.baseExp = levelComponent.baseExp * 1.5f
            levelComponent.level++
            attackComponent.damage = attackComponent.damage + DEFAULT_ATTACK_DAMAGE.toInt() + 2;
            lifeComponent.maxLife = lifeComponent.maxLife + 5

        }
    }
}