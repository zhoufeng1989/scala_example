package exercises

/**
 * Created by zhoufeng on 15/11/9.
 */
abstract class JSON
case class JSeq(elems: List[JSON]) extends JSON
case class JObj(bindings: Map[String, JSON]) extends JSON
case class JNum(num: Double) extends JSON
case class JStr(str: String) extends JSON
case class JBool(b: Boolean) extends JSON
case object JNull  extends JSON

object JSON {
  def show(json: JSON): String = json match {
    case JNum(num) => num.toString
    case JStr(str) => "\"" + str + "\""
    case JBool(b) => b.toString
    case JNull => "Null"
    case JSeq(elems) => "[" + elems.map(show).mkString(", ") + "]"
    case JObj(bindings) => {
      val assocs = bindings.map{case (key, value) => "\"" + key + "\":" + show(value)}
      "{" + assocs.mkString(",")  + "}"
    }
  }
}
