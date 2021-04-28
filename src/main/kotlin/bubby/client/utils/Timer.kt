package bubby.client.utils

class Timer {
  var time = -1L

  fun reset() {
    time = System.nanoTime()
  }

  fun hasTimePassed(ms: Long): Boolean {
    return getCurrentTime(System.nanoTime() - time) >= ms
  }

  fun getCurrentTime(time: Long): Long {
    return time / 1000000L
  }
}
