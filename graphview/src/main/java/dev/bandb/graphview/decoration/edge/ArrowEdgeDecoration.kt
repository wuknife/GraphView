package dev.bandb.graphview.decoration.edge

import android.graphics.*
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import dev.bandb.graphview.AbstractGraphAdapter
import dev.bandb.graphview.util.DisplayView
import dev.bandb.graphview.util.Label

open class ArrowEdgeDecoration constructor(private val linePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    strokeWidth = 5f    // TODO: move default values res xml
    color = Color.GRAY
    style = Paint.Style.FILL
    textSize = 50f
    strokeJoin = Paint.Join.ROUND
    pathEffect = CornerPathEffect(10f)
}) : ArrowDecoration(linePaint) {
    var texPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 3f    // TODO: move default values res xml
        color = Color.RED
        style = Paint.Style.FILL
        textSize = 30f
        strokeJoin = Paint.Join.ROUND
        pathEffect = CornerPathEffect(3f)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.layoutManager == null) {
            return
        }
        val adapter = parent.adapter
        if (adapter !is AbstractGraphAdapter) {
            throw RuntimeException(
                    "GraphLayoutManager only works with ${AbstractGraphAdapter::class.simpleName}")
        }

        val graph = adapter.graph
        val trianglePaint = Paint(linePaint).apply {
            style = Paint.Style.FILL
        }




        graph?.edges?.forEach {
                edge->
//                (source, destination) ->
            val source = edge.source
            val destination = edge.destination
//            display view
            val displayView =  edge.display
            val (x1, y1) = source.position
            val (x2, y2) = destination.position

            val startX = x1 + source.width / 2f
            val startY = y1 + source.height / 2f
            val stopX = x2 + destination.width / 2f
            val stopY = y2 + destination.height / 2f

            val clippedLine = clipLine(startX, startY, stopX, stopY, destination)


            val triangleCentroid: FloatArray
            triangleCentroid = drawTriangle(c, trianglePaint, clippedLine[0], clippedLine[1], clippedLine[2], clippedLine[3])

            val linkPath = getPath(clippedLine)
//                c.drawPath(linkPath, linePaint)

            drawDisplayView(c,displayView,linkPath,texPaint)

            c.drawLine(clippedLine[0],
                    clippedLine[1],
                    triangleCentroid[0],
                    triangleCentroid[1], linePaint)
//            drawDisplayView(c,displayView,linkPath)

            Log.d("ArrowEdge","------------------- c.drawLine :---------------------")
            Log.d("ArrowEdge","clippedLine[0]:"+clippedLine[0])
            Log.d("ArrowEdge","clippedLine[1]:"+clippedLine[1])
//            Log.d("ArrowEdge","triangleCentroid[0]:"+triangleCentroid[0])
//            Log.d("ArrowEdge","triangleCentroid[1]:"+triangleCentroid[1])
        }

//        c.drawText("-------------Test----------------",100f,100f,linePaint)
    }



}
