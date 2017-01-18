package app

import app.models.Greeting
import app.util.concurrent.inNewThread
import app.util.errors.illegalArgs
import com.twitter.util.Future

/**
 * Application services.
 */
package object services {

  /**
   * Greeting service
   */
  def greeting(name: String, maybeTitle: Option[String]): Future[Greeting] =
    inNewThread {
      Greeting.decorate(
        maybeTitle.map(title => s"Hello, $title $name").getOrElse(s"Hello, $name")
      )
    }

  /**
   * Multi-greeting service
   */
  def greetings(name: String, n: Int, maybeTitle: Option[String]): Future[Seq[Greeting]] =
    if (n <= 0 || n > 99) {
      illegalArgs("Bad number; 0 < n < 100")
    } else {
      inNewThread {
        val msg = maybeTitle.map(title => s"Hello, $title $name").getOrElse(s"Hello, $name")
        (1 to n).map(i => Greeting.decorate(s"$msg: $i"))
      }
    }
}

