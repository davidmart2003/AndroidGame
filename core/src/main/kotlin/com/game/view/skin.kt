package com.game.view

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import ktx.assets.disposeSafely
import ktx.scene2d.Scene2DSkin
import ktx.style.label
import ktx.style.skin

fun loadSkin(){
    Scene2DSkin.defaultSkin=skin(TextureAtlas("ui/ui.atlas")){skin->
        label {
         //   font =
        }
    }
}

fun disposeSkin(){
    Scene2DSkin.defaultSkin.disposeSafely()
}