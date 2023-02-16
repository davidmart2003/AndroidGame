package com.game.Screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
<<<<<<< HEAD
import com.game.component.LifeComponent
import com.game.component.PlayerComponent
import com.game.event.EntityDamageEvent
import com.game.event.fire
import com.game.model.GameModel
import com.game.ui.view.*
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.world
=======
import com.game.ui.view.*
>>>>>>> 168e7a52a31f5513ef11a91c3771d3f1e504aae2
import ktx.app.KtxScreen
import ktx.scene2d.*

class UiScreen : KtxScreen {

    private val stage: Stage = Stage(ExtendViewport(720f,480f))
<<<<<<< HEAD
    private val world = world {  }
    private val playerEntity : Entity
    private val model = GameModel(world,stage)
    private lateinit var  gameView: GameView
    init {
        loadSkin()
        playerEntity = world.entity {
            add<PlayerComponent>()
            add<LifeComponent> {
                maxLife = 5f
                life = 3f
            }

        }
=======
    private lateinit var  gameView: GameView
    init {
        loadSkin()
>>>>>>> 168e7a52a31f5513ef11a91c3771d3f1e504aae2
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width,height,true)
    }

    override fun show() {
        stage.clear()
<<<<<<< HEAD
        stage.addListener(model)
        stage.actors {
            gameView= gameView(model)
=======
        stage.actors {
            gameView= gameView()
>>>>>>> 168e7a52a31f5513ef11a91c3771d3f1e504aae2

        }
        stage.isDebugAll=true
    }

    override fun render(delta: Float) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.R)){
            hide()
            show()
        }else  if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)){
<<<<<<< HEAD
            stage.fire(EntityDamageEvent(playerEntity))
=======
            gameView.playerLife(0f)
>>>>>>> 168e7a52a31f5513ef11a91c3771d3f1e504aae2
        }else  if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)){
            gameView.playerLife(0.5f)
        }else  if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)){
            gameView.playerLife(1f)
        }else  if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)){
            gameView.showEnemyInfo(Drawables.PLAYER,0.5f)
        }else  if(Gdx.input.isKeyJustPressed(Input.Keys.E)){
            gameView.popup("SI ME CORRO")
        }

        stage.act()
        stage.draw()
    }

    override fun dispose() {
        stage.dispose()
        disposeSkin()
    }
}