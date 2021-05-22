package cat.simple.alarms.ui

import android.app.AlertDialog
import android.app.AlertDialog.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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

class AlarmsFragment : Fragment() {
    private val realm = Realm.getDefaultInstance()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_alarms, container, false)

        val recyclerView = root.findViewById<RecyclerView>(R.id.recycler)
        recyclerView.layoutManager =
            LinearLayoutManager(requireActivity().applicationContext)
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(
            DividerItemDecoration(requireActivity().applicationContext,
                DividerItemDecoration.VERTICAL)
        )

        val adapter = ExampleRecyclerViewAdapter(realm.where(Alarm::class.java).findAll().sort("endTime"))
        recyclerView.adapter = adapter

        return root
    }

    override fun onDestroy() {
        super.onDestroy()
        realm?.close()
    }
    internal inner class ExampleRecyclerViewAdapter(data: OrderedRealmCollection<Alarm?>?) :
        RealmRecyclerViewAdapter<Alarm?,
                ExampleRecyclerViewAdapter.ExampleViewHolder?>(data, true) {



        override fun onCreateViewHolder(parent: ViewGroup,
                                        viewType: Int): ExampleViewHolder {
            val view = LayoutInflater.from(parent.context).
            inflate(R.layout.row_alarm, parent, false) as View
            return ExampleViewHolder(view)
        }
        override fun onBindViewHolder(holder: ExampleViewHolder, position: Int) {
            getItem(position)?.let {
                holder.data = it
                holder.name.text = it.name
                holder.endTime.text = HelperTime.getTimeString(it.endTime)
                val diff = HelperTime.getDurationBreakdown(Math.max(0, it.endTime - System.currentTimeMillis()))
                holder.countdown.text = diff

                holder.itemView.setOnClickListener {
                    Builder(activity)
                            .setMessage("Delete?")
                            .setCancelable(true)
                            .setPositiveButton("Delete") { _, _ ->
                                holder.data?.let { data ->
                                    val _id = data.id
                                    realm.executeTransactionAsync { r->
                                        r.where(Alarm::class.java).equalTo("id", _id).findAll().deleteAllFromRealm()
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
            var data: Alarm? = null
            var name = view.findViewById<TextView>(R.id.name)
            var endTime = view.findViewById<TextView>(R.id.end_time)
            var countdown = view.findViewById<TextView>(R.id.countdown)
        }
        init {
            print("Created RealmRecyclerViewAdapter for ${getData()!!.size} items.")
        }
    }

}