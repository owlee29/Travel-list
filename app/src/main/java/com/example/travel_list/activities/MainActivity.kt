package com.example.travel_list.activities

import android.app.Activity
import android.content.Intent
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.travel_list.R
import com.example.travel_list.adapters.TravelPlacesAdapter
import com.example.travel_list.database.DatabaseHandler
import com.example.travel_list.models.TravelListModel
import kotlinx.android.synthetic.main.activity_main.*
import pl.kitek.rvswipetodelete.SwipeToDeleteCallback
import pl.kitek.rvswipetodelete.SwipeToEditCallback

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fabAddHappyPlace.setOnClickListener{
            val intent = Intent(this, AddTravelListActivity::class.java)
            startActivityForResult(intent, ADD_PLACE_ACTIVITY_REQUEST_CODE)
        }
        getTravelPlacesListFromLocalDB()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == ADD_PLACE_ACTIVITY_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK) {
                getTravelPlacesListFromLocalDB()
            } else {
                Log.e("Activity", "Cancelled or Back pressed")
            }
        }
    }

    private fun setupTravelPlacesRecyclerView(travelPlaceList: ArrayList<TravelListModel>) {
        rv_travel_places_lists.layoutManager = LinearLayoutManager(this)
        rv_travel_places_lists.setHasFixedSize(true)

        val placesAdapter = TravelPlacesAdapter(this, travelPlaceList)
        rv_travel_places_lists.adapter = placesAdapter

        placesAdapter.setOnClickListener(object: TravelPlacesAdapter.OnClickListener{
            override fun onClick(position: Int, model: TravelListModel) {
                val intent = Intent(
                    this@MainActivity, TravelPlaceDetailActivity::class.java)
                intent.putExtra(EXTRA_PLACE_DETAILS, model)
                startActivity(intent)
            }
        })

        val editSwipeHandler = object: SwipeToEditCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rv_travel_places_lists.adapter as TravelPlacesAdapter
                adapter.notifyEditItem(
                    this@MainActivity,
                    viewHolder.adapterPosition,
                    ADD_PLACE_ACTIVITY_REQUEST_CODE
                )
            }
        }
        val  editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
        editItemTouchHelper.attachToRecyclerView(rv_travel_places_lists)

        val deleteSwipeHandler = object: SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val adapter = rv_travel_places_lists.adapter as TravelPlacesAdapter
                adapter.removeAt(viewHolder.adapterPosition)

                getTravelPlacesListFromLocalDB()
            }
        }
        val  deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
        deleteItemTouchHelper.attachToRecyclerView(rv_travel_places_lists)
    }

    private fun getTravelPlacesListFromLocalDB() {
        val dbHandler = DatabaseHandler(this)
        val getTravelList: ArrayList<TravelListModel> = dbHandler.getTravelPlacesList()

        if(getTravelList.size > 0) {
            rv_travel_places_lists.visibility = View.VISIBLE
            tv_no_records_available.visibility = View.GONE
            setupTravelPlacesRecyclerView(getTravelList)
        } else {
            rv_travel_places_lists.visibility = View.GONE
            tv_no_records_available.visibility = View.VISIBLE
        }
    }

    companion object {
        var ADD_PLACE_ACTIVITY_REQUEST_CODE = 1
        var EXTRA_PLACE_DETAILS = "extra_place_details"
    }
}