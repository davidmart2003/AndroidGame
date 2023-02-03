package com.game.system

import com.game.component.EnemyComponent
import com.game.component.PlayerComponent
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import ktx.log.logger

@AllOf([EnemyComponent::class])
class SpawnPortalSystem(
) : IteratingSystem() {
    val enemies=world.family(allOf = arrayOf(EnemyComponent::class))


    override fun onTickEntity(entity: Entity) {



        log.debug {
            enemies.numEntities.toString()
        }
    }

    companion object {
        private val log = logger<LifeSystem>()
    }
}