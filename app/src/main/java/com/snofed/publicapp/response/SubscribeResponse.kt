package com.snofed.publicapp.response

import com.snofed.publicapp.dto.SubscribeDTO

class SubscribeResponse (
       val success : Boolean,
       val message: String,
       val data: SubscribeDTO?,
       val statusCode: Int,
       val totalItems : Int
)