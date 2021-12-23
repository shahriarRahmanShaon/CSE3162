package bd.ac.ru.cse3162

import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import okhttp3.OkHttpClient
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices

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

                        var lat = location!!.latitude  // we're sure we gonna get a response, so used !! but not recommended
                        var lon = location!!.longitude



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


    }
}