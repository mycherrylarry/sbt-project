package net.cherry.server.infrastructure

import com.twitter.concurrent.Offer

/**
 * handle event
 */
trait EventHandler {
  def handleEvent(event: String): Offer[Unit]
}
