package dev.bandb.graphview.util

/**
 * Lable data
 */
data class Label constructor(var text: String ?,var size: Int =10) :DisplayView{

    private var viewType = "Label"

    override fun getName():String {
        return viewType
    }
}
