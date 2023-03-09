package com.game


import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.I18NBundle
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.game.Screens.MenuScreen
import com.game.ui.view.disposeSkin

import ktx.app.KtxGame
import ktx.app.KtxScreen

class MyGame : KtxGame<KtxScreen>() {
    private val batch: Batch by lazy { SpriteBatch() }
    val gameStage by lazy { Stage(ExtendViewport(16f, 9f)) }
    val uiStage by lazy { Stage(ExtendViewport(1280f, 720f), batch) }
    lateinit var textureAtlas: TextureAtlas
    lateinit var settingPref: Preferences
    lateinit var recordPref :Preferences
    lateinit var bundle : I18NBundle
    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
        textureAtlas = TextureAtlas(Gdx.files.internal("graphics/gameObjects.atlas"))
        bundle = I18NBundle.createBundle(Gdx.files.internal("lan/mybundle"))
        settingPref = Gdx.app.getPreferences("setting")
        recordPref = Gdx.app.getPreferences("record")
        addScreen(MenuScreen(this))
     //   addScreen(InventoryScreen())
       // setScreen<InventoryScreen>()
        setScreen<MenuScreen>()
        //setScreen<UiScreen>()
    }

    override fun dispose() {
        super.dispose()
        disposeSkin()
    }
    companion object {
        const val UNIT_SCALE = 1 / 16f
        var CREATED = false
        var SPEED =0f
        var ATTACK=0f
        var LIFE =0f

    }


}