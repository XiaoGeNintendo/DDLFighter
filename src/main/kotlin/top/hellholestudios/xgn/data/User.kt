package top.hellholestudios.xgn.data

data class User(
    val name: String,
    /**
     * Hash is the hash of his username and password
     */
    val hash: String,
    val completed: MutableMap<String,String>,
    /**
     * Key is ID
     */
    val groups: MutableMap<String,String>,
    val admin: Boolean,
    val internalID: String
){

    fun inGroup(id: String):Boolean{
        return id in groups
    }

    /**
     * This function will call DataModel.dms and thus is not independent
     */
    fun getGroupsD():List<UserGroup>{
        return groups.keys.map { DataModel.dms.groups[it]!! }
    }

    fun completeDate(ddl: DDL?):String?{
        return completed[ddl?.internalID]
    }
}
