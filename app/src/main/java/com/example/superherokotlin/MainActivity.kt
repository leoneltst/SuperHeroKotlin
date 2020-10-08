package com.example.superherokotlin

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.lang.ref.WeakReference

//handler instance
var handler: Handler = Handler()
lateinit var adapter : ExampleAdapter
//Variable for checking progressbar loading or not
private var isLoading: Boolean = false
lateinit var layoutManager : LinearLayoutManager



val results: ArrayList<UserResponse> = ArrayList()

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        layoutManager = LinearLayoutManager(this)
        getimage(1, 10,this)
        addScrollerListener(this)


    }
}

data class UserResponse( val id: String, val name: String, val url: String)
interface UserService {
    @GET("api/10156112965520834/{ID}/image")
    fun getUsers(@Path("ID") ID: Int?): Call<UserResponse>
}
fun getimage(ID: Int?, counter: Int,context: MainActivity){
    //https://www.superheroapi.com/api.php/10156112965520834/1/
    val url ="https://www.superheroapi.com/"
    val service = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(UserService::class.java)
    if (counter == 10) {
        cargando(context)
    }
    service.getUsers(ID).enqueue(object : Callback<UserResponse> {

        override fun onFailure(call: Call<UserResponse>, t: Throwable) {
            Log.d("TAG_", "An error happened!")
            t.printStackTrace()
            cargaFinalizada(context)
        }

        override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
            Log.d("TAG_", response.body().toString().plus("    ").plus(counter))
            val partsNonRegex = response.body().toString()
                .split(",") //No Regex. This will split on the period character
            val id = partsNonRegex.component1().split("=").component2()
            val name = partsNonRegex.component2().split("=").component2()
            val url = partsNonRegex.component3().split("=").component2().dropLast(1)
            results.add(UserResponse(id,name, url))
            if (counter > 1) {
                if (ID != null) {
                    getimage(ID + 1, counter - 1,context)
                }
            }
            else{
                cargaFinalizada(context)
            }

        }
    })
}

fun cargando(context: MainActivity){
    val activityReference: WeakReference<MainActivity> = WeakReference(context)
    val activity = activityReference.get()
    if (activity != null) {
        activity.progressBar.visibility = View.VISIBLE
    }
}
fun cargaFinalizada(context: MainActivity){
    val activityReference: WeakReference<MainActivity> = WeakReference(context)
    val activity = activityReference.get()
    if (activity != null) {
        activity.progressBar.visibility = View.GONE
        context.recyclerView.adapter=ExampleAdapter(results,context)
        context.recyclerView.layoutManager=LinearLayoutManager(context)

    }
}


private fun addScrollerListener(context: MainActivity) {
    context.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (!isLoading) {
                if (layoutManager.findLastCompletelyVisibleItemPosition() == results.size - 1) {
                    loadMore(context)
                    isLoading = true
                }
            }
        }
    })
}
    private fun loadMore(context: MainActivity)
    {
        handler.post(Runnable
        {
            results.add(UserResponse("1","load","https://w7.pngwing.com/pngs/260/352/png-transparent-computer-icons-arrow-loading-monochrome-symbol-point-thumbnail.png"))
            adapter.notifyItemInserted(results.size - 1)
        })
        handler.postDelayed(Runnable {
            results.removeAt(results.size - 1)
            var listSize = results.size
            adapter.notifyItemRemoved(listSize)
            var nextLimit = listSize + 10
            getimage(nextLimit,10,context)
            adapter.notifyDataSetChanged()
            isLoading = false
        },2500)
    }

