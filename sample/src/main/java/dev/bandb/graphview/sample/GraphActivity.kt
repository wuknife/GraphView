package dev.bandb.graphview.sample

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.memory.MemoryCache
import coil.request.ImageRequest
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dev.bandb.graphview.AbstractGraphAdapter
import dev.bandb.graphview.graph.Graph
import dev.bandb.graphview.graph.Node
import dev.bandb.graphview.graph.Note
import dev.bandb.graphview.util.DisplayView
import dev.bandb.graphview.util.Image
import dev.bandb.graphview.util.Label
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

abstract class GraphActivity : AppCompatActivity() {
    protected lateinit var recyclerView: RecyclerView
    protected lateinit var adapter: AbstractGraphAdapter<NodeViewHolder>
    private lateinit var fab: FloatingActionButton
    private var currentNode: Node? = null
    private var nodeCount = 1

    protected abstract fun createGraph(): Graph
    protected abstract fun setLayoutManager()
    protected abstract fun setEdgeDecoration()

    //    note view
    private lateinit var noteView: LinearLayout
//    note view display animation duration
    private var shortAnimationDuration: Int = 0

    private lateinit var context:Context

    private lateinit var memoryCache: MemoryCache
    private lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = getApplicationContext()
        setContentView(R.layout.activity_graph)
//        inite image
        initImage()
        val graph = createGraph()
        recyclerView = findViewById(R.id.recycler)
        setLayoutManager()
        setEdgeDecoration()
        setupGraphView(graph)

//        setupFab(graph)
        setupToolbar()

//        setup note view
        setupNote()
    }

    /**
     * init image
     */
    private fun initImage() {
        memoryCache = MemoryCache.Builder(context)
            .maxSizeBytes(Int.MAX_VALUE)
            .build()
        imageLoader = ImageLoader.Builder(context)
            .memoryCache(memoryCache)
            .diskCache(null)
            .build()
    }

    /**
     * hide Note
     */
    private fun hideNote(){
        if(noteView.visibility != View.GONE) {
            noteView.visibility = View.GONE
        }
    }

    /**
     * display note
     */
    private suspend fun displayNote(note : Note){
//        dispaly content
        displayNoteContent(note)
//        animation
        if(noteView.visibility == View.GONE) {

            this.runOnUiThread(java.lang.Runnable {
                noteView.apply {
                    // Set the content view to 0% opacity but visible, so that it is visible
                    // (but fully transparent) during the animation.
                    alpha = 0f
                    visibility = View.VISIBLE
                    // Animate the content view to 100% opacity, and clear any animation
                    // listener set on the view.
                    animate()
                        .alpha(1f)
                        .setDuration(shortAnimationDuration.toLong())
                        .setListener(null)
                }
            })
        }
    }

    private suspend fun displayNoteContent(note : Note){
//        List<DisplayView>() viewList = note.data
        var contentList:List<DisplayView> = note.data
//        delete noteView

        this.runOnUiThread(java.lang.Runnable {
            noteView.removeAllViews()
        })
//        add views
        for (contentView : DisplayView in contentList){
            val noteItemView = LayoutInflater.from(noteView.context)
                .inflate(R.layout.note, noteView, false)

            var tempTextView:TextView  = noteItemView.findViewById(R.id.noteTextView)
            var tempImageView:ImageView  = noteItemView.findViewById(R.id.noteImageView)

            this.runOnUiThread(java.lang.Runnable {
                if (contentView is Label) {
                    tempTextView.setText(contentView.text)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        tempTextView.setAutoSizeTextTypeWithDefaults(AUTO_SIZE_TEXT_TYPE_UNIFORM)
                    }
                    tempTextView.textSize = contentView.size.toFloat()
                    tempImageView.visibility = View.GONE
                } else if (contentView is Image) {

                    val request = ImageRequest.Builder(context)
                        .data(contentView.url)
                        .target(tempImageView)
                        .build()
                    val result = imageLoader.enqueue(request)
                    tempImageView.maxHeight = contentView.height
                    tempImageView.maxWidth = contentView.width
                    tempTextView.visibility = View.GONE
                }

                noteView.addView(noteItemView)

        })
        }
    }
    /**
     * initNote view
     */
    private fun setupNote() {
        noteView = findViewById(R.id.noteView)
        noteView.visibility = View.GONE

    }

    private fun setupGraphView(graph: Graph) {
        adapter = object : AbstractGraphAdapter<NodeViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NodeViewHolder {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.node, parent, false)
                return NodeViewHolder(view)
            }

            override fun onBindViewHolder(holder: NodeViewHolder, position: Int) {
                var dv = getNode(position)!!.data

                this@GraphActivity.runOnUiThread(java.lang.Runnable {
                    if (dv is Label) {
                        holder.textView.text = dv.text
                    } else if (dv is Image) {
                        if (dv.url != null) {
                            GlobalScope.launch {
                                val request = ImageRequest.Builder(context)
                                    .data(dv.url)
                                    .target(holder.imageView)
                                    .build()
                                val result = imageLoader.execute(request)
                            }
                        }
                    }
                })


            }
        }.apply {
            this.submitGraph(graph)
             fun setupGraphView(graph: Graph) {
                adapter = object : AbstractGraphAdapter<NodeViewHolder>() {
                    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NodeViewHolder {
                        val view = LayoutInflater.from(parent.context)
                            .inflate(R.layout.node, parent, false)
                        return NodeViewHolder(view)
                    }

                    override fun onBindViewHolder(holder: NodeViewHolder, position: Int) {
                        var dv = getNode(position)!!.data

                        this@GraphActivity.runOnUiThread(java.lang.Runnable {
                            if (dv is Label) {
                                holder.textView.text = dv.text
                            } else if (dv is Image) {

                                GlobalScope.launch {
                                    val request = ImageRequest.Builder(context)
                                        .data(dv.url)
                                        .target(holder.imageView)
                                        .build()
                                    val result = imageLoader.execute(request)
                                }
                            }
                        })
                    }
                }.apply {
                    this.submitGraph(graph)
                    recyclerView.adapter = this
                }
            }

            recyclerView.adapter = this
        }
    }

    private fun setupFab(graph: Graph) {
//        fab = findViewById(R.id.addNode)
//        fab.setOnClickListener {
//            val newNode = Node(Label(this.nodeText ))
//            if (currentNode != null) {
//                graph.addEdge(currentNode!!, newNode)
//            } else {
//                graph.addNode(newNode)
//            }
//            adapter.notifyDataSetChanged()
//        }
//        fab.setOnLongClickListener {
//            if (currentNode != null) {
//                graph.removeNode(currentNode!!)
//                currentNode = null
//                adapter.notifyDataSetChanged()
//                fab.hide()
//            }
//            true
//        }
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val ab = supportActionBar
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            ab.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    protected inner class NodeViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.textView)
        var imageView: ImageView = itemView.findViewById(R.id.imageView)
        init {
            itemView.setOnClickListener {
//                if (!fab.isShown) {
//                    fab.show()
//                }

                currentNode = adapter.getNode(bindingAdapterPosition)

                GlobalScope.launch{

                    displayNote(currentNode!!.note)
                }

//                Snackbar.make(itemView, "Clicked on " + currentNode!!.data?.toString(),
//                        Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    protected val nodeText: String
        get() = "Node " + nodeCount++
}
