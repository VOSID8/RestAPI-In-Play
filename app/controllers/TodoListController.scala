package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json.{Json, Writes}
import scala.collection.mutable

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class TodoListController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  case class TodoListItem(id: Long, description: String, isItDone: Boolean)
  implicit val todoListItemWrites: Writes[TodoListItem] = Json.writes[TodoListItem]

  private val todoList = new mutable.ListBuffer[TodoListItem]()
  todoList += TodoListItem(1, "test", true)
  todoList += TodoListItem(2, "some other value", false)

  def getAll(): Action[AnyContent] = Action {
  if (todoList.isEmpty) {
    NoContent
  } else {
    Ok(Json.toJson(todoList))
  }
}

  def randomNumber = Action{
    Ok(util.Random.nextInt(100).toString)
  }

  def getById(itemId: Long) = Action {
    val foundItem = todoList.find(_.id == itemId)
    foundItem match {
      case Some(item) => Ok(Json.toJson(item))
      case None => NotFound
  }
}

def markAsDone(itemId: Long) = Action {
    val foundItem = todoList.find(_.id == itemId)
    foundItem match {
      case Some(item) =>
        val newItem = item.copy(isItDone = true)
        todoList.dropWhileInPlace(_.id == itemId)
        todoList += newItem
        Accepted(Json.toJson(newItem))
      case None => NotFound
    }
  }

  def deleteAllDone() = Action {
    todoList.filterInPlace(_.isItDone == false)
    Accepted
  }

  

}
