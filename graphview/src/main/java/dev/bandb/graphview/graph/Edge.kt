package dev.bandb.graphview.graph

import dev.bandb.graphview.util.DisplayView
import java.sql.Timestamp

data class Edge(val source: Node, val destination: Node, val timestamp:Long) {

    var note: Note = Note(System.currentTimeMillis())
//    id
    var id : String ?= null

    var test2 : String ?= null

//    显示对象
    public var display : DisplayView ?= null


}