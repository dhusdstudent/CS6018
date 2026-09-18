package com.example.assignment_03

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import androidx.core.content.edit

class ShakeLog( context: Context) {
    private val preferences = context.getSharedPreferences(
        "shake_backlog",
        Context.MODE_PRIVATE
    )

    private val key = "shakes"

    fun getShakes(): List<Shake> {
        val json = preferences.getString(key, "[]") ?: "[]"
        val array = JSONArray(json)

        return buildList {
            for (i in 0 until array.length()){
                val objectAtIndex = array.getJSONObject(i)

                add(Shake(
                    timestamp = objectAtIndex.getLong("timestamp"),
                        vigor = objectAtIndex.getDouble("vigor").toFloat()
                    )
                )
            }
        }
    }

    fun saveShakes(shakes: List<Shake>){
        val array = JSONArray()

        for (shake in shakes){
            val objectToSave = JSONObject()

            objectToSave.put("timestamp", shake.timestamp)
            objectToSave.put("vigor", shake.vigor)
            array.put(objectToSave)
        }

        preferences.edit { putString(key, array.toString()) }
    }
    fun deleteOldOnes(cutoff:Long ){
        val remaining = getShakes().filter {
            it.timestamp >= cutoff
        }
        saveShakes(remaining)
    }
}