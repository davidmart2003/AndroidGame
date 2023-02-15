package com.game.widget

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.game.ui.view.get
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.badlogic.gdx.utils.Scaling
import com.game.ui.view.Drawables
import jdk.jfr.Percentage
import ktx.actors.plusAssign
import ktx.scene2d.*

class CharacterInfo(
    charDrawable: Drawables?,
    private val skin: Skin,

) : WidgetGroup(), KGroup {
    private val background: Image = Image(skin[Drawables.BACKGROUNDINFO])
    private val character: Image = Image(if (charDrawable == null) null else skin[charDrawable])
    private val lifeBar: Image = Image(skin[Drawables.LIFEBAR])
    private val fondo : Image = Image(skin[Drawables.FRAME_BGD])


    init {
        this +=fondo
        this += background
        this += character.apply {
            setPosition(32f, 70f)
            setSize(64f, 64f)
            setScaling(Scaling.contain)
        }
        this += lifeBar.apply {
            setPosition(21f, 16f)
            setSize(128f, 16f)
        }
    }


    override fun getPrefHeight() = background.drawable.minHeight

    override fun getPrefWidth() = background.drawable.minWidth

    fun character(charDrawable: Drawables?) {
        if (charDrawable == null) {
            character.drawable = null
        } else {
        character.drawable=skin[charDrawable]
        }
    }

    fun life(percentage: Float,duration:Float=0.75f){
        lifeBar.clearActions()
        lifeBar += Actions.scaleTo(MathUtils.clamp(percentage,0f,1f),1f,duration)
    }
}

@Scene2dDsl
fun <S> KWidget<S>.characterInfo(
    charDrawable: Drawables?,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: CharacterInfo.(S) -> Unit = {}
): CharacterInfo = actor(CharacterInfo(charDrawable, skin), init)