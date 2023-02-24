package com.game.model

import kotlin.reflect.KProperty

/**
 * Clase que guarda y notifica a las clases que esuchan los cambios de distintas variables
 */
abstract class PropertyChangeSource {
    @PublishedApi
    /**
     * Almacen de las clases cuando esucha cambios de variables
     */
    internal val listenerMap = mutableMapOf<KProperty<*>, MutableList<(Any) -> Unit>>()

    /**
     * Guarda las funciones de las clases que escuchan para ejecutar el cambio de la variable
     */
    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> onPropertyChange(property: KProperty<T>, noinline action: (T) -> Unit) {
        val actions = listenerMap.getOrPut(property) { mutableListOf() } as MutableList<(T) -> Unit>
        actions += action
    }

    /**
     * Notifica el cambio de variable
     */
    fun notify(property: KProperty<*>, value: Any) {
        listenerMap[property]?.forEach { it(value) }
    }


}

/**
 * Clase que guarda las variables para notificar sus cambios
 */
class PropertyNotifier<T : Any>(initialValue: T) {
    private var _value: T = initialValue

    operator fun getValue(thisRef: PropertyChangeSource, property: KProperty<*>): T = _value

    operator fun setValue(thisRef: PropertyChangeSource, property: KProperty<*>, value: T) {
        _value = value
        thisRef.notify(property, value)
    }
}

/**
 * Inicializador de la clase
 */
inline fun <reified T : Any> propertyNotify(initialValue: T): PropertyNotifier<T> = PropertyNotifier(initialValue)