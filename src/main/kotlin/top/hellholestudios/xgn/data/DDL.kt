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

    fun descString():String{
        return desc.replace("<br/>","\n").trim()
    }
    fun visibleGroupString():String{
        return visibleGroups.joinToString(",")
    }

    fun tagString():String{
        return tag.joinToString(", ")
    }

    fun contains(time: LocalDateTime):Boolean{
        return time in timeStart..timeEnd
    }

    fun isVisibleTo(user: User?): Boolean{
        if(visibility==Visibility.Public){
            return true
        }
        if(visibility==Visibility.Private){
            return user?.name==uploader
        }
        return visibleGroups.any { user?.inGroup(it)==true }
    }

    /**
     * A DDL is considered archived if it has ended for 90 days
     * @return if the ddl is archived
     */
    fun isArchived(): Boolean{
        val now=LocalDateTime.now()
        return now.minusDays(90)>=timeEnd
    }

    fun isNoDuration():Boolean{
        return timeEnd<=timeStart
    }

    fun isFinishedBy(user: User?): Boolean{
        if(user==null){
            return false
        }

        return this.internalID in user.completed
    }

    /**
     * Not independent.
     */
    fun getVisibilityTag():String{
        return when (visibility) {
            Visibility.Public -> {
                "Public - Everyone can see"
            }
            Visibility.Private -> {
                "Private - Only you can see"
            }
            else -> {
                visibleGroups.joinToString(", "){DataModel.dms.groups[it]!!.name}
            }
        }
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
