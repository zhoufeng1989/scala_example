package exercises

/**
 * Created by zhoufeng on 15/11/26.
 */
trait Parent {
  val value: String
  override  val toString = "[" + value + "]"
}

object TraitExample extends App {
  val x = new Parent  {
    override val value: String = "Hi"
  }
  // print [null]
  println(x)


  val y = new { val value = "Hi"} with Parent
  // print [Hi]
  println(y)

}
