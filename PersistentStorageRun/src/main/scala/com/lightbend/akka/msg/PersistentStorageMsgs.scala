package com.lightbend.akka.msg;

case class takeSnapshot();
case class Execute(command: String);
case class Event(data: String);