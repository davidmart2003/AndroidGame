package com.game.ui.view

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.game.event.InventoryEvent
import com.game.event.PauseEvent
import com.game.event.fire
import com.game.model.GameModel
import com.game.model.InventoryModel
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
    //private var inventory: Image
    private lateinit var Pause: Pause
    private lateinit var Inventory: InventorySlot

    init {
        // UI
        setFillParent(true)
        playerInfo = characterInfo(Drawables.PLAYER, skin) {
            it.top().left()
        }

        enemyInfo = characterInfo(Drawables.FLYINGEYE, skin) {
            this.alpha = 0f
            it.top()
            it.padLeft(400f)
        }
        pause = image(skin[Drawables.PAUSE]) {
            onClick {
                log.debug { "ENTRO AQUI EN PAUSE" }

                stage.fire(PauseEvent())
            }
            it.top().right()
            it.padLeft(380f)
            //it.padRight(5f)

        }


//        inventory = image(skin[Drawables.FRAME_BGD]){
//            onClick { stage.fire(InventoryEvent()) }
//        }

        table {
            background = skin[Drawables.FRAME_BGD]

            this@GameView.popupLabel = label(text = "", style = Labels.FRAME.skinKey) { lblCell ->
                this.setAlignment(Align.topLeft)
                this.wrap = true
                lblCell.expand().fill().pad(14f)
            }

            this.alpha = 0f
            it.expand().width(130f).height(200f).top().row()
        }

        controller = controller(Drawables.DOWN, skin) {
            it.top().left().row()
        }


        // data binding
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
            //log.debug { "ENTRO QUI BROTYHER $type " }
            when (type) {
                "FlyingEye" -> showEnemyInfo(Drawables.FLYINGEYE, model.enemyLife)
                else -> showEnemyInfo(null, 1f)
            }

        }
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
        //log.debug { "·ENTrOOOO" }
        enemyInfo.character(charDrawable)
        enemyInfo.life(lifePercentage, 0f)

        if (charDrawable != null) {
            enemyInfo.alpha = 1f
//            // enemy info hidden -> fade it in
//            enemyInfo.clearActions()
//            enemyInfo += sequence(fadeIn(1f, Interpolation.bounceIn), delay(5f, fadeOut(0.5f)))
        } else {
            enemyInfo.alpha = 0f
//             enemy info already fading in -> just reset the fadeout timer. No need to fade in again
//            enemyInfo.resetFadeOutDelay()
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