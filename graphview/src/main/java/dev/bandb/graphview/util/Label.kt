package dev.bandb.graphview.util

/**
 * Lable data
 */
data class Label constructor(var text: String ?,var size: Int =10) :DisplayView{

    private var viewType = "Label"
    private var key = ""
    override fun getKey():String {
        return key
    }

    override fun setKey(tempKey: String) {
       key = tempKey
    }

}
