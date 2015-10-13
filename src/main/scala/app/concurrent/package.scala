package app

import com.twitter.concurrent.NamedPoolThreadFactory
import com.twitter.util.FuturePool
import java.util.concurrent.{SynchronousQueue, ThreadPoolExecutor, TimeUnit}

/**
 * Concurrency utils.
 */
package object concurrent {

  // Core internal thread pool
  // See:
  //   http://dev.bizo.com/2014/06/cached-thread-pool-considered-harmlful.html
  //   https://groups.google.com/forum/#!msg/finaglers/yWOr7-7CmPw/TBsJenqynQQJ
  private val threadPool =  {
    val cpus = Runtime.getRuntime.availableProcessors
    val secs: Long = 60
    val factory = new NamedPoolThreadFactory("greeting.io.thread", makeDaemons = true)
    val pool = new ThreadPoolExecutor(5 * cpus, 15 * cpus, secs, TimeUnit.SECONDS, new SynchronousQueue[Runnable](), factory)
    pool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy)
    pool
  }

  /**
   * Make blocking operations async.
   */
  def inNewThread: FuturePool = FuturePool(threadPool)
}
