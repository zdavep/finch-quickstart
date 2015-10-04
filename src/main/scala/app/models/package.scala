package app

import argonaut._, Argonaut._

/**
 * Model services.
 */
package object models {

  // Provides an in implementation of the EncodeJson Typeclass (from Argonaut) for Greeting
  implicit def encodeGreetingAsJson: EncodeJson[Greeting] = EncodeJson { (g: Greeting) =>
    ("greeting" := g.message) ->: jEmptyObject
  }
}
