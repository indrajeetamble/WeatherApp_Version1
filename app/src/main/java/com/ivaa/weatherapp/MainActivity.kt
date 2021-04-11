package com.ivaa.weatherapp

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.loader.content.AsyncTaskLoader
import org.json.JSONObject
import java.lang.Exception
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

@Suppress("UNREACHABLE_CODE")
class MainActivity : AppCompatActivity() {


    var City : String ="Kolhapur, IN"
    val API : String = "6833e07eaba3e6fd358d9c4d078fcd09"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getSupportActionBar()?.hide();

        weatherTask().execute()
    }

    inner class weatherTask() : AsyncTask<String, Void , String>(){

        override fun onPreExecute() {
            super.onPreExecute()


        }

        override fun doInBackground(vararg params: String?): String? {

            var response: String?
            try {
                response = URL("https://api.openweathermap.org/data/2.5/weather?q=$City&units=metric&appid=$API").readText(Charsets.UTF_8)
            }
            catch (e: Exception)
            {
               response = null
                findViewById<TextView>(R.id.temp).setText("Opps")
            }

            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            try {
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                val updatedAt:Long =jsonObj.getLong("dt")

                val updatedAtText = "Updated At: "+SimpleDateFormat("dd/MM/yyyy hh:mm a" , Locale.ENGLISH).format(
                    Date(updatedAt*1000)
                )
                val temp = main.getString("temp") + "°C"
                val tempMin = "Min Temp : " + main.getString("temp_min") + "°C"
                val tempMax = "Max Temp : " + main.getString("temp_max") + "°C"
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")

                val sunrise = sys.getLong("sunrise")
                val sunset = sys.getLong("sunset")
                val windSpeed =wind.getString("speed")
                val description = weather.getString("description")
                val address= jsonObj.getString("name") +" "+ sys.getString("country")


                findViewById<TextView>(R.id.location).text = address
                findViewById<TextView>(R.id.temp).text = temp
                findViewById<TextView>(R.id.minTemp).text = tempMin
                findViewById<TextView>(R.id.maxTemp).text = tempMax
                findViewById<TextView>(R.id.pressure).text = pressure
                findViewById<TextView>(R.id.humidity).text = humidity
                findViewById<TextView>(R.id.sunrise).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(sunrise*1000)
                findViewById<TextView>(R.id.sunset).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(sunset*1000)
                findViewById<TextView>(R.id.wind).text = windSpeed
                findViewById<TextView>(R.id.status).text = description
                findViewById<TextView>(R.id.updatedAt).text = updatedAtText


            }

            catch (e:Exception){

                findViewById<TextView>(R.id.status).setText("something went wrong")
            }
        }
    }
}