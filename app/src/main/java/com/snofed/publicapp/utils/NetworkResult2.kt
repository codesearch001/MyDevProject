//package com.snofed.publicapp.utils
//
//import android.icu.util.Output
//import android.os.Build
//import android.util.Log
//import androidx.annotation.RequiresApi
//import retrofit2.Response
//import java.io.IOException
//
//open class NetworkResult2 {
//    @RequiresApi(Build.VERSION_CODES.N)
//    suspend fun <T : Any> safeApiCall(call : suspend()-> Response<T>, error : String) :  T?{
//        val result = newsApiOutput(call, error)
//        var output : T? = null
//        when(result){
//            is Output.Success ->
//                output = result.output
//
//            is Output.Error -> Log.e("Error", "The $error and the ${result.exception}")
//        }
//        Log.i("sssssss", "The  and the "+output)
//        return output
//
//    }
//    private suspend fun<T : Any> newsApiOutput(call: suspend()-> Response<T> , error: String) : Output<T> {
//        val response = call.invoke()
//        //Common.app_id=response.toString()
//        return if (response.isSuccessful)
//            Output.Success(response.body()!!)
//        else
//            Output.Error(IOException("OOps .. Something went wrong due to  $error"))
//    }
//}