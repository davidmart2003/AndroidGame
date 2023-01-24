package com.game.ai

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.game.component.*
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import ktx.math.component1
import ktx.math.component2
import ktx.math.vec2

private val TMP_RECT=Rectangle()

data class AiEntity(
    val entity: Entity,
    val world: World,
    private val animationComponents : ComponentMapper<AnimationComponent> = world.mapper(),
    private val moveComponents : ComponentMapper<MoveComponent> = world.mapper(),
    private val attackComponents : ComponentMapper<AttackComponent> = world.mapper(),
    private val stateComponents : ComponentMapper<StateComponent> = world.mapper(),
    private val physicComponents : ComponentMapper<PhysicComponent> = world.mapper(),
    private val aiComponents : ComponentMapper<AiComponent> = world.mapper(),
    private val lifeComponents : ComponentMapper<LifeComponent> = world.mapper(),
    private val playerComponents : ComponentMapper<PlayerComponent> = world.mapper(),
){

    val location : Vector2
        get() = physicComponents[entity].body.position
    val wantsToAttack: Boolean
        get() =attackComponents.getOrNull(entity)?.doAttack ?:false

    val wantsToRun : Boolean
        get()  {
            val moveComponent = moveComponents[entity]
            return moveComponent.cos!=0f || moveComponent.sin!=0f
        }

  val isDead:Boolean
       get()=lifeComponents[entity].isDead

    val attackComponent : AttackComponent
        get()=attackComponents[entity]

    val isAnimationDone:Boolean
        get()=animationComponents[entity].isAnimationDone
    fun animation(type:AnimationState,mode : PlayMode=PlayMode.LOOP,resetAnimation:Boolean = false){
        with(animationComponents[entity]){
            nextAnimation(type)
            this.playMode=mode
            if(resetAnimation){
                stateTime=0f
            }
        }
    }

    fun enableGlobalState(enable:Boolean){
        with(stateComponents[entity]){
            if(enable){
                stateMachine.globalState=DefaultGlobalState.CHECK_ALIVE
            }else{
                stateMachine.globalState=null
            }
        }
    }

    fun state (next: EntityState,inmediateChange:Boolean=false){
        with(stateComponents[entity]){
            nextState=next
            if(inmediateChange){
                stateMachine.changeState(nextState)
            }
        }
    }

    fun root(enabled:Boolean){
        with(moveComponents[entity]){
            root=enabled
        }
    }
    fun startAttack(){
        with(attackComponents[entity]){
            startAttack()
        }
    }

    fun doAndStartAttack(){
        with(attackComponents[entity]){
            doAttack=true
            startAttack()
        }
    }

    fun changeToPreviousState() {
        with(stateComponents[entity]){
            nextState = (stateMachine.previousState)
        }
    }

    fun moveTo(target : Vector2){
        val(targetX,targetY) = target
        val physicComponent = physicComponents[entity]
        val ( sourceX,sourceY) = physicComponent.body.position
        with(moveComponents[entity]){
            val angleRad= MathUtils.atan2(targetY-sourceY,targetX-sourceX)
            cos = MathUtils.cos(angleRad)
            sin = MathUtils.sin(angleRad)
        }
    }

    fun inRange(range:Float , target : Vector2): Boolean {
        val physicComponent = physicComponents[entity]
        val (sourceX,sourceY) = physicComponent.body.position
        val ( offsetX,offsetY) = physicComponent.offset
        var (sizeX,sizeY) = physicComponent.size
        sizeX +=range
        sizeY += range

        TMP_RECT.set(
            sourceX + offsetX - sizeX*0.5f,
            sourceY + offsetY - sizeY*0.5f,
            sizeX,
            sizeY

        )

        return  TMP_RECT.contains(target)
    }

    fun stopMovement(){
        with(moveComponents[entity]){
            cos=0f
            sin=0f
        }
    }

    fun canAttack(): Boolean {
        val attackComponent = attackComponents[entity]
        if(!attackComponent.isReady){
            return false
        }

        val enemy =nearbyEnemies().firstOrNull()
        if(enemy == null ){
            return false
        }
        val enemyphysicComponent = physicComponents[enemy]
        val (sourceX,sourceY) = enemyphysicComponent.body.position
        val ( offsetX,offsetY) = enemyphysicComponent.offset
        var (sizeX,sizeY) = enemyphysicComponent.size
        return inRange(1.5f+attackComponent.extraRange, vec2(sourceX+offsetX,sourceY+offsetY))
    }

    private fun nearbyEnemies(): List<Entity> {
        val aiComponent = aiComponents[entity]
        return aiComponent.nearbyEntities
            .filter { it in playerComponents && !lifeComponents[it].isDead }
    }
    fun hasEnemyNearby()= nearbyEnemies().isNotEmpty()
}