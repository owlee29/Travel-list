package com.example.travel_list.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.travel_list.R
import com.example.travel_list.models.TravelListModel
import kotlinx.android.synthetic.main.activity_travel_place_detail.*

class TravelPlaceDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_travel_place_detail)

        var travelListDetailModel: TravelListModel? = null

        if(intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS)) {
            travelListDetailModel = intent.getParcelableExtra(MainActivity.EXTRA_PLACE_DETAILS)
            as TravelListModel
        }

        if(travelListDetailModel != null) {
            setSupportActionBar(toolbar_travel_place_detail)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = travelListDetailModel.title

            toolbar_travel_place_detail.setNavigationOnClickListener {
                onBackPressed()
            }

            iv_place_image.setImageURI(Uri.parse(travelListDetailModel.image))
            tv_description.text = travelListDetailModel.description
            tv_location.text = travelListDetailModel.location
        }

    }
}