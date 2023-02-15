package com.game.Screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.game.ui.view.*
import ktx.app.KtxScreen
import ktx.scene2d.*

class UiScreen : KtxScreen {

    private val stage: Stage = Stage(ExtendViewport(720f,480f))
    private lateinit var  gameView: GameView
    init {
        loadSkin()
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width,height,true)
    }

    override fun show() {
        stage.clear()
        stage.actors {
            gameView= gameView()

        }
        stage.isDebugAll=true
    }

    override fun render(delta: Float) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.R)){
            hide()
            show()
        }else  if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)){
            gameView.playerLife(0f)
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