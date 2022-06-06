package dev.bandb.graphview.util

/**
 * Lable data
 */
data class Label constructor(var text: String ?,var size: Int =20) :DisplayView{

    private var viewType = "Lable"

    override fun getName():String {
        return viewType
    }
}
