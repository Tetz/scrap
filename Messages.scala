package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import java.sql._
//import java.util._

case class Messages(id:Long,user_id:Long,group_id:Long,message:String,created_at:Timestamp)

object Messages {

  var messages = {
    get[Long]("id") ~
    get[Long]("user_id") ~
    get[Long]("group_id") ~
    get[String]("message") ~
    get[Timestamp]("created_at") map {
      case id~user_id~group_id~message~created_at => Messages(id,user_id,group_id,message,created_at)
    }
  }

  def fetchSomeBy(user_id:Long,group_id:Long) : List[Messages] = DB.withConnection {implicit c =>
    SQL("SELECT * FROM Messages WHERE group_id={group_id} ORDER BY id DESC LIMIT 30 ").on('user_id -> user_id, 'group_id -> group_id).as(messages *)
  }

  def fetchAll(): List[Messages] = DB.withConnection {implicit c =>
    SQL("SELECT * FROM Messages").as(messages *)
  }

  def add(user_id:Long, group_id:String, message:String) {
    DB.withConnection {implicit c =>
       SQL("INSERT INTO Messages (user_id,group_id,message) VALUES ({user_id},{group_id},{message})"
         ).on('user_id -> user_id, 'group_id -> group_id ,'message -> message 
      ).executeUpdate()
    }
  }

}

