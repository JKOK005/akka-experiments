package com.lightbend.akka.actor
import com.lightbend.akka.msg._

case class PersistentActorState(eventsChain: List[String] = Nil){
	val update = (evt: Event) => {copy(evt.data :: eventsChain)}
	def getSize() = eventsChain.length
	override def toString: String = eventsChain.toString
}