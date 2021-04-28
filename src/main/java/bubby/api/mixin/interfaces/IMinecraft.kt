package bubby.api.mixin.interfaces

interface IMinecraft
{
  val timer: ITimer
  fun setRightClickDelayTimer(newTime: Int)
}