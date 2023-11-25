package app.climaec.wheather.recycler

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import app.climaec.wheather.databinding.ItemRecyclerBinding
import app.climaec.wheather.model.MyCurrentWheather
import app.climaec.wheather.pronostico.Pronostico
import com.bumptech.glide.Glide
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ClassViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding = ItemRecyclerBinding.bind(view)

    @RequiresApi(Build.VERSION_CODES.O)
    fun render(pronostico: Pronostico) {
        val imagen = pronostico.weather[0].icon
        val imageUrl = "https://openweathermap.org/img/w/$imagen.png"
        Glide.with(binding.tvImage).load(imageUrl).into(binding.tvImage)
        binding.tvItemTem.text = "${pronostico.main.temp.toInt()} °C"
        binding.tvItemStatus.text = "${pronostico.weather[0].description}"
        binding.tvItemTime.text = displayItem(pronostico.dt_txt)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun displayItem(dtTxt: String): CharSequence? {
        val input = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val output = DateTimeFormatter.ofPattern("MM-dd HH:mm")
        val dateTime = LocalDateTime.parse(dtTxt, input)
        return output.format(dateTime)

    }

}