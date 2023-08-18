package controllers

import javax.inject._
import play.api.libs.json.Json
import play.api._
import play.api.mvc._
import play.api.i18n._
import models.TaskListInMemoryModel
import play.api.data._
import play.api.data.Forms._
import play.api.libs.json._
import akka.actor.Actor
import play.api.libs.streams.ActorFlow
import akka.actor.ActorSystem
import akka.stream.Materializer
import actors.ChatActor
import akka.actor.Props
import actors.ChatManager

case class LoginData(username: String, password: String)


@Singleton
class base @Inject() (cc: MessagesControllerComponents)(implicit system: ActorSystem, mat: Materializer) extends MessagesAbstractController(cc) {
  val loginForm = Form(mapping(
    "Username" -> text(3, 10),
    "Password" -> text(8))(LoginData.apply)(LoginData.unapply))

  def login=Action{ implicit request =>
    Ok(views.html.login1(loginForm))
  }
  // def validateLoginGet(username:String,password: String)= Action{

    // Ok(s"$username got logged in with pass $password")
  // }

  def validateLoginPost = Action{ implicit request =>
    val postVals = request.body.asFormUrlEncoded
    postVals.map { args =>
      val username = args("username").head
      val password = args("password").head
      if (TaskListInMemoryModel.validateUser(username,password)) {
        Ok(views.html.index(username)).withSession("username" -> username)
      } else{
        Redirect(routes.base.validateLoginPost).flashing("error" -> "Invalid password/username combination")
      }
      //      ok so routes. kerke uske aage ka ager link kisise match hota hai toh cool verna compile error
      //      Ager toh shi rha toh tashList daaldenge verna login wapas
      //      Ok(s"$username logged in with $password")
    }.getOrElse(Redirect(routes.base.validateLoginPost))
  }

  def createUser = Action { implicit request =>
    val postVals = request.body.asFormUrlEncoded
    postVals.map { args =>
      val username = args("username").head
      val password = args("password").head
      if (TaskListInMemoryModel.createUser(username, password)) {
        Ok(views.html.index(username)).withSession("username" -> username)
      } else {
        Redirect(routes.base.validateLoginPost)
      }
    }.getOrElse(Redirect(routes.base.validateLoginPost)).flashing("error" -> "User creation failed")
  }
  //flash stays up for one call, it goes away after that
  // so basically, request me store horha hai and username me stored tha usernameOption so get it?

  def world = Action { implicit requset =>
      Ok("hi")
    }



  def product(prodType: String, prodNum: Int) = Action{
    Ok(s"Product Type is: $prodType, product number is: $prodNum")
  }

  // def usa = Action {implicit request =>
  // val usernameOption = request.session.get("username")
  // usernameOption.map { username =>
    // Ok(views.html.usa(username)).withSession("username" -> username)
  // }.getOrElse {Ok("Failed")}
  // }



  val manager = system.actorOf(Props[ChatManager], "Manager")


  def socket = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef { out =>
      ChatActor.props(out, manager)
    }
  }


  def logout = Action {
    Redirect(routes.base.validateLoginPost).withNewSession
  }

  def freemedia = Action{ implicit request =>
    val postVals = request.body.asFormUrlEncoded
    postVals.map { args =>
      val country = args("country").head
      val username = args("username").head
      Ok(views.html.media(country,username)).withSession("country" -> country)
      //      ok so routes. kerke uske aage ka ager link kisise match hota hai toh cool verna compile error
      //      Ager toh shi rha toh tashList daaldenge verna login wapas
      //      Ok(s"$username logged in with $password")
    }.getOrElse(Ok("wtf"))
  }


}

        //val json = request.body.asJson.get
        //val selectedVal = (json \ "selectedvalue").as[String]
        //val username = (json \ "username").as[String]
        //val selectedVal = request.session.get("country")
        //val username = "Mark"
        //println(selectedVal)
        //println(username)
        // selectedVal.map { selectedVal =>
          // Ok(views.html.media(username, selectedVall)).withSession("username" -> username, "selectedvalue" -> selectedVall);
        // };
        //Ok("hi")
        
        //Ok(views.html.media(selectedVal, username)).withSession("country" -> selectedVal)