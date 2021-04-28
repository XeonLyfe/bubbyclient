package bubby.api.event

import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

class EventManager {
  private val registries = mutableListOf<EventMethod>()

  @Suppress("UNCHECKED_CAST")
  fun register(obj: Any) {
    registries.addAll(
      obj.javaClass.declaredMethods
      .filter{it.parameterTypes.size == 1}
      .filter{it.isAnnotationPresent(PogEvent::class.java)}
      .map{EventMethod(obj, it, it.parameterTypes[0] as Class<out Event?>)}
    )
  }

  fun unregister(obj: Any) {
    registries.removeAll(registries.filter{it.obj == obj})
  }

  fun call(event: Event): Event {
    try {
      registries.filter { it.type == event.javaClass }.forEach {
        try {
          it.method.invoke(it.obj, event)
        }
        catch(ignored: IllegalAccessException) {
        }
        catch(ignored: IllegalArgumentException) {
        }
        catch(ignored: InvocationTargetException) {
        }
      }
    }
    catch(ignored: ConcurrentModificationException) {
    }

    return event
  }
}

data class EventMethod(val obj: Any, val method: Method, val type: Class<out Event?>)
