package com.game.system

import com.game.component.EnemyComponent
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import ktx.log.logger

@AllOf([EnemyComponent::class])
class SpawnPortalSystem(
    private val enemyComponents: ComponentMapper<EnemyComponent>
):IteratingSystem() {
    override fun onTickEntity(entity: Entity) {
      //  enemyComponents[entity].enemies.add(entity)
        log.debug { enemyComponents[entity].enemies.size.toString() }
    }

    companion object {
        private val log = logger<LifeSystem>()
    }
}