package app

import com.typesafe.config.ConfigFactory

/**
 * Utility values and functions.
 */
package object util {

  // Dynamic config loader
  private def _loadConfig = {
    val _config = ConfigFactory.load()
    if (_config.getBoolean("application.config.override")) { // Allow overridden config from external file
      ConfigFactory.parseFile(new java.io.File(_config.getString("application.config.file")))
    } else { _config }
  }

  /**
   * Application configuration object.
   */
  final val config = _loadConfig
}
