package com.game.System

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.physics.box2d.World
import com.game.Component.ImageComponent
import com.game.Component.PhysicComponent
import com.github.quillraven.fleks.*
import ktx.math.component1
import ktx.math.component2

val Fixture.entity : Entity
    get() = this.body.userData as Entity
@AllOf([PhysicComponent::class, ImageComponent::class])
class  PhysicSystem(
    private val myWorld: World,
    private val imageComponents: ComponentMapper<ImageComponent>,
    private val physicsComponents: ComponentMapper<PhysicComponent>

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

    override fun beginContact(contact: Contact?) {

    }

    override fun endContact(contact: Contact?) {

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