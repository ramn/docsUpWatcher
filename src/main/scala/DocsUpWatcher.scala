import scala.xml.{XML, Elem}

object DocsUpWatcher extends App {

  val watchedLinkPrefixes =
    io.Source.fromFile("watchedLinkPrefixes.txt")
      .getLines
      .toList
      .filterNot(_ == "")

  val xmlData: Elem = XML.loadFile("RSSFeed.xml")

  val linkRegex = """<a href="(http://docs-up.com/diff.*?)">.*?</a>""".r

  val links = for {
    contentElem <- (xmlData \\ "content")
    val content = contentElem.text
    linkMatch <- linkRegex.findAllIn(content).matchData
    href <- linkMatch.subgroups
    if isWatched(href)
  } yield href

  def isWatched(wrappedLink: String): Boolean =
    watchedLinkPrefixes exists { prefix =>
      wrappedLink contains prefix
  }

  links foreach println
}
