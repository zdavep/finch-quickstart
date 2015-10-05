package app

import com.twitter.finagle.IndividualRequestTimeoutException
import com.twitter.finagle.httpx.{Response, Request}
import com.twitter.finagle.service.TimeoutFilter
import com.twitter.finagle.util.DefaultTimer
import com.twitter.util.Duration
import com.typesafe.config.ConfigFactory

/**
 * Utility values and functions.
 */
package object util {

  // Dynamic config loader
  private def _loadConfig = {
    val _config = ConfigFactory.load()
    if (!_config.getBoolean("application.config.override")) _config else { // Allow overridden config from external file
      ConfigFactory.parseFile(new java.io.File(_config.getString("application.config.file")))
    }
  }

  /**
   * Application configuration object.
   */
  final val config = _loadConfig

  /**
   * A simple filter tha adds timeouts to service calls
   */
  def timeoutFilter(duration: Duration): TimeoutFilter[Request, Response] =
    new TimeoutFilter(duration, new IndividualRequestTimeoutException(duration), DefaultTimer.twitter)
}
