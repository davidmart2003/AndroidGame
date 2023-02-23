package com.game.component

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ai.btree.BehaviorTree
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeParser
import com.badlogic.gdx.scenes.scene2d.Stage
import com.game.ai.AiEntity
import com.github.quillraven.fleks.ComponentListener
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Qualifier
import com.github.quillraven.fleks.World

/**
 * Componente que tiene cada entidad que requiera inteligencia artificial
 *
 * @property nearbyEntities Conjunto de las entitides
 * @property treePath Ruta del archivo .tree
 *
 * @constructor Contructor con valores default
 */
data class AiComponent (
    val nearbyEntities:MutableSet<Entity> = mutableSetOf(),
    var treePath : String ="",

    ){
    /**
     *  Comportamiento secuencial que va a seguir la IA
     */
    lateinit var behaviorTree : BehaviorTree<AiEntity>

    companion object {
        /**
         * Listener que actúa cada vez que una entidad se le añade el AIComponent
         * Coge el archivo con extensión ".tree" y la las funciones a seguir de la IA
         * Y crea el comportamiento con la clase BehaviorTree y el parser BehaviorTreeParser
         *
         * @property world El mundo que contiene las entidades
         * @property stage El escenario donde se esta renderizando el juego
         */
        class AiComponentListener(
            private val world: World,
           @Qualifier("gameStage") private val stage:Stage
        ): ComponentListener<AiComponent> {
            private val treeParser= BehaviorTreeParser<AiEntity>()

            /**
             * Cuando una entidad es añadido a AiComponent, convierte el archivo
             * ".tree" con sus funciones de IA al comportamineto de la IA
             *
             * @param entity Entidad que fue añadido al AIComponent
             * @param component El tipo de componente que fue añadido
             */
            override fun onComponentAdded(entity: Entity, component: AiComponent) {
                component.behaviorTree = treeParser.parse(
                   Gdx.files.internal(component.treePath),
                    AiEntity(entity, world,stage)
                )
            }

            override fun onComponentRemoved(entity: Entity, component: AiComponent) =Unit
        }
    }
}