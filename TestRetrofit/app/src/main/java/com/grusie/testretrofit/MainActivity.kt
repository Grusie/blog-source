package com.grusie.testretrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.gson.GsonBuilder
import com.grusie.testretrofit.model.UserInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    lateinit var btnSend: Button
    lateinit var etId: EditText
    lateinit var tvResult: TextView
    lateinit var retrofit : Retrofit
    lateinit var retrofitService : ApiInterface
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofitService = retrofit.create(ApiInterface::class.java)

        btnSend = findViewById(R.id.btn_send)
        tvResult = findViewById(R.id.tv_result)
        etId = findViewById(R.id.et_id)

        btnSend.setOnClickListener{
            retrofitService.getUserData(id = if(!etId.text.isNullOrEmpty()) etId.text.toString() else null).enqueue(object: Callback<UserInfo>{
                override fun onResponse(call: Call<UserInfo>, response: Response<UserInfo>) {
                    if(response.isSuccessful){
                        //정상적으로 통신이 성공한 경우
                        val result = response.body()
                        tvResult.text = result.toString()
                        Log.e(TAG, "onResponse 성공 : $result")
                    } else {
                        //통신 실패(응답 코드 3xx, 4xx 등)
                        Log.e(TAG, "onResponse 실패 : ${response.errorBody()}")
                    }
                }

                override fun onFailure(call: Call<UserInfo>, t: Throwable) {
                    //통신 실패 (인터넷 끊킴, 예외 발생 등 시스템적인 이유)
                    Log.e(TAG, "onFailure ${t.message}" )
                }
            })
        }
    }

    companion object {
        const val TAG = "MAIN_ACTIVITY"
    }
}