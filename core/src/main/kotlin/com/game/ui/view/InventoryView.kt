package com.game.ui.view

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.I18NBundle
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
    bundle: I18NBundle,
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


            this@InventoryView.lvl = label(text = bundle["InventoryLevel"] +"1", style = Labels.FRAME.skinKey, skin) {
                this.setAlignment(Align.center)

            }
            this@InventoryView.life = label(text = bundle["InventoryLife"]+ LIFE, style = Labels.FRAME.skinKey, skin) {
                this.setAlignment(Align.center)
                it.row()
            }
            this@InventoryView.attack = label(text = bundle["InventoryDamage"] + ATTACK, style = Labels.FRAME.skinKey, skin) {
                this.setAlignment(Align.center)

            }
            this@InventoryView.speed = label(text = bundle["InventorySpeed"] +SPEED, style = Labels.FRAME.skinKey, skin) {
                this.setAlignment(Align.center)
                it.row()
            }

            this@InventoryView.back = textButton(text = bundle["Atras"], style = Buttons.DEFAULT.skinKey) {
                onClick { stage.fire(HideInventoryEvent()) }
                it.right()

            }

            it.expand().fill().left().top()
        }


        // data binding

        model.onPropertyChange(GameModel::level) { level ->
            this@InventoryView.lvl.setText(bundle["InventoryLevel"] + level)
        }
        model.onPropertyChange(GameModel::life) { life ->
            this@InventoryView.life.setText(bundle["InventoryLife"] + life )
        }
        model.onPropertyChange(GameModel::speed) { speed ->
            this@InventoryView.speed.setText(bundle["InventorySpeed"] + speed)
        }
        model.onPropertyChange(GameModel::attack) { attack ->
            this@InventoryView.attack.setText(bundle["InventoryDamage"] + attack)
        }

    }
}

@Scene2dDsl
        /**
         * Extension del constructor para poder añadirla como actor al escenario
         */
fun <S> KWidget<S>.inventoryView(
    model: GameModel,
    bundle: I18NBundle,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: InventoryView.(S) -> Unit = {}
): InventoryView = actor(InventoryView(model, bundle,skin), init)
