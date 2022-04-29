package com.example.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target


class MainActivity : AppCompatActivity() {

    //used to globalise the use of Url outside the Loadmeme function within the MainActivity
    var currentImageUrl: String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadmeme()
    }

    private fun loadmeme(){
        // Instantiate the RequestQueue.

        //adding a progressbar until the meme is loading
        val progresssbar = findViewById<ProgressBar>(R.id.progress_bar)
        progresssbar.visibility = View.VISIBLE

        //val queue = Volley.newRequestQueue(this)
        //val queue = MySingleton.getInstance(this.applicationContext).requestQueue
        val url = "https://meme-api.herokuapp.com/gimme"

       // Request a string response from the provided URL.
       val jsonObjectRequest = JsonObjectRequest(Request.Method.GET , url , null,
           { response ->
               currentImageUrl = response.getString("url")
               val imageview = findViewById<ImageView>(R.id.MemeShareImage)

               //only glide is taking time in loading and we have to add a listener to hide progress
               //bar when the image is ready to display
               Glide.with(this).load(currentImageUrl).listener(object :RequestListener<Drawable> {

                   override fun onLoadFailed(
                       e: GlideException?,
                       model: Any?,
                       target: Target<Drawable>?,
                       isFirstResource: Boolean
                   ): Boolean {
                       progresssbar.visibility = View.GONE
                       return false
                   }

                   override fun onResourceReady(
                       resource: Drawable?,
                       model: Any?,
                       target: Target<Drawable>?,
                       dataSource: DataSource?,
                       isFirstResource: Boolean
                   ): Boolean {
                       progresssbar.visibility = View.GONE
                       return false
                   }

               }).into(imageview)
           },
           {
               Toast.makeText(this,"something went wrong" , Toast.LENGTH_SHORT).show()
           })

        //add another object to the queue
       MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

    }

    fun shareMeme(view: View) {

        /* Adding functionalities in the function */
        val intent = Intent(Intent(Intent.ACTION_SEND))
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT ,"Hey! I am sharing this ROFL meme $currentImageUrl")
        val chooser = Intent.createChooser(intent , "share image using these....")
        startActivity(chooser)

    }
    fun nextMeme(view: View) {

        //calling loadmeme() function to load the next meme.
        loadmeme()

    }
}