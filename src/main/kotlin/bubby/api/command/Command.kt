package bubby.api.command

open class Command(private val name: String, private val desc: String) {
  open fun execute(command: String, args: List<String>) {

  }

  fun getName(): String {
    return this.name
  }

  fun getDesc(): String {
    return this.desc
  }
}
