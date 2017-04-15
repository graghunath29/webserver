package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import com.redis._
import com.typesafe.config.ConfigFactory

case class ClassifiedTweet(urgency: String, category: String, id: String )

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject() extends Controller {
  
  val userNProduct = "testuser:toronto"
  val brkHeader = "BREAKING"
  val sportHeader = "SPORTS"
  val poliHeader = "POLITICS"
  val sciTechHeader = "SCITECH"
  val businessHeader = "BUSINESS"
  val artsHeader = "ARTS"
  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
    
    
    val r = new RedisClient(ConfigFactory.load().getString("my.configuration.redisIP"), 6379)
    val data = r.get(userNProduct).getOrElse("");
    val clTw = data.split(',');
   
   
    val twObjs = clTw.map(x => ClassifiedTweet(x.split(':')(0), x.split(':')(1), x.split(':')(2))); //Classified Tweets as ClassifiedTweet object collection
    
    val viewMap = scala.collection.mutable.Map[String, String]()
    
    val breakingTweetIds = twObjs.filter((_.urgency == "BREAKING")).map(_.id)
    var htmlString = ""
    for (t <- breakingTweetIds) {
        
        val url = """https://api.twitter.com/1.1/statuses/oembed.json?id=""" + t
        val resp = scala.io.Source.fromURL(url).mkString
    
        val json: JsValue = Json.parse(resp)
    
        htmlString += (json \ "html").as[String] 
    }
    viewMap(brkHeader) = htmlString
    
    val politicsTweetIds = twObjs. filter((_.category == "POLITICS")).map(_.id)
    htmlString = "" 
    for (t <- politicsTweetIds) {
        
        val url = """https://api.twitter.com/1.1/statuses/oembed.json?id=""" + t
        val resp = scala.io.Source.fromURL(url).mkString
    
        val json: JsValue = Json.parse(resp)
    
        htmlString += (json \ "html").as[String] 
    }
    viewMap(poliHeader) = htmlString
    
    val businessTweetIds = twObjs. filter((_.category == "BUSINESS")).map(_.id)
    htmlString = "" 
    for (t <- businessTweetIds) {
        
        val url = """https://api.twitter.com/1.1/statuses/oembed.json?id=""" + t
        val resp = scala.io.Source.fromURL(url).mkString
    
        val json: JsValue = Json.parse(resp)
    
        htmlString += (json \ "html").as[String]
    }
    viewMap(businessHeader) = htmlString
    
    val sportsTweetIds = twObjs. filter((_.category == "SPORTS")).map(_.id)
    htmlString = "" 
    for (t <- sportsTweetIds) {
        
        val url = """https://api.twitter.com/1.1/statuses/oembed.json?id=""" + t
        val resp = scala.io.Source.fromURL(url).mkString
    
        val json: JsValue = Json.parse(resp)
    
        htmlString += (json \ "html").as[String]
    }
    viewMap(sportHeader) = htmlString
    
    val sciTechTweetIds = twObjs. filter((_.category == "SCITECH")).map(_.id)
    htmlString = "" 
    for (t <- sciTechTweetIds) {
        
        val url = """https://api.twitter.com/1.1/statuses/oembed.json?id=""" + t
        val resp = scala.io.Source.fromURL(url).mkString
    
        val json: JsValue = Json.parse(resp)
    
        htmlString += (json \ "html").as[String]
    }
    viewMap(sciTechHeader) = htmlString
    
    val artsTweetIds = twObjs. filter((_.category == "ARTS")).map(_.id)
    htmlString = "" 
    for (t <- artsTweetIds) {
        
        val url = """https://api.twitter.com/1.1/statuses/oembed.json?id=""" + t
        val resp = scala.io.Source.fromURL(url).mkString
    
        val json: JsValue = Json.parse(resp)
    
        htmlString += (json \ "html").as[String]
    }
    viewMap(artsHeader) = htmlString
    
    Ok(views.html.index(viewMap))
  }

}
