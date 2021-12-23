package bd.ac.ru.cse3162

import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {
    // Ui component declaration
    lateinit var search: Button
    lateinit var lat: TextView
    lateinit var lon: TextView
    lateinit var viewWeather: Button
    lateinit var city_date: TextView
    lateinit var celciousView: TextView
    lateinit var farenhiteView: TextView

    // initilizing library and frameworks
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // connecting them with this activity
        search = findViewById(R.id.bSearch)
        lat = findViewById(R.id.lat)
        lon = findViewById(R.id.lon)
        viewWeather = findViewById(R.id.weatherBtn)
        city_date = findViewById(R.id.cityAndName)
        celciousView = findViewById(R.id.celciusTemp)
        farenhiteView = findViewById(R.id.farenTemp)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        requestPermissionLauncher = registerForActivityResult( ActivityResultContracts.RequestPermission())
        { isGranted: Boolean ->
            if (isGranted) {
                Log.i("Permission: ", "Granted")

            } else {
                Log.i("Permission: ", "Denied")
            }
        }
        // initializing button press for Location Button
        search.setOnClickListener{
            when {
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
                    // The permission is granted
                    // doing the task that needs location permission

                    fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
                        // Got last known location with listener in it ignoring error checking

                        var latFromFused = location!!.latitude  // we're sure we gonna get a response, so used !! but not recommended
                        var lonFromFused = location!!.longitude

                        Log.d("latitude", latFromFused.toString())
                        Log.d("latitude", lonFromFused.toString())
                        var hold= getWeather(lat = latFromFused.toString(), lon = lonFromFused.toString())

                        runOnUiThread {
                            lat.setText(latFromFused.toString())
                            lon.setText(lonFromFused.toString())
                        }




                    }
                }
                shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    Toast.makeText(this, "message", Toast.LENGTH_SHORT).show() // showing message why we need location info
                    requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
                }
                else -> {
                    // Everything is fine you can simply request the permission
                    requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
        }





         fun getAddress(lat: Double, lng: Double): String {
            // Function to get location city name using geocoder
            var geocoder = Geocoder(this) // initializing geocoder for the context
            var list = geocoder.getFromLocation(lat, lng, 1) // getting location using lat long
            return list[0].locality  // as it returns a list object we fetch only the local name
        }
    }

    private fun getWeather(lat: String, lon: String) {
        //function to fetch weather data from openWeatherApi using OKHttps3

        var url = "https://api.weatherbit.io/v2.0/current?lat=${lat}&lon=${lon}&key=edec539ec9524340bc48c18fa9a77221" // making dynamic api url and personal key
        var request = Request.Builder() // making a builder request with url just made above
            .url(url)
            .build()
        // making request call
        client.newCall(request).enqueue(object : Callback {
            // Acctually making a request constantly which return two callback named onFailure and Onresponse

            override fun onFailure(call: Call, e: IOException) {
                // We simply catch the error
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                // Do the things we plan to do if the request is successful

                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    var getString = response.body!!.string() // get the response body from response and map that into string

                    // now we need to map the response into dataclass object
                    var gson = GsonBuilder().create() // build gson instance
                    var result =  gson.fromJson(getString, Welcome::class.java) // map the response into welcome.kt data class

                    var temp =  result.data[0].temp.toString()
                    Log.d("response", temp)
                    var celciousToFarenhite = result.data[0].temp + 273.5

                    runOnUiThread {
                        celciousView.setText(temp)
                        farenhiteView.setText(celciousToFarenhite.toString())
                    }
                }
            }
        })
    }
}