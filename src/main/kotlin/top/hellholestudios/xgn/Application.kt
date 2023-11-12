package top.hellholestudios.xgn

import freemarker.cache.ClassTemplateLoader
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.freemarker.*
import io.ktor.server.netty.*
import io.ktor.server.sessions.*
import top.hellholestudios.xgn.data.DataModel
import top.hellholestudios.xgn.data.UsernameSession
import top.hellholestudios.xgn.plugins.*
import java.io.File


fun main(args: Array<String>) {
    var arg=args
    if(args.size<2){
        arg= arrayOf("0.0.0.0","8080")
        println("WARNING: Start on default port and ip")
    }

    DataModel.load()

    embeddedServer(Netty, port = arg[1].toInt(), host = arg[0], watchPaths = listOf("build"), module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(FreeMarker){
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }
    install(Authentication){
        basic("auth-basic"){
            validate{ cre->
                if(cre.name in DataModel.dms.users){
                    if(DataModel.dms.users[cre.name]!!.hash==cre.password){
                        UserIdPrincipal(cre.name)
                    }else{
                        null
                    }
                }else{
                    null
                }
            }
        }
    }
    install(Sessions){
        cookie<UsernameSession>("session_data",directorySessionStorage(File(".sessions"))){

        }
    }
    configureRouting()
}
