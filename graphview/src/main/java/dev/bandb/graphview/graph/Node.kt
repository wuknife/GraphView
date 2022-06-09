package dev.bandb.graphview.graph

import dev.bandb.graphview.util.DisplayView
import dev.bandb.graphview.util.Size
import dev.bandb.graphview.util.VectorF

data class Node(var data: DisplayView)  {
    // TODO make private
    val position: VectorF = VectorF()
    val size: Size = Size()
//    id
    var id : String
        get() = id
        set(value) {
            id = value
        }
    var test1 : String
        get() = test1
        set(value) {
            test1 = value
        }
    var note: Note = Note(listOf(this.data))

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
}
