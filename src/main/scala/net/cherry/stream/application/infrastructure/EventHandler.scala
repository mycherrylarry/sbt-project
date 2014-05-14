package net.cherry.stream.application.infrastructure

import com.twitter.concurrent.Offer

trait EventHandler {
  def handleEvent(event: String): Offer[Unit]
}
