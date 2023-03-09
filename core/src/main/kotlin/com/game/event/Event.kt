package com.game.event

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.Stage
import com.game.component.PlayerComponent
import com.github.quillraven.fleks.Entity


fun Stage.fire(event: Event){ this.root.fire(event) }

/**
 * Evento que se lanza al cambiar de mapa
 * @property map Mapa que se va a cargar
 * @property playerComponent Entidad del jugador
 */
data class MapChangeEvent (val map:TiledMap, val playerComponent: PlayerComponent?=null) : Event()

/**
 * Evento que se lanza parta crear un portal
 */
class SpawnPortalEvent() : Event()

/**
 * Evento que se lanza si la entidad hace daño
 */
class EntityDamageEvent(val entity: Entity): Event()

/**
 * Evento que se lanza si la entidad muere para reproducir un sonido
 *
 * @property name Nombre de la entidad para reproducir el sonido específico
 */
class DeathSound(var name :String): Event()

/**
 * Evento que se lanza cada vez que entramos en el campo de vision de la entidad
 *
 * @property entity Entidad
 */
class EntityAggroEvent(val entity: Entity?): Event()

/**
 * Evento que se lanza cuando es pulsado el boton de moviemineto
 *
 * @property sin Direccion en el eje Y
 * @property cos Direccion en el eje X
 */
class ButtonPressedEvent(val sin:Float,val cos:Float):Event()

/**
 * Evento que se lanza cuando el boton de ataque es pulsado
 *
 * @property attack True si esta pulsado
 */
class ButtonAttackPressed(val attack : Boolean) : Event()

/**
 * Evento que se lanza si el boton de escudfo es lanzado
 *
 * @property shield True si el boton esta pulsado
 */
class ButtonShieldPressed(val shield : Boolean) : Event()

/**
 * Evento que se lanza para crear una nueva partida
 */
class NewGameEvent():Event()

/**
 * Evento que se lanza para abrir el menu de opciones
 */
class SettingsGameEvent():Event()
class HideSettingsGameEvent():Event()

/**
 * Evento que se lanza cada vez que el jugador realiza un ataque
 */
class HitEvent():Event()

/**
 * Evento que se lanza cada vez que el jugador sube de nivel
 *
 * @property level Nivel del personaje
 */
class LevelUpEvent(val level:Int):Event()

/**
 * Evento que se lanza cada vez que la vida maxima del jugador cambia
 * @property life Vida maxima del jugador
 */
class ActualLifeEvent(val life: Float):Event()

/**
 * Evento que se lanza cunado se cierra el inventario
 */
class HideInventoryEvent():Event()

/**
 * Evento que se lanza cada vez que la velocidad del jugador cambia
 *
 * @property speed Velocidad del jugador
 */
class SpeedEvent(val speed: Float):Event()

/**
 * Evento que se lanza cada que el daño del jugador cambia
 *
 * @property attack Daño del jugador
 */
class AttackEvent(val attack: Float):Event()

/**
 * Evento que se lanza para abrir el menu de créditos
 */
class CreditsGameEvent():Event()
class HideCreditsEvent():Event()

/**
 * Evento que se lanza para abrir el menu de pausa
 */
class PauseEvent():Event()

/**
 * Evento que se lanza para cerrar el menu de pause
 */
class ResumeEvent():Event()

/**
 * Evento que se lanza para abrir el inventario
 */
class InventoryEvent():Event()

/**
 * Evento que se lanza para abrir el Menu
 */
class MenuScreenEvent():Event()

/**
 * Evento que se lanza cuando el jugador muere
 */
class DeadEvent(): Event()

/**
 * Evento que se lanza cuando el jugador vence al final boss , es decir gana
 */
class WinEvent():Event()

class TimeEvent(val entity: Entity) : Event()

class TutorialGameEvent():Event()
class AccelerometerEvent():Event()

class HideTutorialEvent():Event()