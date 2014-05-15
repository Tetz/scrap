package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.libs.json._

import java.io.FileReader
import java.io.FileNotFoundException
import java.io.IOException

object Chat extends Controller {

  // Redirect to http::/example.com/
  def index = Action {
    Redirect(routes.Application.index)
  }

  def addMessage = Action { request =>
    (
      for{
        json <- request.body.asJson
        uuid <- (json \ "uuid").asOpt[String]
        name <- (json \ "name").asOpt[String]
        group_id <- (json \ "group_id").asOpt[String]
        message <- (json \ "message").asOpt[String]
        }yield{

          // Create new user
          models.Users.create(uuid)
          models.Users.update_name(uuid,name)
          models.Users.update_group_id(uuid,group_id.toLong)

          // Add a message
          val user_id = models.Users.fetchRow(uuid).head.id
          models.Messages.add(user_id,group_id,message) 

          // TODO Debug
          println(user_id)
          println(uuid)
          println(name)
          println(group_id)
          println(message)
          Ok("Chat.scala :: load")
      }
      ).getOrElse{
        Ok("Chat.scala :: load :: error")
        }
      }

      def getMessage = Action { request =>
        (
          for{
            json <- request.body.asJson
            uuid <- (json \ "uuid").asOpt[String]
            group_id <- (json \ "group_id").asOpt[String]
            }yield{

              // TODO Create new user
              models.Users.create(uuid)

              // TODO Get messages
              val user_id = models.Users.fetchRow(uuid).head.id
              val messageList = models.Messages.fetchSomeBy(user_id,group_id.toLong)


              // TODO Create Json 
              try{
                var messageMap = for (v <- messageList)yield{
                  Map(
                    "user_id" -> v.id.toString,
                    "name" -> models.Users.fetchRowById(v.id).head.name,
                    "message" -> v.message
                    )
                }

                val jsonData = Json.obj(
                  "messages" -> Json.toJson(messageMap),
                  "info" -> "end"
                )
                println(jsonData)
                Ok(jsonData)
              }catch{
                case ex: FileNotFoundException => println("missing file")
                case ex: IOException => println("I/O error")
              }

            // OutPut Result
            val hoge = models.Users.fetchRowById(user_id).head.name
            println(messageList)
            Ok("Chat.scala :: :: error")
        }
        ).getOrElse{
          Ok("Chat.scala :: :: error")
            }
        }

      }
