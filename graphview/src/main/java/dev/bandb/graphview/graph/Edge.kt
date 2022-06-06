package dev.bandb.graphview.graph

data class Edge(val source: Node, val destination: Node) {

    var note: Note = Note(null)

}