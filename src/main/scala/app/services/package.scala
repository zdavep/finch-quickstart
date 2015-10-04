package app

import app.concurrent.inNewThread
import app.errors.illegalArgs
import app.models.Greeting
import com.twitter.util.Future

/**
 * Application services.
 */
package object services {

  /**
   * Greeting service
   */
  def greeting(name: String, title: Option[String]): Future[Greeting] = inNewThread {
    Greeting.decorate(title.fold(s"Hello, $name")(title => s"Hello, $title $name"))
  }

  /**
   * Multi-greeting service
   */
  def greetings(name: String, n: Int, title: Option[String]): Future[Seq[Greeting]] =
    if (n < 0 || n > 99) illegalArgs("Bad number; 0 < n < 100") else inNewThread {
      val msg = title.fold(s"Hello, $name")(title => s"Hello, $title $name")
      (1 to n).map(i => Greeting.decorate(s"$msg: $i")).toSeq
    }
}

