package dev.bandb.graphview.util

data class Image( var width:Int = 85, var height:Int = 114 ) :DisplayView{
    public lateinit var url:String


    private var key = ""

    constructor(url:String) : this() {
        this.url = url
    }
    private var viewType = "Image"
    override fun getKey():String {
        return key
    }

    override fun setKey(tempKey: String) {
        key = tempKey
    }
}