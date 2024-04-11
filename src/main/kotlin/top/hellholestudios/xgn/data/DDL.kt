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

    /**
     * @return `desc` with `\n` replaced by `<br/>`
     */
    fun descString():String{
        return desc.replace("<br/>","\n").trim()
    }

    /**
     * @return `visibleGroups` joined to string
     */
    fun visibleGroupString():String{
        return visibleGroups.joinToString(",")
    }

    /**
     * @return `tag` joined to string
     */
    fun tagString():String{
        return tag.joinToString(", ")
    }

    /**
     * @return whether `time` is in timeStart~timeEnd inclusive
     */
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

    /**
     * @return If timeEnd is smaller or equal to timeStart
     */
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
     * Frontend function.
     *
     * Not independent and uses [DataModel].
     *
     * @return user-friendly visibility string
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

    /**
     * Frontend Function
     *
     * Active means not completed and not finished
     * @return if the DDL is considered "active" for a certain user
     */
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
     * Frontend Function.
     *
     * @return the color name for displaying using current time
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

    /**
     * Frontend Function
     *
     * @return the icon to be used
     */
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
