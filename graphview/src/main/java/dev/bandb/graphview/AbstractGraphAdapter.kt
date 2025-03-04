package dev.bandb.graphview

import android.view.View
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import dev.bandb.graphview.graph.Graph
import dev.bandb.graphview.graph.Node

abstract class AbstractGraphAdapter<VH : RecyclerView.ViewHolder>(var view:View) : RecyclerView.Adapter<VH>( ) {
    public  var rootView = view
    var graph: Graph? = null
    override fun getItemCount(): Int = graph?.nodeCount ?: 0

    open fun getNode(position: Int): Node? = graph?.getNodeAtPosition(position)
    open fun getNodeData(position: Int): Any? = graph?.getNodeAtPosition(position)?.data
//    get note data by node
    open fun getNoteDataByNode(position: Int): Any? = graph?.getNodeAtPosition(position)?.note

    /**
     * Submits a new graph to be displayed.
     *
     *
     * If a graph is already being displayed, you need to dispatch Adapter.notifyItem.
     *
     * @param graph The new graph to be displayed.
     */
    open fun submitGraph(@Nullable graph: Graph?) {
        this.graph = graph
    }
}