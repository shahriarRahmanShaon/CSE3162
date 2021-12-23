package bd.ac.ru.cse3162

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.location.FusedLocationProviderClient
import okhttp3.OkHttpClient
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

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

        // initializing button press for Location Button
        search.setOnClickListener{

        }


    }
}