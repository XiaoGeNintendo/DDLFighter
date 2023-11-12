package top.hellholestudios.xgn.data

import java.time.LocalDateTime

data class UserGroup(
    val id:String,
    val name:String,
    val createdTime: LocalDateTime,
    /**
     * Username
     */
    val creator: String,
    /**
     * Key is username
     */
    val members: MutableMap<String,String>,
){
    fun getMemberCount():Int = members.size
}
