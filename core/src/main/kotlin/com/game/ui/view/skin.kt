package com.game.ui.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import ktx.assets.disposeSafely
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.style.label
import ktx.style.set
import ktx.style.skin

enum class Drawables(
    val atlasKey: String
) {
    BACKGROUNDINFO("backgroundInfo"),
    PLAYER("idle"),
    LIFEBAR("lifebar"),
    FRAME_BGD("background"),
    FRAME_FGD("background"),

}

enum class Labels {
    FRAME;

    val skinKey = this.name.lowercase()
}

enum class Fonts(
    val atlasRegionKey: String,
    val scaling: Float,
) {
    DEFAULT("damage", 0.5f);

    val skinKey = "Font_${this.name.lowercase()}"
    val fontPath = "${this.atlasRegionKey}.fnt"
}

operator fun Skin.get(drawable: Drawables): Drawable = this.getDrawable(drawable.atlasKey)
operator fun Skin.get(font: Fonts): BitmapFont =this.getFont(font.skinKey)

fun loadSkin() {
    Scene2DSkin.defaultSkin = skin(TextureAtlas("ui/ui.atlas")) { skin ->
       Fonts.values().forEach { fnt->
           skin[fnt.skinKey] = BitmapFont(Gdx.files.internal(fnt.fontPath), skin.getRegion(fnt.atlasRegionKey)).apply {
               data.setScale(fnt.scaling)
               data.markupEnabled=true
           }
       }

        label(Labels.FRAME.skinKey) {
            font = skin[Fonts.DEFAULT]
            background = skin[Drawables.FRAME_BGD].apply {
                leftWidth=3f
                rightWidth=3f
                topHeight=5f
            }

        }
    }
}

fun disposeSkin() {
    Scene2DSkin.defaultSkin.disposeSafely()
}

@Scene2dDsl
fun <S> KWidget<S>.gameView(
    skin: Skin = Scene2DSkin.defaultSkin,
    init: GameView.(S) -> Unit = {}
): GameView = actor(GameView(skin), init)