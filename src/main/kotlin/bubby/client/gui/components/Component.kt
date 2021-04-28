package bubby.client.gui.components

interface Component {
  fun renderComponent()
  fun updateComponent(x: Int, y: Int)
  fun mouseClicked(x: Int, y: Int, b: Int)
  fun mouseReleased(x: Int, y: Int, b: Int)
  val parentHeight: Int
  fun keyTyped(c: Char, k: Int)
  fun setOff(nOff: Int)
  val height: Int
  fun handleMouseInput()
}