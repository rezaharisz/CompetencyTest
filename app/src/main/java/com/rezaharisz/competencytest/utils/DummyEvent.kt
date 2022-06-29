package com.rezaharisz.competencytest.utils

import android.content.Context
import android.util.Log
import com.rezaharisz.competencytest.R
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

data class Event(
    val id: Int,
    val poster: String,
    val title: String,
    val description: String,
    val date: String,
    val time: String,
    val latitude: String,
    val longitude: String
)

object DummyEvent {

    fun getData(context: Context): ArrayList<Event>{
        val data = arrayListOf<Event>()
        val jsonArray = loadJsonArray(context)

        try {
            if (jsonArray != null){
                for (i in 0 until jsonArray.length()){
                    val item = jsonArray.getJSONObject(i)
                    data.add(
                        Event(
                            item.getInt("id"),
                            item.getString("poster"),
                            item.getString("title"),
                            item.getString("description"),
                            item.getString("date"),
                            item.getString("time"),
                            item.getString("latitude"),
                            item.getString("longitude")
                        )
                    )
                }
            }
        } catch (e: Exception){
            Log.e("JSON_EXCEPTION", e.message.toString())
        }

        return data
    }

    private fun loadJsonArray(context: Context): JSONArray?{
        val builder = StringBuilder()
        val `in` = context.resources.openRawResource(R.raw.dummy_event)
        val reader = BufferedReader(InputStreamReader(`in`))
        var line: String?
        try {
            while (reader.readLine().also { line = it } != null){
                builder.append(line)
            }
            val json = JSONObject(builder.toString())
            return json.getJSONArray("events")
        } catch (e: Exception){
            Log.d("DUMMY_EXCEPTION", e.message.toString())
        }
        return null
    }

}