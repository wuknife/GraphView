package dev.bandb.graphview.graph

import dev.bandb.graphview.util.DisplayView
import dev.bandb.graphview.util.Size
import dev.bandb.graphview.util.VectorF

/**
 * Note,display DisplayView List
 */
data class Note(val timestamp:Long){

    var  data: MutableList<DisplayView> = mutableListOf<DisplayView>()

    val position: VectorF = VectorF()
    val size: Size = Size()

    var height: Int
        get() = size.height
        set(value) {
            size.height = value
        }

    var width: Int
        get() = size.width
        set(value) {
            size.width = value
        }

    var x: Float
        get() = position.x
        set(value) {
            position.x = value
        }

    var y: Float
        get() = position.y
        set(value) {
            position.y = value
        }

    fun setSize(width: Int, height: Int) {
        this.width = width
        this.height = height
    }

    fun setPosition(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    fun setPosition(position: VectorF) {
        this.x = position.x
        this.y = position.y
    }

    fun addData(displayView: DisplayView){
        data.add(displayView)
    }

    fun addDatas(displayViews: List<DisplayView>){
        data.addAll(displayViews)
    }
    fun clearData(){
        data.clear()
    }
}
