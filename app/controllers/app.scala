package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._



case class Student(id: Int, address: String, name: String)
implicit val studentListJson = Json.format[Student]

@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents)
 extends BaseController {

  val studentList = new mutable.ListBuffer[Student]()
  studentList += Student(1,"Delhi","Mayank")
  studentList += Student(2,"Mathura","Yashika")

  def getAll(): Action[AnyContent] = Action {
    if (studentList.isEmpty) NoContent else Ok(Json.toJson(studentList))
  }

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

}
