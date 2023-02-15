package com.game.ui.view

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.game.model.GameModel
import com.game.widget.CharacterInfo
import com.game.widget.characterInfo
import ktx.actors.alpha
import ktx.actors.plusAssign
import ktx.actors.txt
import ktx.scene2d.*


class GameView(
    model: GameModel,
    skin: Skin,
) : Table(skin), KTable {

    private val playerInfo: CharacterInfo
    private val enemyInfo: CharacterInfo
    private val popupLabel: Label

    init {
        //UI
        setFillParent(true)
        playerInfo = characterInfo(Drawables.PLAYER) {
            it.top().left()

        }
        enemyInfo = characterInfo(Drawables.PLAYER) {
            this.alpha = 0f
            it.row()
            it.top()
        }


        table {
            background = skin[Drawables.FRAME_BGD]

            this@GameView.popupLabel = label("", style = Labels.FRAME.skinKey) { lblCell ->
                this.setAlignment(Align.topLeft)
                this.wrap = true
                lblCell.expand().fill().pad(4f)
            }
            this.alpha = 0f

            it.expand().width(330f).top().row()
        }


        //data binding
        model.onPropertyChange(model::playerLife) { playerLife ->
            playerLife(playerLife)
        }

        model.onPropertyChange(model::lootText) { lootText ->
            popup(lootText)
        }

        model.onPropertyChange(model::enemyLife) { enemyLife ->
            enemyLife(enemyLife)
        }

        model.onPropertyChange(model::enemyType) { enemyType ->
            when (enemyType) {
                "flyingeye" ->
                    showEnemyInfo(Drawables.FLYINGEYE, model.enemyLife)

            }
        }
    }

    private fun Actor.resetFadeOutDelay() {
        this.actions.filterIsInstance<SequenceAction>().lastOrNull()?.let { sequence ->
            val delay = sequence.actions.last() as DelayAction
            delay.time = 0f
        }
    }

    fun playerLife(percentage: Float) = playerInfo.life(percentage)
    fun enemyLife(percentage: Float) = enemyInfo.life(percentage)

    fun showEnemyInfo(drawables: Drawables, lifePercentage: Float) {
        enemyInfo.character(drawables)
        enemyInfo.life(lifePercentage, 0f)

        if (enemyInfo.alpha == 0f) {
            enemyInfo.clearActions()
            enemyInfo += Actions.sequence(
                Actions.fadeIn(1f, Interpolation.bounceIn),
                delay(5f, fadeOut(0.5f))
            )
        } else {
            popupLabel.parent.resetFadeOutDelay()
        }
    }

    fun popup(infoText: String) {
        popupLabel.txt = infoText

        popupLabel.parent.clearActions()
        popupLabel.parent += Actions.sequence(Actions.fadeIn(0.2f), delay(4f, fadeOut(0.75f)))
    }
}

@Scene2dDsl
fun <S> KWidget<S>.gameView(
    model: GameModel,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: GameView.(S) -> Unit = {}
): GameView = actor(GameView(model, skin), init)
