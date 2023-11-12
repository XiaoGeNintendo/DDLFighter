package top.hellholestudios.xgn.data

import java.time.LocalDateTime
import java.util.*

data class DDL(
    val name: String,
    val timeStart: LocalDateTime,
    val timeEnd: LocalDateTime,
    val tag: MutableList<String>,
    val uploader: String,
    val desc: String,
    val importance: Importance,
    val addDate: LocalDateTime,
    val internalID: String,
    val visibility: Visibility,
    val visibleGroups: MutableList<String>
){

    fun contains(time: LocalDateTime):Boolean{
        return time in timeStart..timeEnd
    }

    fun active(usr: User?):Boolean{
        if(getColor(usr)=="grey"){
            return false
        }
        if(usr==null){
            return true
        }
        if(this.internalID in usr.completed){
            return false
        }
        return true
    }
    /**
     * This gets the color for display using current time
     */
    fun getColor(user: User?):String{
        if(user!=null && internalID in user.completed){
            return "green"
        }

        val now=LocalDateTime.now()
        return if(now>timeEnd){
            //ended
            "grey"
        }else{
            importance.color()
        }
    }

    fun getIcon(user: User?):String{
        if(user!=null && internalID in user.completed){
            return "check double"
        }
        val now=LocalDateTime.now()
        return if(now>timeEnd){
            //ended
            "bell slash"
        }else{
            importance.icon()
        }
    }

    @Deprecated("Use internalID instead", ReplaceWith("internalID"))
    fun hashName():Int{
        return name.hashCode()
    }
}
