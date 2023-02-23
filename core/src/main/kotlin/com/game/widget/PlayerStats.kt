package com.game.widget

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.badlogic.gdx.utils.Scaling
import com.game.ui.view.Drawables
import com.game.ui.view.Labels
import com.game.ui.view.get
import ktx.actors.plusAssign
import ktx.scene2d.*

class PlayerStats(
    ) : WidgetGroup(), KGroup {
    val lblLvl : com.badlogic.gdx.scenes.scene2d.ui.Label
    val lblLife : com.badlogic.gdx.scenes.scene2d.ui.Label
    val lblSpeed : com.badlogic.gdx.scenes.scene2d.ui.Label
    val lblAttack : com.badlogic.gdx.scenes.scene2d.ui.Label

    init {

        lblLvl = label("", Labels.LEVEL.skinKey).apply {
            setPosition(64f,116f)
        }

        this +=lblLvl
        lblLife  = label("", Labels.LEVEL.skinKey).apply {
            setPosition(64f,116f)
        }

        this +=lblLife
        lblAttack = label("", Labels.LEVEL.skinKey).apply {
            setPosition(64f,116f)
        }

        this +=lblAttack

        lblSpeed = label("", Labels.LEVEL.skinKey).apply {
            setPosition(64f,116f)
        }
        this +=lblSpeed
    }


    fun level(lvl: Int) = lblLvl.setText(lvl.toString())
    fun totalDamage(dmg : Float)=lblAttack.setText(dmg.toString())
    fun totalLife(life : Float)=lblLife.setText(life.toString())
    fun totalSpeed(speed : Float)=lblSpeed.setText(speed.toString())
}

@Scene2dDsl
fun <S> KWidget<S>.playerStats(
    init: PlayerStats.(S) -> Unit = {}
): PlayerStats = actor(PlayerStats(), init)