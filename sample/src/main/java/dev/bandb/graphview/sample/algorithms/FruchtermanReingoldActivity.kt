package dev.bandb.graphview.sample.algorithms

import dev.bandb.graphview.decoration.edge.ArrowEdgeDecoration
import dev.bandb.graphview.graph.Edge
import dev.bandb.graphview.graph.Graph
import dev.bandb.graphview.graph.Node
import dev.bandb.graphview.graph.Note
import dev.bandb.graphview.layouts.energy.FruchtermanReingoldLayoutManager
import dev.bandb.graphview.sample.GraphActivity
import dev.bandb.graphview.util.DisplayView
import dev.bandb.graphview.util.Image
import dev.bandb.graphview.util.Label
import java.lang.Boolean.FALSE
import java.lang.Boolean.TRUE

class FruchtermanReingoldActivity : GraphActivity() {
    var imageUrl ="https://img2.doubanio.com/view/personage/raw/public/e60557f89344a06edbd4b0c2c7b83efe.jpg"
    public override fun setLayoutManager() {
        recyclerView.layoutManager = FruchtermanReingoldLayoutManager(this, 1000)
    }
    var index:Int = 0
    public override fun setEdgeDecoration() {
        recyclerView.addItemDecoration(ArrowEdgeDecoration())
    }

    public override fun createGraph(): Graph {
        val graph = Graph()
//        Image(imageUrl)
        var a = Node(Label(nodeText),System.currentTimeMillis() )
        var b = Node(Label(nodeText),System.currentTimeMillis())
        var c = Node(Label(nodeText),System.currentTimeMillis())
        var d = Node(Label(nodeText),System.currentTimeMillis())
        var e = Node(Label(nodeText),System.currentTimeMillis())
        var f = Node(Label(nodeText),System.currentTimeMillis())
        var g = Node(Label(nodeText),System.currentTimeMillis())
        var h = Node(Label(nodeText),System.currentTimeMillis())
        a = addNodeId(a)
        b = addNodeId(b)
        c = addNodeId(c)
        d = addNodeId(d)
        e = addNodeId(e)
        f = addNodeId(f)
        g = addNodeId(g)
        h = addNodeId(h)
        a.note = getNote();
        b.note = getNote();
        c.note = getNote();
        d.note = getNote();
        e.note = getNote();
        f.note = getNote();
        g.note = getNote();
        h.note = getNote();
        var ab = graph.addEdge(a,b)
        var ac = graph.addEdge(a, c)
        var ad = graph.addEdge(a, d)
        var ce = graph.addEdge(c, e)
        var df = graph.addEdge(d, f)
        var fc = graph.addEdge(f, c)
        var gc = graph.addEdge(g, c)
        var hg = graph.addEdge(h, g)
        ab = addEdgeId(ab)
        ab.display = Label(" relation 1")
        ac = addEdgeId(ac)
        ac.display = Label(" relation 2")
        ad = addEdgeId(ad)
        ad.display = Label(" relation 3")
        ce = addEdgeId(ce)
        ce.display = Label(" relation 4")
        df = addEdgeId(df)
        df.display = Label(" relation 5")
        fc = addEdgeId(fc)
        fc.display = Label(" relation 6")
        gc = addEdgeId(gc)
        gc.display = Label(" relation 7")
        hg = addEdgeId(hg)
        hg.display = Label(" relation 8")
        ab.note  = getNote();
        ac.note  = getNote();
        ad.note  = getNote();
        ce.note  = getNote();
        df.note  = getNote();
        fc.note  = getNote();
        gc.note  = getNote();
        hg.note  = getNote();

        return graph
    }

    private fun addEdgeId(edge: Edge): Edge {
        index++
        edge.id = index.toString()
        return edge
    }

    private fun addNodeId(node: Node): Node {
        index++
        node.id = index.toString()
        return node
    }

    private fun randomBoolean():Boolean{
        var ranInt:Int = (0..1).random()
        var result :Boolean

        if(ranInt == 0){
            result = FALSE
        }
        else{
            result = TRUE
        }
        return result
    }
    /**
     * get random node
     */
    private fun getDisplayViewRandom(nodeText: String): DisplayView {

        lateinit var result : DisplayView
        if(randomBoolean()){
            result = Label(nodeText)
        }
        else{
            result = Image(imageUrl)
        }
        return result
    }

    /**
     * get note by node
     */
    private fun getNote(): Note {

        var note:Note = Note(System.currentTimeMillis())

        note.addData(getDisplayViewRandom(nodeText))
        while (randomBoolean()){
            note.addData(getDisplayViewRandom(nodeText))
        }
        return note
    }
}