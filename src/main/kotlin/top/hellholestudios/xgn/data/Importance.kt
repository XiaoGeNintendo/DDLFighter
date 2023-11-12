package top.hellholestudios.xgn.data

import java.lang.IllegalArgumentException

enum class Importance {
    NotImportant,
    Normal,
    Important,
    VeryImportant,
    Emergency;

    fun icon():String{
        return if(this==NotImportant){
            "calendar alternate"
        }else if(this==Normal){
            "clock"
        }else if(this==Important){
            "stopwatch"
        }else if(this==VeryImportant){
            "exclamation circle"
        }else if(this==Emergency){
            "exclamation triangle"
        }else{
            throw IllegalArgumentException("Unknown enum value")
        }
    }

    fun color():String{
        return if(this==NotImportant){
            "black"
        }else if(this==Normal){
            "blue"
        }else if(this==Important){
            "yellow"
        }else if(this==VeryImportant){
            "red"
        }else if(this==Emergency){
            "violet"
        }else{
            throw IllegalArgumentException("Unknown enum value")
        }
    }
}