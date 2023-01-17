package com.game.Component

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable

enum class  AnimationType{
    char_blue_1,FlyingEye,Skeleton,Goblin,Mushroom,UNDEFINED;


}

enum class AnimationState{
    IDLE,RUN,DEATH,ATTACK,TAKEHIT,JUMP;

    val atlasKey:String = this.toString().lowercase()
}
data class AnimationComponent(
    var model:AnimationType=AnimationType.UNDEFINED,
    var stateTime:Float=0f,
    var playMode: Animation.PlayMode = Animation.PlayMode.LOOP
) {
    lateinit var animation : Animation<TextureRegionDrawable>
    var nextAnimation:String= NO_ANIMATION

    // atlasKey/AniimationStateLoweCAse
    //char_blue/iddle00
    //char_blue/iddle01
    //char_blue/iddle02
    fun nextAnimation(model: AnimationType, type: AnimationState){
        this.model=model
        nextAnimation="$model/${type.atlasKey}"
    }

    companion object{
        const val NO_ANIMATION=""
    }
}