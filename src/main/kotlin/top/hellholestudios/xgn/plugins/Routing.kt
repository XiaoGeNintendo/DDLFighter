package top.hellholestudios.xgn.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.pipeline.*
import top.hellholestudios.xgn.data.*
import java.time.LocalDateTime
import java.util.UUID
import javax.xml.crypto.Data

fun PipelineContext<Unit,ApplicationCall>.username():String?{
    return call.sessions.get<UsernameSession>()?.username
}

fun PipelineContext<Unit,ApplicationCall>.user():User?{
    return DataModel.dms.users[username()]
}

fun PipelineContext<Unit,ApplicationCall>.buildEnv()=mutableMapOf(
        "importances" to Importance.entries.toTypedArray(),
        "model" to DataModel,
        "username" to username(),
        "user" to user(),
        "serverTime" to LocalDateTime.now()
    )
fun Application.configureRouting() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
    }


    routing {

        //page part
        get("/") {
            DataModel.recache()
            call.respond(FreeMarkerContent("test.ftl", buildEnv()))
        }
        get("/index") {
            DataModel.recache()
            call.respond(FreeMarkerContent("test.ftl", buildEnv()))
        }
        get("/add") {
            if(username()==null){
                call.respond("You need to login to add DDLs and events")
            }else{
                call.respond(FreeMarkerContent("add.ftl", buildEnv()))
            }
        }
        get("/login") {
            call.respond(FreeMarkerContent("login.ftl", buildEnv()))
        }
        get("/groups"){
            call.respond(FreeMarkerContent("groups.ftl", buildEnv()))
        }

        //do part
        post("/doLogin") {
            val p = call.receiveParameters()
            if (p["name"] == null || p["password"] == null) {
                call.respond("Malformed Request")
                return@post
            }
            if (p["name"] !in DataModel.dms.users) {
                call.respond("No such user. Please register!")
                return@post
            }
            if (p["password"] != DataModel.dms.users[p["name"]]!!.hash) {
                call.respond("Incorrect password.")
                return@post
            }

            call.sessions.set(UsernameSession(p["name"]!!))
            call.respond("success")
        }

        post("/doRegister") {
            val p = call.receiveParameters()
            if (p["name"] == null || p["password"] == null) {
                call.respond("Malformed Request")
                return@post
            }
            if (p["name"] in DataModel.dms.users) {
                call.respond("Username occupied.")
                return@post
            }

            DataModel.dms.users[p["name"]!!] =
                User(p["name"]!!, p["password"]!!, HashMap(), HashMap(),false, UUID.randomUUID().toString().replace("-", "_"))
            DataModel.save()
            call.sessions.set(UsernameSession(p["name"]!!))
            println(call.sessions.get<UsernameSession>())
            call.respond("success")
        }

        get("/reload") {
            DataModel.load()
            call.respondRedirect("index")
        }

        get("/del/{id}") {
            val res=DataModel.findDDL(call.parameters["id"])
            if(res==null){
                call.respond("No such event")
                return@get
            }

            if(user()?.admin==true || user()?.name==res.uploader){
                val ok = DataModel.removeDDL(call.parameters["id"])
                if (ok) {
                    call.respondRedirect("/index")
                } else {
                    call.response.status(HttpStatusCode.InternalServerError)
                    call.respondText("Failed to remove ${call.parameters["id"]}")
                }
            }else{
                call.respond("This page is admin only.")
            }
        }

        get("/delGroup/{id}"){
            val usr=user()
            val id=call.parameters["id"]
            if(usr==null){
                call.response.status(HttpStatusCode.Unauthorized)
                call.respond("Please login first.")
                return@get
            }
            if(id==null || id !in DataModel.dms.groups){
                call.response.status(HttpStatusCode.BadRequest)
                call.respond("Group does not exist or has been deleted.")
                return@get
            }
            for((x,_) in DataModel.dms.groups[id]!!.members){
                DataModel.dms.users[x]?.groups?.remove(id)
            }
            DataModel.dms.groups.remove(id)
            DataModel.save()
            call.respondRedirect("/groups")
        }
        post("/userList/{id}"){
            val id=call.parameters["id"]
            if(id==null || id !in DataModel.dms.groups){
                call.response.status(HttpStatusCode.BadRequest)
                call.respond("Group does not exist or has been deleted.")
                return@post
            }
            call.respondText(DataModel.dms.groups[id]!!.members.keys.joinToString("<br/>"))
        }

        post("/join/{id}"){
            val usr=user()
            if(usr==null){
                call.response.status(HttpStatusCode.Unauthorized)
                call.respond("Please login first.")
                return@post
            }

            val id=call.parameters["id"]
            if(id==null || id !in DataModel.dms.groups){
                call.response.status(HttpStatusCode.BadRequest)
                call.respond("Group does not exist or has been deleted.")
                return@post
            }

            if(id in usr.groups){
                usr.groups.remove(id)
                DataModel.dms.groups[id]!!.members.remove(usr.name)
                call.respond("success_leave")
            }else{
                usr.groups[id]=LocalDateTime.now().toString()
                DataModel.dms.groups[id]!!.members[usr.name]="member"
                call.respond("success_join")
            }

            DataModel.save()
        }

        get("/complete/{id}") {
            val res=DataModel.findDDL(call.parameters["id"])
            if(res==null){
                call.respond("No such event")
                return@get
            }

            val usr=user()
            if(usr!=null){
                if(res.internalID in usr.completed){
                    usr.completed.remove(res.internalID)
                }else{
                    usr.completed[res.internalID]=LocalDateTime.now().toString()
                }
                DataModel.save()

                call.respondRedirect("/index")
            }else{
                call.respond("This page login only.")
            }
        }

        post("/addGroup"){
            if(user()==null){
                call.response.status(HttpStatusCode.Unauthorized)
                return@post
            }
            val p=call.receiveParameters()
            if(p["name"]==null){
                call.response.status(HttpStatusCode.BadRequest)
                return@post
            }

            val grp=UserGroup(
                UUID.randomUUID().toString().replace("-","_"),
                p["name"]!!,
                LocalDateTime.now(),
                username()!!,
                mutableMapOf(username()!! to "creator")
            )

            user()!!.groups[grp.id]=LocalDateTime.now().toString()
            DataModel.dms.groups[grp.id]=grp
            DataModel.save()
            call.respondRedirect("/groups")
        }

        post("/doAdd") {
            try {
                if(user()==null){
                    call.response.status(HttpStatusCode.Unauthorized)
                    return@post
                }

                val p = call.receiveParameters()

                val endtime = if (p["endtime"] == null || p["endtime"] == "") {
                    p["starttime"]!!
                } else {
                    p["endtime"]!!
                }

                println(p["visibility"])
                println(p["group"])

                val cc = DDL(
                    p["name"]!!,
                    LocalDateTime.parse(p["starttime"]!!),
                    LocalDateTime.parse(endtime),
                    p["tag"]!!.split(",").map { it.trim() }.toMutableList(),
                    username()!!,
                    p["desc"]!!,
                    enumValues<Importance>()[p["importance"]!!.toInt()],
                    LocalDateTime.now(),
                    UUID.randomUUID().toString().replace("-", "_"),
                    enumValues<Visibility>()[p["visibility"]!!.toInt()],
                    p["group"]!!.split(",").toMutableList()
                )
                DataModel.addDDL(cc)
                println("Successfully added the given ddl")
                call.respondRedirect("index")
            } catch (e: Exception) {
                call.response.status(HttpStatusCode.BadRequest)
                call.respond("Failed to process queries as: $e")
            }
        }

        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("static")
        }
    }
}
