package com.ekta.kotlin.roomdb.todolist.Activity

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ekta.kotlin.roomdb.todolist.Adapter.PersonAdapter
import com.ekta.kotlin.roomdb.todolist.Adapter.SwipeToDeleteCallback
import com.ekta.kotlin.roomdb.todolist.DBHelper.PersonInfo
import com.ekta.kotlin.roomdb.todolist.R
import com.ekta.kotlin.roomdb.todolist.Util.AddDataDailog
import com.ekta.kotlin.roomdb.todolist.Util.DialogCallback
import com.ekta.kotlin.roomdb.todolist.Util.Utils
import com.ekta.kotlin.roomdb.todolist.ViewModel.PeronViewModel
import com.ekta.kotlin.roomdb.todolist.databinding.ActivityMainBinding
import java.util.Calendar


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var videoModel: PeronViewModel
    lateinit var adapter: PersonAdapter
    private var personList = mutableListOf<PersonInfo>()
    lateinit var dialog: AddDataDailog
    private lateinit var weekdaySpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        videoModel = PeronViewModel(application)
        weekdaySpinner = findViewById(R.id.weekday_spinner)



        val weekdays = arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")

        val weekdayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, weekdays)
        weekdayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        weekdaySpinner.adapter = weekdayAdapter

        binding.weekdaySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedDay = parent.getItemAtPosition(position).toString()
                getAllPersonList(selectedDay)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle case where nothing is selected if necessary
            }
        }

        val currentDayOfWeek = getCurrentDayOfWeek()
        val position = weekdays.indexOf(currentDayOfWeek)
        if (position >= 0) {
            weekdaySpinner.setSelection(position)
        }

        binding.fabAddTodo.setOnClickListener {


            Utils.getUtils(this).startActivity(AddTodoActivity::class.java)

        }

        getAllPersonList()


    }
    private fun getCurrentDayOfWeek(): String {
        val calendar = Calendar.getInstance()
        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> "Sunday"
            Calendar.MONDAY -> "Monday"
            Calendar.TUESDAY -> "Tuesday"
            Calendar.WEDNESDAY -> "Wednesday"
            Calendar.THURSDAY -> "Thursday"
            Calendar.FRIDAY -> "Friday"
            Calendar.SATURDAY -> "Saturday"
            else -> ""
        }
    }

    fun getAllPersonList(day: String? = null) {
        val dataObserver = Observer<List<PersonInfo>> { list ->
            list?.let {
                personList = list.toMutableList()
                adapter.updateList(list)
            }
        }

        if (day == null) {
            videoModel.allPerson.observe(this, dataObserver)
        } else {
            videoModel.getPersonsByDay(day).observe(this, dataObserver)
        }

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        adapter = PersonAdapter(this, object : PersonAdapter.OnclickUpdate {
            override fun updateitem(personInfo: PersonInfo) {
                dialog = AddDataDailog(applicationContext, object : DialogCallback {
                    override fun Adddata() {
                        // Handle add data
                    }

                    override fun updateData(personInfo1: PersonInfo) {
                        videoModel.upadtePerson(personInfo1)
                    }
                }, true, personInfo)
                dialog.show(supportFragmentManager, "")
            }
        })
        binding.recyclerView.adapter = adapter
        enableSwipeToDelete()
    }


    fun getAllPersonList0() {

        videoModel.allPerson.observe(this, Observer { list ->
            list?.let {
                personList = list.toMutableList()
                adapter.updateList(list)
            }
        })

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)



        adapter = PersonAdapter(this, object : PersonAdapter.OnclickUpdate {
            override fun updateitem(personInfo: PersonInfo) {
                dialog = AddDataDailog(applicationContext, object : DialogCallback {
                    override fun Adddata() {

                    }


                    override fun updateData(personInfo1: PersonInfo) {
                        videoModel.upadtePerson(personInfo1)

                    }

                }, true, personInfo)
                dialog.show(supportFragmentManager, "")
            }

        })
        binding.recyclerView.adapter = adapter
        enableSwipeToDelete()
    }


    private fun enableSwipeToDelete() {


       /* val swipeToDeleteCallback: SwipeToDeleteCallback = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
                val position = viewHolder.adapterPosition
                adapter.removeAt(position)
                videoModel.deletePerson(personList[position])
                personinfo = personList[position]
            }
        }
        val itemTouchhelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchhelper.attachToRecyclerView(binding.recyclerView)*/
    }


}