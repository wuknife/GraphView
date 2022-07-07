package dev.bandb.graphview.util

/**
 * display view interface
 */
interface DisplayView {
//   public val viewType : Int
//    public var imageUrl : String
//    public var imageWidth : Int
//    public var imageHeight : Int
    /**
     * get DisplayView Key
     */
    public fun getKey() : String

    /**
     * set DisplayView Key
     */
    public fun setKey( paramKey: String)
}