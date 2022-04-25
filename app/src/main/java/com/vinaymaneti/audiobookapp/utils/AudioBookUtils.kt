package com.vinaymaneti.audiobookapp.utils

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import com.vinaymaneti.audiobookapp.AudioBookApplication
import com.vinaymaneti.audiobookapp.AudioBooksModel
import com.vinaymaneti.audiobookapp.R
import com.vinaymaneti.audiobookapp.TAG
import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

object AudioBookUtils {



    private val context = AudioBookApplication.applicationContext()
    fun addItemsFromJSON() : MutableList<AudioBooksModel> {
        val viewItems: MutableList<AudioBooksModel> = mutableListOf()
        try {
            val jsonDataString = readJSONDataFromFile()
            val jsonArray = JSONArray(jsonDataString)
            for (i in 0 until jsonArray.length()) {
                val itemObj = jsonArray.getJSONObject(i)

                val bookTitleName = itemObj.getString("book_title")

                val authorName = itemObj.getString("author_name")

                // get image name from JSON
                val imageName: String = itemObj.getString("cover_image")

                // get resource id by image name
                val resourceId = context.resources.getIdentifier(imageName, "drawable", context.packageName)

                // get drawable by resource id
                val drawable = context.resources.getDrawable(resourceId)

                // get bitmap by resource id
                val bitmap = BitmapFactory.decodeResource(context.resources, resourceId)

                val audioPath = itemObj.getString("audio_path")
                val audioBooksModel = AudioBooksModel(bitmap, bookTitleName, authorName, audioPath)

                viewItems.add(audioBooksModel)
            }
        } catch (e: JSONException) {
            Log.d(TAG, "addItemsFromJSON: ", e)
        } catch (e: IOException) {
            Log.d(TAG, "addItemsFromJSON: ", e)
        }
        return viewItems
    }

    @Throws(IOException::class)
    private fun readJSONDataFromFile(): String {
        var inputStream: InputStream? = null
        val builder = StringBuilder()
        try {
            var jsonString: String? = null
            inputStream = context.resources.openRawResource(R.raw.audio_books)
            val bufferedReader = BufferedReader(
                InputStreamReader(inputStream, "UTF-8")
            )
            while (bufferedReader.readLine().also { jsonString = it } != null) {
                builder.append(jsonString)
            }
        } finally {
            inputStream?.close()
        }
        return String(builder)
    }
}