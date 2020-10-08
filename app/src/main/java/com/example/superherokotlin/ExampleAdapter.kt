package com.example.superherokotlin


import android.content.Intent
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_hero.view.*

class ExampleAdapter(private val examplelist:List<UserResponse>, context: MainActivity) : RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder>() {

    val Mycontext=context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleViewHolder {
       val itemView =LayoutInflater.from(parent.context).inflate(R.layout.row_hero, parent, false)

        return ExampleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExampleViewHolder, position: Int) {
        val currentItem = examplelist[position]

        val picasso = Picasso.get()
        picasso.load(currentItem.url).into( holder.imageView)

        holder.textview.text = currentItem.name

        val intent = Intent(Mycontext,  herodet::class.java)
        intent.putExtra("id",currentItem.id)
        intent.putExtra("name",currentItem.name)
        intent.putExtra("url",currentItem.url)
        holder.itemView.setOnClickListener{
            Mycontext.startActivity(intent)
        }

    }







    override fun getItemCount()=examplelist.size

    class ExampleViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        val imageView: ImageView= itemView.imageView_1
        val textview: TextView = itemView.heroname



    }

}

