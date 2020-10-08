package com.example.superherokotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.security.AccessControlContext

class herodet : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_herodet)
        val name = intent.getStringExtra("name")
        val id = intent.getStringExtra("id")?.toInt()
        val url = intent.getStringExtra("url")
        Toast.makeText(this,name , Toast.LENGTH_SHORT)
        if (name != null) {
            Log.d("TAG/////////////", name)
        }

        val textViewname = findViewById<TextView>(R.id.textViewname)
        val textViewdatos = findViewById<TextView>(R.id.textViewdatos)
        val imageView = findViewById<ImageView>(R.id.imageView)
        val picasso = Picasso.get()
        picasso.load(url).into( imageView)
        textViewname.text=name
        if (id != null) {
            getdata(id,this)
        }



    }
}





data class dataResponse( val intelligence: String, val strength: String, val speed: String,val durability: String, val power: String, val combat: String)
interface dataService {
    @GET("api/10156112965520834/{ID}/powerstats")
    fun getdata(@Path("ID") ID: Int?): Call<dataResponse>
}
fun getdata(ID: Int, context: herodet){
    //https://www.superheroapi.com/api.php/10156112965520834/1/
    val url ="https://www.superheroapi.com/"
    val service = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(dataService::class.java)

    service.getdata(ID).enqueue(object : Callback<dataResponse> {

        override fun onFailure(call: Call<dataResponse>, t: Throwable) {
            Log.d("TAG_", "An error happened!")
            t.printStackTrace()

        }

        override fun onResponse(call: Call<dataResponse>, response: Response<dataResponse>) {
            Log.d("TAG_", response.body().toString())
            val partsNonRegex = response.body().toString()
                .split(",") //No Regex. This will split on the period character
            val a1= partsNonRegex.component1().split("=").component2()
            val a2= partsNonRegex.component2().split("=").component2()
            val a3= partsNonRegex.component3().split("=").component2()
            val a4= partsNonRegex.component4().split("=").component2()
            val a5= partsNonRegex.component5().split("=").component2()
            val a6 = partsNonRegex.last().split("=").component2().dropLast(1)
            val textViewdatos = context.findViewById<TextView>(R.id.textViewdatos)
            val datosformateados="Inteligencia="+a1+"\nFuerza="+a2+"\nVelocidad="+a3+"\nDurabilidad="+a4+"\nPoder="+a5+"\nCombate="+a6


            textViewdatos.text= datosformateados


        }
    })
}