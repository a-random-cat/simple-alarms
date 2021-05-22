package cat.simple.alarms.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cat.simple.alarms.HelperCrypto
import cat.simple.alarms.HelperTime
import cat.simple.alarms.R
import cat.simple.alarms.entities.Alarm
import cat.simple.alarms.entities.Shortcut
import io.realm.OrderedRealmCollection
import io.realm.Realm
import io.realm.RealmRecyclerViewAdapter
import java.util.*
import java.util.concurrent.TimeUnit

class ShortcutFragment : Fragment() {

    private val realm = Realm.getDefaultInstance()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_shortcuts, container, false)

        val recyclerView = root.findViewById<RecyclerView>(R.id.recycler)
        recyclerView.layoutManager =
            LinearLayoutManager(requireActivity().applicationContext)
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(
            DividerItemDecoration(requireActivity().applicationContext,
                DividerItemDecoration.VERTICAL)
        )

        val adapter = ExampleRecyclerViewAdapter(realm.where(Shortcut::class.java).findAll().sort("name"))
        recyclerView.adapter = adapter

        return root
    }

    override fun onDestroy() {
        super.onDestroy()
        realm?.close()
    }

    internal inner class ExampleRecyclerViewAdapter(data: OrderedRealmCollection<Shortcut?>?) :
        RealmRecyclerViewAdapter<Shortcut?,
                ExampleRecyclerViewAdapter.ExampleViewHolder?>(data, true) {



        override fun onCreateViewHolder(parent: ViewGroup,
                                        viewType: Int): ExampleViewHolder {
            val view = LayoutInflater.from(parent.context).
            inflate(R.layout.row_shortcut, parent, false) as View
            return ExampleViewHolder(view)
        }
        override fun onBindViewHolder(holder: ExampleViewHolder, position: Int) {
            getItem(position)?.let {
                holder.data = it
                holder.name.text = it.name
                holder.duration.text = HelperTime.getDurationBreakdown(it.duration)
                activity?.let { activity ->
                    holder.icon.setImageResource(resources.getIdentifier(it.icon, "drawable", activity.packageName))
                }
                holder.itemView.setOnClickListener {
                    AlertDialog.Builder(activity)
                            .setMessage("Set Alarm?")
                            .setCancelable(true)
                            .setPositiveButton("Set Alarm") { _, _ ->
                                holder.data?.let { data ->
                                    val _name = data.name
                                    val _duration = data.duration
                                    realm.executeTransactionAsync { r->
                                        r.createObject(Alarm::class.java, HelperCrypto.randomUUID()).apply {
                                            name = _name
                                            endTime = System.currentTimeMillis() + _duration
                                        }

                                    }
                                }


                            }
                            .setNegativeButton("Cancel") { dialog, _ ->
                                // Dismiss the dialog
                                dialog.dismiss()
                            }
                            .create().show()
                }
            }
        }
        override fun getItemId(index: Int): Long {
            return getItem(index)!!.id.toLong()
        }
        internal inner class ExampleViewHolder(view: View)
            : RecyclerView.ViewHolder(view) {
            var data: Shortcut? = null
            var icon = view.findViewById<ImageView>(R.id.icon)
            var name = view.findViewById<TextView>(R.id.name)
            var duration = view.findViewById<TextView>(R.id.duration)
        }
        init {
            print("Created RealmRecyclerViewAdapter for ${getData()!!.size} items.")
        }
    }

}