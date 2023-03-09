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
import com.badlogic.gdx.utils.I18NBundle
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


/**
 * Vista del juego (gameplay)
 *
 * @property model Modelo del juego que actualiza y notifica cualquier cambio
 * @property recordPref Almacen de los records del juego
 * @property skin Skin de los componentes
 */
class GameView(
    model: GameModel,
    bundle:I18NBundle,
    recordPref: Preferences,
    skin: Skin
) : Table(skin), KTable {
    /**
     * Almacen de los records del juegos
     */
    private val recordPref = recordPref
    private val bundle=bundle
    /**
     * Componente de informacion del jugador
     */
    private val enemyInfo: CharacterInfo

    /**
     *  Componente de informacion del enemigo
     */
    private val playerInfo: CharacterInfo

    /**
     * Componente de etiqueta de texto
     */
    private val time: Label

    /**
     * Componente para controlar el movimiento y las habilidades del jugador
     */
    val controller: Controller
    private val popupLabel: Label

    /**
     * Componente de imagen para pausar el juego
     */
    private var pause: Image

    /**
     * Componente imagen para ver las estadísticas del jugador
     */
    private var inventory: Image

    /**
     * Tabla
     */
    private var table1: Table
    private var table2: Table

    /**
     * Tiempo de juego total
     */
    private var timeGame: Int = 0

    /**
     * Componente de pausa que se abre al pausar el juego
     */
    private lateinit var Pause: Pause

    /**
     * Componente de muerte, se abre cuando se muere en el juego
     */
    private lateinit var Dead: Dead

    /**
     * Componente de Win, se abre cuando se gana el juego
     */
    private lateinit var Win: Win

    init {
        // UI
        setFillParent(true)

        table1 = table {

            this@GameView.playerInfo = characterInfo(Drawables.PLAYER, skin) {
                it.expand().top().left()
            }

            this@GameView.enemyInfo = characterInfo(null, skin) {
                this.alpha = 0f
                it.expand().top().left()
            }

            this@GameView.time =
                label(text = this@GameView.timeGame.toString(), style = Labels.LEVEL.skinKey) {
                    this.setFontScale(2f)
                    it.expand().top().right()
                    it.padTop(20f)
                    it.padRight(10f)

                }
            this@GameView.inventory = image(skin[Drawables.FRAME_BGD]) {
                onClick { stage.fire(InventoryEvent()) }
                it.size(100f, 100f)

                it.top().left()
                it.padTop(5f)
                it.padRight(50f)

            }

            this@GameView.pause = image(skin[Drawables.PAUSE]) {
                it.size(100f, 100f)

                onClick {
                    stage.fire(PauseEvent())
                }
                it.padRight(30f)
                it.padTop(10f)
                it.right().top().row()
            }

            it.expand().fill().row()
        }

        table2 = table {

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
        model.onPropertyChange(GameModel::time) { time ->
            updateLabeltime(time)
            this@GameView.timeGame = time

        }
        model.onPropertyChange(GameModel::enemyType) { type ->
            when (type) {
                "FlyingEye" -> showEnemyInfo(Drawables.FLYINGEYE, model.enemyLife)
                "Skeleton" -> showEnemyInfo(Drawables.SKELETON, model.enemyLife)
                "Demon" -> showEnemyInfo(Drawables.DEMON, model.enemyLife)
                "Mushroom" -> showEnemyInfo(Drawables.MUSHROOM, model.enemyLife)
                "Goblin" -> showEnemyInfo(Drawables.GOBLIN, model.enemyLife)
                else -> showEnemyInfo(null, 1f)
            }

        }
    }

    /**
     * Funcion que al morir se despiega el menu
     */
    fun death() {
        this.clear()
        Dead = deadUp(this@GameView.recordPref,this@GameView.bundle, skin) {
            this.time(time = this@GameView.timeGame)
            it.expand().center()
        }
        this += Dead
    }

    /**
     * Funcion que al ganar se desplega el menuWin
     */
    fun win() {
        this.clear()
        Win = winUp(this@GameView.recordPref,this@GameView.bundle, skin) {
            this.time(time = this@GameView.timeGame)

        }
        this += Win

    }

    /**
     * Funcion que pausa y despliega el menu de pausa
     */
    fun pause() {
        this.clear()
        Pause = pauseUp(bundle,skin) {
            it.expand().center()

        }
        this += Pause
    }


    /**
     * Funcion que vuelve el juego y despliega de nuevo el gameplay
     */
    fun resume() {
        this.clear()
        this += table1
        this += table2
        this += controller(Drawables.DOWN, skin) {
            it.expand().left().bottom()
        }
    }

    /**
     * Funcion que actualiza la vida del personaje principal
     *
     * @param percentage Porcentaje de vida del jugador
     */
    fun playerLife(percentage: Float) = playerInfo.life(percentage)

    /**
     * Funcion que actualiza el nivel del perosnaje
     *
     * @param level Nivel del jugador
     */
    fun levelUp(level: Int) = playerInfo.level(level)

    /**
     * Resetea el delay de desvanecimiento para volver a usarlo
     */
    private fun Actor.resetFadeOutDelay() {
        this.actions
            .filterIsInstance<SequenceAction>()
            .lastOrNull()
            ?.let { sequence ->
                val delay = sequence.actions.last() as DelayAction
                delay.time = 0f
            }
    }

    /**
     * Actualiza la etiqueta de texto de tiempo
     *
     * @param time Tiempo  de juego
     */
    fun updateLabeltime(time: Int) {
        this.time.setText(time)
    }

    /**
     * Muestra la informacion de los enemigos
     *
     * @param charDrawable Modelo de enemigo a mostrar
     * @param lifePercentage Porcentaje de vida
     */
    fun showEnemyInfo(charDrawable: Drawables?, lifePercentage: Float) {
        enemyInfo.character(charDrawable)
        enemyInfo.life(lifePercentage, 0f)

        if (charDrawable != null) {
            enemyInfo.alpha = 1f
        } else {
            enemyInfo.alpha = 0f
        }
    }

    /**
     * Funcion que actualiza la vida de los enemigos
     */
    fun enemyLife(percentage: Float) = enemyInfo.also { it.resetFadeOutDelay() }.life(percentage)


    /**
     * Funcion que se muestra un mensaje por pantalla y se desvance al cabo de un tiempo
     *
     * @param infoText Mensaje a mostrar por pantalla
     */
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
        /**
         * Extension del constructor para poder añadirla como actor al escenario
         */
fun <S> KWidget<S>.gameView(
    model: GameModel,
    bundle:I18NBundle,
    recordPref: Preferences,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: GameView.(S) -> Unit = {}
): GameView = actor(GameView(model,bundle, recordPref, skin), init)