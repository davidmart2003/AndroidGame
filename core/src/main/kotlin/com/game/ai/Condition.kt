package com.game.ai

import com.badlogic.gdx.ai.btree.LeafTask
import com.badlogic.gdx.ai.btree.Task

/**
 * Condiciones creadad para usar en el archivo con extension ".tree"
 */
abstract class Condition : LeafTask<AiEntity>() {
    val entity : AiEntity
        get()=`object`

    abstract fun condition(): Boolean

    /**
     * True si la ejeccuion ha sido hecha, false si ha fallado
     */
    override fun execute(): Status {
        return when {
            condition()-> Status.SUCCEEDED
            else -> Status.FAILED
        }
    }

    /**
     * Funcion que copia  a la accion actual
     *
     * @param task accion donde se copia a la actual
     */
    override fun copyTo(task: Task<AiEntity>?)=task
}

/**
 * Condicion del behaviortree, si la entidad puede atacar devuelve true
 */
class CanAttack : Condition(){
    override fun condition()= entity.canAttack()

}

/**
 * Condicion del behaviortree, si la entidad tiene un enemigo cerca devuelve true
 */
class IsEnemyNearby : Condition(){
    override fun condition()=entity.hasEnemyNearby()
}