package exercises

import scala.annotation.tailrec

/**
 * Created by zhoufeng on 15/11/11.
 */
object Loops {
  def While(condition: => Boolean)(commands: => Unit): Unit = {
    if(condition) {
      commands
      While(condition)(commands)
    }
    else ()
  }

  def Repeat(commands: => Unit)(condition: => Boolean): Unit = {
    commands
    if(condition) () else Repeat(commands)(condition)
  }

  class Do(commands: => Unit) {
    @tailrec
    final def Until(condition: => Boolean): Unit = {
      commands
      if (condition) () else Until(condition)
    }
  }

  object Do {
    def apply(commands: => Unit) = new Do(commands)
  }
}
