package com.game.ui.view

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.game.event.InventoryEvent
import com.game.event.PauseEvent
import com.game.event.fire
import com.game.model.GameModel
import com.game.system.LifeSystem
import com.game.widget.*
import ktx.actors.alpha
import ktx.actors.onClick
import ktx.actors.plusAssign
import ktx.actors.txt
import ktx.log.logger
import ktx.scene2d.*


class GameView(
    model: GameModel,
    skin: Skin
) : Table(skin), KTable {

    private val enemyInfo: CharacterInfo
    private val playerInfo: CharacterInfo

    private val controller: Controller
    private val popupLabel: Label
    private var pause: Image
    private var inventory: Image
    private lateinit var Pause: Pause
    private lateinit var Dead: Dead
    private lateinit var Win: Win

    init {
        // UI
        setFillParent(true)

        table {

            this@GameView.playerInfo = characterInfo(Drawables.PLAYER, skin) {
                it.expand().top().left()
            }

            this@GameView.enemyInfo = characterInfo(null, skin) {
                this.alpha = 0f
                it.expand().top().left()
            }


            this@GameView.inventory = image(skin[Drawables.FRAME_BGD]) {
                onClick { stage.fire(InventoryEvent()) }
                it.top().left()
                it.padTop(5f)
                it.padRight(50f)

            }

            this@GameView.pause = image(skin[Drawables.PAUSE]) {
                onClick {


                    stage.fire(PauseEvent())
                }
                it.right().top().row()
            }

            it.expand().fill().row()
        }

        table {

            this@GameView.popupLabel = label(text = "", style = Labels.FRAME.skinKey) {
                it.row()
            }
            this.alpha = 0f
            it.expand().fill().row()
        }



        this@GameView.controller = controller(Drawables.DOWN, skin) {
            it.expand().left().bottom()
        }


        // data binding
        model.onPropertyChange(GameModel::level) { level ->
            levelUp(level)
        }
        model.onPropertyChange(GameModel::playerLife) { lifePercentage ->
            playerLife(lifePercentage)
        }
        model.onPropertyChange(GameModel::enemyLife) { lifePercentage ->
            enemyLife(lifePercentage)
        }
        model.onPropertyChange(GameModel::lootText) { lootInfo ->
            popup(lootInfo)
        }
        model.onPropertyChange(GameModel::enemyType) { type ->
            when (type) {
                "FlyingEye" -> showEnemyInfo(Drawables.FLYINGEYE, model.enemyLife)
                else -> showEnemyInfo(null, 1f)
            }

        }
    }

    fun death() {
        this.clear()
        Dead = deadUp(skin) {
            it.expand().center()
        }
        this += Dead
    }
    fun win() {
        this.clear()
        Win = winUp(skin) {

        }
        this += Win
    }
    fun pause() {
        this.clear()
        Pause = pauseUp(skin) {
            it.expand().center()

        }
        this += Pause
    }


    fun resume() {
        this.clear()
        this += playerInfo
        this += pause
        this += enemyInfo
        this += controller
    }

    fun playerLife(percentage: Float) = playerInfo.life(percentage)

    fun levelUp(level: Int) = playerInfo.level(level)
    private fun Actor.resetFadeOutDelay() {
        this.actions
            .filterIsInstance<SequenceAction>()
            .lastOrNull()
            ?.let { sequence ->
                val delay = sequence.actions.last() as DelayAction
                delay.time = 0f
            }
    }

    fun showEnemyInfo(charDrawable: Drawables?, lifePercentage: Float) {
        enemyInfo.character(charDrawable)
        enemyInfo.life(lifePercentage, 0f)

        if (charDrawable != null) {
            enemyInfo.alpha = 1f
        } else {
            enemyInfo.alpha = 0f
        }
    }

    fun enemyLife(percentage: Float) = enemyInfo.also { it.resetFadeOutDelay() }.life(percentage)


    fun popup(infoText: String) {
        popupLabel.txt = infoText
        if (popupLabel.parent.alpha == 0f) {
            popupLabel.parent.clearActions()
            popupLabel.parent += sequence(fadeIn(0.2f), delay(4f, fadeOut(0.75f)))
        } else {
            popupLabel.parent.resetFadeOutDelay()
        }
    }

    companion object {
        private val log = logger<LifeSystem>()
    }
}


@Scene2dDsl
fun <S> KWidget<S>.gameView(
    model: GameModel,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: GameView.(S) -> Unit = {}
): GameView = actor(GameView(model, skin), init)