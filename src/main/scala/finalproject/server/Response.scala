package finalproject.server

import cats._
import cats.syntax.all._

/** An http response.
  */
case class Response(
    httpVersion: String,
    status: Response.Status,
    body: Array[Byte],
    headers: Map[String, String]
) {

  /** TODO #2
    *
    * Transforms the response into a valid byte representation as per HTTP specs.
    */
  def bytes: Array[Byte] = {
    val encodedStatusLine = Seq(httpVersion, status.code.show, status.reason).mkString("", " ", "\r\n")
    val encodedHeaders = headers.map { case (key, value) => key + ": " + value }.mkString("", "\r\n", "\r\n")
    val encoded = (encodedStatusLine + encodedHeaders + "\r\n").getBytes()

    encoded ++ body
  }

  /** String representation of this response (mostly for debugging).
    */
  override def toString: String = Response.show.show(this)
}

object Response {

  /** ADT (incomplete) for valid http status.
    */
  abstract class Status(val code: Int, val reason: String)
  object Ok extends Status(200, "OK")
  object NotFound extends Status(404, "Not Found")
  object InternalServerError extends Status(500, "Internal Server Error")

  implicit val show: Show[Response] = Show.show { response =>
    s"""Response(
       |  httpVersion = ${response.httpVersion}
       |  status = ${response.status.code} ${response.status.reason}
       |  headers = [
       |    ${response.headers.map { case (k, v) => s"$k: $v" }.mkString("\n    ")}
       |  ]
       |  body = ${new String(response.body)}
       |)""".stripMargin
  }
}
