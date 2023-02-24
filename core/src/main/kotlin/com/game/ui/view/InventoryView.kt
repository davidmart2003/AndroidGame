package com.game.ui.view

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.Align
import com.game.MyGame.Companion.ATTACK
import com.game.MyGame.Companion.LIFE
import com.game.MyGame.Companion.SPEED
import com.game.event.HideInventoryEvent
import com.game.event.fire
import com.game.model.GameModel
import com.game.widget.PlayerStats
import com.game.widget.playerStats
import ktx.actors.onClick
import ktx.actors.plusAssign
import ktx.scene2d.*

/**
 * Vista de las estadisticas del jugador
 *
 * @property model Modelo del juego que actualiza y notifica cualquier cambio
 * @property skin Skin de los componentes
 */
class InventoryView(
    model: GameModel,
    skin: Skin
) : KTable, Table(skin) {
    /**
     * Etiquetas de textos
     */
    var lvl: Label
    var life: Label
    var speed: Label
    var attack: Label

    /**
     * componente de texto que funciona de boton
     */
    var back: TextButton


    init {
        // UI
        setFillParent(true)

        table {
            background = skin[Drawables.FRAME_BGD]


            this@InventoryView.lvl = label(text = "Your Level: 1", style = Labels.FRAME.skinKey, skin) {
                this.setAlignment(Align.center)

            }
            this@InventoryView.life = label(text = "Your Max Life: $LIFE", style = Labels.FRAME.skinKey, skin) {
                this.setAlignment(Align.center)
                it.row()
            }
            this@InventoryView.attack = label(text = "Your total damage: $ATTACK", style = Labels.FRAME.skinKey, skin) {
                this.setAlignment(Align.center)

            }
            this@InventoryView.speed = label(text = "Your actual speed : $SPEED", style = Labels.FRAME.skinKey, skin) {
                this.setAlignment(Align.center)
                it.row()
            }

            this@InventoryView.back = textButton(text = "Back", style = Buttons.DEFAULT.skinKey) {
                onClick { stage.fire(HideInventoryEvent()) }
                it.right()

            }

            it.expand().fill().left().top()
        }


        // data binding

        model.onPropertyChange(GameModel::level) { level ->
            this@InventoryView.lvl.setText("Your level: $level")
        }
        model.onPropertyChange(GameModel::life) { life ->
            this@InventoryView.life.setText("Actual Life: $life hp")
        }
        model.onPropertyChange(GameModel::speed) { speed ->
            this@InventoryView.speed.setText("Actual speed: $speed")
        }
        model.onPropertyChange(GameModel::attack) { attack ->
            this@InventoryView.attack.setText("Actual damage $attack")
        }

    }
}

@Scene2dDsl
        /**
         * Extension del constructor para poder añadirla como actor al escenario
         */
fun <S> KWidget<S>.inventoryView(
    model: GameModel,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: InventoryView.(S) -> Unit = {}
): InventoryView = actor(InventoryView(model, skin), init)
