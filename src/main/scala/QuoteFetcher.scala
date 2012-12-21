
class QuoteFetcher {
  val randomQuoteUrlStr =
    "http://www.iheartquotes.com/api/v1/random?show_permalink=false&show_source=false"

  val randomQuoteUrl = new java.net.URL(randomQuoteUrlStr)

  def getQuote: Option[String] = {
    import scala.actors.Futures._

    awaitAll(
      4000,
      future { io.Source.fromURL(randomQuoteUrl).mkString.trim }
      ).flatten.headOption.asInstanceOf[Option[String]]
  }
}

//object QuoteFetcher extends App {
  //print("'")
  //println((new QuoteFetcher).getQuote)
  //print("'")
//}
