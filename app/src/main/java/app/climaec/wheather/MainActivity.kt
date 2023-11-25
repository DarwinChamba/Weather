package app.climaec.wheather

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.climaec.wheather.databinding.ActivityMainBinding
import app.climaec.wheather.databinding.BottonSheetLayoutBinding
import app.climaec.wheather.`object`.ObjectRetrofit
import app.climaec.wheather.pronostico.Pronostico
import app.climaec.wheather.recycler.ClassAdapter
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sheetLayoutBinding: BottonSheetLayoutBinding
    private lateinit var dialog: BottomSheetDialog
    private var city:String="quito"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        sheetLayoutBinding = BottonSheetLayoutBinding.inflate(layoutInflater)
        dialog = BottomSheetDialog(this, R.style.BottomSheetTheme)
        setContentView(binding.root)
        dialog.setContentView(sheetLayoutBinding.root)
        //llamar funciones
        binding.search.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(!query.isNullOrEmpty()){
                    city=query
                }
                getCurrentWheather(city)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        getCurrentWheather(city)
        cambiarPantalla()
        binding.pronosticos.setOnClickListener {
            openDialog()
        }
    }

    private fun openDialog() {
        getForescat()
        sheetLayoutBinding.recyler.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@MainActivity, 1, RecyclerView.HORIZONTAL, false)
        }
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.show()
    }

    private fun getForescat() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = try {
                ObjectRetrofit.api.getForecast(
                    "quito",
                    "metric",
                    "f278d04bce78fb220aa6a605f2523434"
                )
            } catch (e: IOException) {
                Log.i("error", "${e.message.toString()}")
                return@launch
            } catch (e: HttpException) {
                Log.i("error", "${e.message.toString()}")
                return@launch
            }
            runOnUiThread {
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()!!
                    var forescatArray = arrayListOf<Pronostico>()
                    forescatArray = data.list as ArrayList<Pronostico>
                    val adapter = ClassAdapter(forescatArray)
                    sheetLayoutBinding.recyler.adapter = adapter
                    sheetLayoutBinding.textView.text = "five days forecast in ${data.city.name}"
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getCurrentWheather(city:String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = try {
                ObjectRetrofit.api.CurrenteWether(
                    city,
                    "metric",
                    "f278d04bce78fb220aa6a605f2523434"
                )
            } catch (e: IOException) {
                Log.i("error", "${e.message.toString()}")
                return@launch
            } catch (e: HttpException) {
                Log.i("error", "${e.message.toString()}")
                return@launch
            }
            runOnUiThread {
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()!!
                    val iconId = data.weather[0].icon
                    val urlImage = "https://openweathermap.org/img/w/$iconId.png"
                    Glide.with(this@MainActivity).load(urlImage).into(binding.imageView)
                    //amanecer
                    binding.atardecer.text = dateFormatConverter(
                        data.sys.sunset.toLong()
                    )

                    //atardecer
                    binding.amanecer.text = SimpleDateFormat(
                        "hh:mm a",
                        Locale.ENGLISH
                    ).format(Date(data.sys.sunrise.toLong() * 1000))

                    binding.apply {
                        locaion.text = "${data.name} ${data.sys.country}"
                        minTemp.text = "Min Tem: ${data.main.temp_min} °C"
                        temp.text = "${data.main.temp.toInt()} °C"
                        maxTemp.text = "Max Tem: ${data.main.temp_max} °C"
                        clear.text = "Temperatura como: ${data.main.feels_like} °C"
                        humedad.text = "${data.main.humidity} %"
                        presion.text = "${data.main.pressure} hPa"
                        viento.text = "${data.wind.speed} KM/H"
                        val res = SimpleDateFormat(
                            "hh:mm a",
                            Locale.ENGLISH
                        ).format(data.dt * 1000)
                        update.text = "Ultima actualización: $res"
                    }
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        response.code().toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }

    }

    private fun dateFormatConverter(toLong: Long): String {

        return SimpleDateFormat(
            "hh:mm a",
            Locale.ENGLISH
        ).format(Date(toLong * 1000))
    }

    private fun cambiarPantalla() {
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = getColor(R.color.colorPantalla)
    }
}