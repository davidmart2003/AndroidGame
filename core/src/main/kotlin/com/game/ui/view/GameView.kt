package com.game.ui.view

import com.badlogic.gdx.Preferences
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
    recordPref : Preferences,
    skin: Skin
) : Table(skin), KTable {
    private val recordPref = recordPref
    private val enemyInfo: CharacterInfo
    private val playerInfo: CharacterInfo
    private val time : Label
     val controller: Controller
    private val popupLabel: Label
    private var pause: Image
    private var inventory: Image
    private var table1 : Table
    private var table2 : Table
    private var timeGame: Int = 0
    private lateinit var Pause: Pause
    private lateinit var Dead: Dead
    private lateinit var Win: Win

    init {
        // UI
        setFillParent(true)

       table1= table {

            this@GameView.playerInfo = characterInfo(Drawables.PLAYER, skin) {
                it.expand().top().left()
            }

            this@GameView.enemyInfo = characterInfo(null, skin) {
                this.alpha = 0f
                it.expand().top().left()
            }

            this@GameView.time = label(text = this@GameView.timeGame.toString(), style = Labels.LEVEL.skinKey ){
                this.setFontScale(2f)
                it.expand().top().right()
                it.padTop(20f)
                it.padRight(10f)

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

        table2= table {

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
        model.onPropertyChange(GameModel::time){time ->
            updateLabeltime(time)
            this@GameView.timeGame=time

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
        Dead = deadUp(this@GameView.recordPref,skin) {
            this.time(time=this@GameView.timeGame)
            it.expand().center()
        }
        this += Dead
    }
    fun win() {
        this.clear()
        Win = winUp(this@GameView.recordPref,skin) {
        this.time(time=this@GameView.timeGame)

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
        this += table1
        this +=table2
        this +=  controller(Drawables.DOWN, skin) {
            it.expand().left().bottom()
        }
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

    fun updateLabeltime(time : Int){
        this.time.setText(time)
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
    recordPref: Preferences,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: GameView.(S) -> Unit = {}
): GameView = actor(GameView(model,recordPref, skin), init)