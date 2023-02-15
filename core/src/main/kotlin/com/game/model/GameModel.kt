package com.game.model

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.game.component.AnimationComponent
import com.game.component.LifeComponent
import com.game.component.PlayerComponent
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.World
import org.graalvm.compiler.hotspot.replacements.ObjectSubstitutions.notify


class GameModel(
    world: World,
    stage: Stage,
): EventListener{

    private val playerComponents: ComponentMapper<PlayerComponent> = world.mapper()
    private val lifeComponents: ComponentMapper<LifeComponent> = world.mapper()
    private val animationComponents: ComponentMapper<AnimationComponent> = world.mapper()

    var playerLife=1f
    private set(value) {
       // notify(::playerLife,value)
        field=value
    }
    var enemyLife=1f
    var lootText=""

    override fun handle(event: Event?): Boolean {
        playerLife=2f
        return true
    }

}