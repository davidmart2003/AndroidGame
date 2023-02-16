package com.game.model

import kotlin.reflect.KProperty

abstract class PropertyChangeSource {
    @PublishedApi
    internal val listenerMap = mutableMapOf<KProperty<*>, MutableList<(Any) -> Unit>>()

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> onPropertyChange(property: KProperty<T>, noinline action: (T) -> Unit) {
        val actions = listenerMap.getOrPut(property) { mutableListOf() } as MutableList<(T) -> Unit>
        actions += action
    }

    fun notify(property: KProperty<*>, value:Any) {
        listenerMap[property]?.forEach { it(value) }
    }

<<<<<<< HEAD

=======
    /*
    val model = GameModel()
    model.playerLife

        //view
        model.onPropertyChange(model::playerLife){playerLife->
        lifeBar.setValue(playerLife)
     */
>>>>>>> 168e7a52a31f5513ef11a91c3771d3f1e504aae2
}