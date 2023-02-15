package com.game.event

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.Entity


fun Stage.fire(event: Event){
    this.root.fire(event)
}
data class MapChangeEvent (
    val map:TiledMap
) : Event() {
}
class SpawnPortalEvent()
 : Event() {
}


class EntityLootEvent(): Event(){

}
class EntityDamageEvent(val entity: Entity): Event(){

}
class EntityAggroEvent(val entity: Entity): Event(){

}