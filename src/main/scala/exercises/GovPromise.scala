package exercises

/**
 * Created by zhoufeng on 15/11/22.
 */
import concurrent.{Promise, Future, Await}
import concurrent.duration._
import concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}

case class TaxCut(reduction: Int)
case class Excuse(msg: String) extends Exception(msg)

object Government {
  def redeemCampaignPledge(): Future[TaxCut] = {
    val promise: Promise[TaxCut] = Promise()
    Future {
      println("Starting the new legislative period.")
      Thread.sleep(1000)
      promise.success(TaxCut(30))
      println("We reduced the taxes! You must reelect us!!!")
    }
    promise.future
  }

  def redeemCampaignPledgeAgain(): Future[TaxCut] = {
    val promise = Promise[TaxCut]()
    Future {
      println("Starting the new legislative period.")
      Thread.sleep(1000)
      promise.failure(Excuse("Global economy crisis"))
      println("We failed, but you will forgive us.")
    }
    promise.future
  }
}

object GovPromise extends App {
  val f = Government.redeemCampaignPledge()
  println("Check promise...")
  f.onComplete {
    case Success(TaxCut(reduction)) => println(s"Promise success: $reduction")
    case Failure(ex) => println(s"Promise failure: ${ex.getMessage}")
  }

  val g = Government.redeemCampaignPledgeAgain()
  println("Check promise...")
  g.onComplete {
    case Success(TaxCut(reduction)) => println(s"Promise success: $reduction")
    case Failure(ex) => println(s"Promise failure: ${ex.getMessage}")
  }

  Await.result(f, Duration.Inf)
  Await.result(g, Duration.Inf)
}
