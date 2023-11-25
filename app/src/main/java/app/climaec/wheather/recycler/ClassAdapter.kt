package app.climaec.wheather.recycler

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import app.climaec.wheather.R
import app.climaec.wheather.pronostico.Pronostico

class ClassAdapter(private val forescatArray: ArrayList<Pronostico>) :RecyclerView.Adapter<ClassViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        return ClassViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.item_recycler,parent,false
        ))
    }

    override fun getItemCount()=forescatArray.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        val position=forescatArray[position]
        holder.render(position)
    }
}