package com.game.system

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.physics.box2d.World
import com.game.component.AiComponent
import com.game.component.ImageComponent
import com.game.component.PhysicComponent
import com.game.system.EntitySpawnSystem.Companion.AI_SENSOR
import com.github.quillraven.fleks.*
import ktx.math.component1
import ktx.math.component2

val Fixture.entity : Entity
    get() = this.body.userData as Entity
@AllOf([PhysicComponent::class, ImageComponent::class])
class  PhysicSystem(
    private val myWorld: World,
    private val imageComponents: ComponentMapper<ImageComponent>,
    private val physicsComponents: ComponentMapper<PhysicComponent>,
    private val aiComponents : ComponentMapper<AiComponent>

) : ContactListener, IteratingSystem(interval = Fixed(1 / 60f)) {

    init {
        myWorld.setContactListener(this)
    }

    override fun onUpdate() {
        if (myWorld.autoClearForces) {
            myWorld.autoClearForces = false
        }
        super.onUpdate()
        myWorld.clearForces()
    }

    override fun onTick() {
        super.onTick()
        myWorld.step(deltaTime, 6, 2)
    }

    override fun onTickEntity(entity: Entity) {
        val physicComponent = physicsComponents[entity]
        physicComponent.prevPos.set(physicComponent.body.position)

        if (!physicComponent.impulse.isZero) {
            physicComponent.body.applyLinearImpulse(
                physicComponent.impulse,
                physicComponent.body.worldCenter,
                true
            )
            physicComponent.impulse.setZero()
        }
    }

    override fun onAlphaEntity(entity: Entity, alpha: Float) {
        val physicComponent = physicsComponents[entity]
        val imageComponent = imageComponents[entity]

        val (prevX,prevY) = physicComponent.prevPos
        val (bodyX, bodyY)= physicComponent.body.position

        imageComponent.image.run {
            setPosition(
                MathUtils.lerp(prevX,bodyX,alpha)-width*0.5f,
                MathUtils.lerp(prevY,bodyY,alpha)-height*0.5f)
        }
    }

    override fun beginContact(contact: Contact) {
        val entityA = contact.fixtureA.entity
        val entityB = contact.fixtureB.entity

        val isEntityBCollisionFixture = !contact.fixtureB.isSensor
        val isEntityACollisionFixture =  !contact.fixtureA.isSensor

        val isEntityAAiSensor = entityA in aiComponents && contact.fixtureA.isSensor && contact.fixtureA.userData==AI_SENSOR
        val isEntityBAiSensor = entityB in aiComponents && contact.fixtureB.isSensor && contact.fixtureB.userData==AI_SENSOR

        when {

        isEntityAAiSensor && isEntityBCollisionFixture ->{
            aiComponents[entityA].nearbyEntities +=entityB
        }
            isEntityBAiSensor && isEntityACollisionFixture ->{
                aiComponents[entityB].nearbyEntities +=entityA
            }
        }
    }

    override fun endContact(contact: Contact) {
        val entityA = contact.fixtureA.entity
        val entityB = contact.fixtureB.entity

        val isEntityBCollisionFixture = !contact.fixtureB.isSensor
        val isEntityACollisionFixture =  !contact.fixtureA.isSensor

        val isEntityAAiSensor = entityA in aiComponents && contact.fixtureA.isSensor && contact.fixtureA.userData==AI_SENSOR
        val isEntityBAiSensor = entityB in aiComponents && contact.fixtureB.isSensor && contact.fixtureB.userData==AI_SENSOR

        when {

            isEntityAAiSensor && isEntityBCollisionFixture ->{
                aiComponents[entityA].nearbyEntities -=entityB
            }
            isEntityBAiSensor && isEntityACollisionFixture ->{
                aiComponents[entityB].nearbyEntities -=entityA
            }
        }
    }

    private fun Fixture.isStaticBody() = this.body.type == BodyDef.BodyType.StaticBody
    private fun Fixture.isDynamicBody() = this.body.type == BodyDef.BodyType.DynamicBody

    override fun preSolve(contact: Contact, oldManifold: Manifold) {
        contact.isEnabled =
                ( contact.fixtureA.isStaticBody() && contact.fixtureB.isDynamicBody())||
                (contact.fixtureB.isStaticBody() && contact.fixtureA.isDynamicBody())
    }

    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {
        Unit
    }


}