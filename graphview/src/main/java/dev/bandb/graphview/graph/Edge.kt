package dev.bandb.graphview.graph

import dev.bandb.graphview.util.DisplayView

data class Edge(val source: Node, val destination: Node) {

    var note: Note = Note(null)
//    id
    var id : String
        get() = id
        set(value) {
            id = value
        }
    var test2 : String
        get() = test2
        set(value) {
            test2 = value
        }
//    显示对象
    public var display : DisplayView
        get() = display
        set(value) {
            display = value
        }

}