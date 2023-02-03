package com.game.component

import com.github.quillraven.fleks.Entity

class EnemyComponent(
) {
    val enemies:MutableList<Entity>
        get() = mutableListOf()



    fun addEnemies(entity: Entity) {
        enemies.add(entity)
    }
}