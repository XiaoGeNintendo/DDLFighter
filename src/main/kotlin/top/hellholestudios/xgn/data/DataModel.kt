package top.hellholestudios.xgn.data

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import top.hellholestudios.xgn.patch.LocalDateTypeAdapter
import java.io.File
import java.nio.charset.Charset
import java.time.LocalDateTime

object DataModel {
    @Transient
    val gson = GsonBuilder().registerTypeAdapter(LocalDateTime::class.java,LocalDateTypeAdapter()).create()

    class DataModelSon{
        var ddls: MutableList<DDL> = ArrayList()
        var users: MutableMap<String,User> = HashMap()
        var groups: MutableMap<String,UserGroup> = HashMap()
    }

    var dms = DataModelSon()

    // some caches
    var emergencies: List<DDL> = ArrayList()
    var ongoing: List<DDL> = ArrayList()
    var ended: List<DDL> = ArrayList()
    var future: List<DDL> = ArrayList()
    var newlyAdded: List<DDL> = ArrayList()

    fun load() {
        val f = File("data.json")
        if (!f.exists()) {
            f.createNewFile()
        }
        val other = gson.fromJson(f.readText(Charset.forName("utf-8")), DataModelSon::class.java)
        if(other!=null){
            this.dms=other
        }
        println("Loaded DDLs: ${dms.ddls.size}")

        recache(true)
    }

    fun save(){
        val f=File("data.json")
        if(!f.exists()){
            f.createNewFile()
        }
        val t= gson.toJson(dms)
        println(t)
        f.writeText(t)
        println("Saved DDLs")
    }

    fun recache(sort:Boolean=false) {
        val now = LocalDateTime.now()

        val ddls=dms.ddls
        if(sort) {
            ddls.sortBy { it.timeStart }
        }

        emergencies=ddls.filter { it.timeEnd>now && it.importance==Importance.Emergency }
        ongoing=ddls.filter { it.contains(now) }
        ended=ddls.filter { it.timeEnd<now }.reversed()
        future=ddls.filter { it.timeStart>now }
        newlyAdded=ddls.filter { it.addDate>=LocalDateTime.now().minusHours(24) }.sortedBy { it.addDate }.reversed()
    }

    /**
     * THis will automatically recache and save the DMS
     */
    fun editDDL(id: String, cc: DDL){
        dms.ddls[dms.ddls.indexOfFirst { it.internalID==id }]=cc
        recache(true)
        save()
    }
    /**
     * This will automatically recache and save the DMS
     */
    fun addDDL(cc: DDL) {
        dms.ddls.add(cc)
        recache(true)
        save()
    }

    /**
     * @param id
     */
    fun findDDL(s: String?):DDL?{
        if(s==null){
            return null
        }
        return dms.ddls.find { it.internalID == s }
    }

    fun removeDDL(s: String?):Boolean {
        if(s==null){
            return false
        }

        val x=dms.ddls.removeIf { it.internalID == s }
        recache(false)
        save()
        return x
    }

    fun userGroups():List<UserGroup>{
        return dms.groups.values.toList()
    }
}