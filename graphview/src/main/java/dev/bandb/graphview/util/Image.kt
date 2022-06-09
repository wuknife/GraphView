package dev.bandb.graphview.util

data class Image( var width:Int = 85, var height:Int = 114 ) :DisplayView{
    public lateinit var url:String


    constructor(url:String) : this() {
        this.url = url
    }
    private var viewType = "Image"
    override fun getName(): String {
        return viewType
    }
    public fun test():String{
        return "test"
    }
}