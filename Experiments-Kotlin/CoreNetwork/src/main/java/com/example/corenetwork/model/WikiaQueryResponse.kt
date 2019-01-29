package com.example.corenetwork.model

import com.google.gson.annotations.SerializedName

object WikiaQueryResponse{
    data class Response(val query:Query)
    data class Query(val pages:Pages)
    data class Pages(@SerializedName("633093") val page633093:Page633093)
    data class Page633093(val pageid:String)
}