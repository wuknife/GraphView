package dev.bandb.graphview.decoration.edge

import android.graphics.*
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import dev.bandb.graphview.AbstractGraphAdapter
import dev.bandb.graphview.graph.Node
import dev.bandb.graphview.util.DisplayView
import dev.bandb.graphview.util.Label
import kotlin.Boolean as Boolean1


open class ArrowDecoration constructor(private val linePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    strokeWidth = 5f
    color = Color.BLACK
    style = Paint.Style.STROKE
    strokeJoin = Paint.Join.ROUND
    pathEffect = CornerPathEffect(10f)
    textSize = 50f
}) : RecyclerView.ItemDecoration() {
    val textHoffset = 0f
    val textVoffset = 10f

    private val PRECISION = 0.002f
    private val trianglePath = Path()
//    linkPath
    private val linkPath = Path()

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
        val trianglePaint = Paint(this.linePaint).apply {
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
            drawTriangle(c, trianglePaint, clippedLine[0], clippedLine[1], clippedLine[2], clippedLine[3])
        }
//        c.drawPath(trianglePath,linePaint)
    }

    /**
     * draw display view
     */
    @RequiresApi(Build.VERSION_CODES.O)
    protected fun drawDisplayView(c: Canvas, displayView: DisplayView?, linkPath: Path, paint : Paint) {
        if(displayView != null && displayView is Label){
            val pathMeasure = PathMeasure(linkPath,false)
            val pathLength = pathMeasure.length

//            val linkPointPos = linkPath.approximate

            val centerOffset = getApproxXToCenterText(displayView.text!!,paint.typeface,paint.textSize,pathLength)
//            c.rotate(180f)
//            pathMeasure.getPosTan(pathMeasure.length / 2,pos,tan)
//            val matrix = Matrix()
//            matrix.reset()
//            matrix.postRotate(-90f,pos[0],pos[1])
//            linkPath.transform(matrix)
            if(needRotate(linkPath)){
                rotatePath(linkPath)
            }


            c.drawTextOnPath(displayView.text!!,linkPath, centerOffset, 50f,paint)
//            draw path to find reason for text position

//            c.drawPath(linkPath,paint)
        }
    }

    /**
     * rotate path 180`
     */
    private fun rotatePath(linkPath: Path) {
        val mMatrix = Matrix()
        val bounds = RectF()
        linkPath.computeBounds(bounds, true)
        mMatrix.postRotate(180f, bounds.centerX(), bounds.centerY())
        linkPath.transform(mMatrix);

    }

    /**
     * judge wheathear rotate path
     * 1.from right to left
     *
     */
    private fun needRotate(linkPath: Path): kotlin.Boolean {
        var result = false
        val pathMeasure = PathMeasure(linkPath,false)

        val pos1 = FloatArray(2) // 起点的时间坐标
        val tan1 = FloatArray(2) //   起点tangent值,用于计算角度
        val pos2 = FloatArray(2) // 终点的时间坐标
        val tan2 = FloatArray(2) //   终点tangent值,用于计算角度
//        caculate start position
        pathMeasure.getPosTan(0f,pos1,tan1)
//        caculate end position
        pathMeasure.getPosTan(pathMeasure.length/2,pos2,tan2)

        if(pos1[0] >= pos2[0]){
            result = true
        }
        return result
    }

    /**
     * get text offset by center
     */
    protected fun getApproxXToCenterText(
        text: String?,
        typeface: Typeface?,
        fontSize: Float,
        widthToFitStringInto: Float
    ): Float {
        val p = Paint()
        p.typeface = typeface
        p.textSize = fontSize.toFloat()
        val textWidth = p.measureText(text)
        val result = ((widthToFitStringInto - textWidth) / 2f)
//        - (fontSize / 2f)
//
//        Log.d("ApproxXToCenterText","text:["+text+"],widthToFitStringInto:["+widthToFitStringInto+"],textWidth:["+textWidth+"]"+
//                ",fontSize:["+fontSize+"],result:["+fontSize+"]")
        return result
    }
    /**
     * return path
     */
    protected fun getPath(clippedLine: FloatArray): Path {
        val linkPath = Path()
        linkPath.moveTo(clippedLine[0],clippedLine[1])
        linkPath.lineTo(clippedLine[2],clippedLine[3])

        return linkPath
    }
    protected fun clipLine(
            startX: Float,
            startY: Float,
            stopX: Float,
            stopY: Float,
            destination: Node
    ): FloatArray {
        val resultLine = FloatArray(4)
        resultLine[0] = startX
        resultLine[1] = startY

        val slope = (startY - stopY) / (startX - stopX)
        val halfHeight = destination.height / 2f
        val halfWidth = destination.width / 2f
        val halfSlopeWidth = slope * halfWidth
        val halfSlopeHeight = halfHeight / slope

        if (-halfHeight <= halfSlopeWidth && halfSlopeWidth <= halfHeight) {
            // line intersects with ...
            if (destination.x > startX) {
                // left edge
                resultLine[2] = stopX - halfWidth
                resultLine[3] = stopY - halfSlopeWidth
            } else if (destination.x < startX) {
                // right edge
                resultLine[2] = stopX + halfWidth
                resultLine[3] = stopY + halfSlopeWidth
            }
        }

        if (-halfWidth <= halfSlopeHeight && halfSlopeHeight <= halfWidth) {
            // line intersects with ...
            if (destination.y < startY) {
                // bottom edge
                resultLine[2] = stopX + halfSlopeHeight
                resultLine[3] = stopY + halfHeight
            } else if (destination.y > startY) {
                // top edge
                resultLine[2] = stopX - halfSlopeHeight
                resultLine[3] = stopY - halfHeight
            }
        }

        return resultLine
    }

    /**
     * Draws a triangle.
     *
     * @param canvas
     * @param paint
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    protected fun drawTriangle(canvas: Canvas, paint: Paint?, x1: Float, y1: Float, x2: Float, y2: Float): FloatArray {
        val angle = (Math.atan2(y2 - y1.toDouble(), x2 - x1.toDouble()) + Math.PI).toFloat()
        val x3 = (x2 + ARROW_LENGTH * Math.cos((angle - ARROW_DEGREES).toDouble())).toFloat()
        val y3 = (y2 + ARROW_LENGTH * Math.sin((angle - ARROW_DEGREES).toDouble())).toFloat()
        val x4 = (x2 + ARROW_LENGTH * Math.cos((angle + ARROW_DEGREES).toDouble())).toFloat()
        val y4 = (y2 + ARROW_LENGTH * Math.sin((angle + ARROW_DEGREES).toDouble())).toFloat()
        trianglePath.reset()
        trianglePath.moveTo(x2, y2) // Top
        trianglePath.lineTo(x3, y3) // Bottom left
        trianglePath.lineTo(x4, y4) // Bottom right
        trianglePath.close()
        canvas.drawPath(trianglePath, paint!!)

        // calculate centroid of the triangle
        val x = (x2 + x3 + x4) / 3
        val y = (y2 + y3 + y4) / 3
        val triangleCentroid = floatArrayOf(x, y)
        return triangleCentroid
    }

    companion object {
        //TODO: expose
        private const val ARROW_DEGREES = 0.5f
        private const val ARROW_LENGTH = 50f
    }
}
