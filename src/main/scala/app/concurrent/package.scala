package app

import com.google.common.util.concurrent.ThreadFactoryBuilder
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
  private def newThreadPool = {
    val cpus = Runtime.getRuntime.availableProcessors
    val secs: Long = 60
    val factory = new ThreadFactoryBuilder().setNameFormat("greeting.io.thread-%d").build()
    val pool = new ThreadPoolExecutor(5 * cpus, 15 * cpus, secs, TimeUnit.SECONDS, new SynchronousQueue[Runnable](), factory)
    val policy = new ThreadPoolExecutor.CallerRunsPolicy
    pool.setRejectedExecutionHandler(policy)
    pool
  }

  // Use our internal thread pool builder for future pools.
  private val threadPool = newThreadPool

  /**
   * Make blocking operations async.
   */
  def inNewThread: FuturePool = FuturePool(threadPool)
}
