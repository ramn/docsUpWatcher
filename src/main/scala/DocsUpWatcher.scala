import scala.xml.{XML, Elem}
import java.net.URL
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat


object DocsUpWatcher extends App {
  val feedUrl = new URL("http://docs-up.com/RSSFeed")
  val quoteFetcher = new QuoteFetcher
  lazy val quoteOpt = quoteFetcher.getQuote

  val watchedLinkPrefixes =
    io.Source.fromFile("watchedLinkPrefixes.txt")
      .getLines
      .toList
      .filterNot(_ == "")

  //val xmlData: Elem = XML.loadFile("RSSFeed.xml")
  val xmlData: Elem = XML.load(feedUrl)

  val linkRegex = """<a href="(http://docs-up.com/diff.*?)">.*?</a>""".r

  val links = for {
    entry <- xmlData \\ "entry"
    val updatedAt = parseDate(entry \ "updated" text)
    if updatedAt isAfter DateTime.now.minusDays(1)
    val content = (entry \ "content" text)
    linkMatch <- linkRegex.findAllIn(content).matchData
    href <- linkMatch.subgroups
    if isWatched(href)
  } yield href

  def isWatched(wrappedLink: String): Boolean =
    watchedLinkPrefixes exists { prefix =>
      wrappedLink contains prefix
  }

  def parseDate(dateStr: String): DateTime = {
    val format = ISODateTimeFormat.dateTimeNoMillis
    format.parseDateTime(dateStr)
  }

  links foreach println
  println("\n\n")
  quoteOpt foreach println
}
