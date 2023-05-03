package com.example.travel_list.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.travel_list.adapters.TravelPlacesAdapter
import com.example.travel_list.models.TravelListModel

class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

        companion object {
            private const val DATABASE_VERSION = 1
            private const val DATABASE_NAME = "TravelListDatabase"
            private const val TABLE_TRAVEL_LIST = "TRAVELLISTTABLE"

            private const val KEY_ID = "_id"
            private const val KEY_TITLE = "title"
            private const val KEY_IMAGE = "image"
            private const val KEY_DESCRIPTION = "description"
            private const val KEY_DATE = "date"
            private const val KEY_LOCATION = "location"
            private const val KEY_LATITUDE = "latitude"
            private const val KEY_LONGITUDE = "longitude"
        }
        override fun onCreate(db: SQLiteDatabase?) {
            val CREATE_TRAVEL_LIST_TABLE = ("CREATE TABLE " + TABLE_TRAVEL_LIST +
                    "("
                    + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_TITLE + " TEXT,"
                    + KEY_IMAGE + " TEXT,"
                    + KEY_DESCRIPTION + " TEXT,"
                    + KEY_DATE + " TEXT,"
                    + KEY_LOCATION + " TEXT,"
                    + KEY_LATITUDE + " TEXT,"
                    + KEY_LONGITUDE + " TEXT)")
            db?.execSQL(CREATE_TRAVEL_LIST_TABLE)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("DROP TABLE IF EXISTS $TABLE_TRAVEL_LIST")
            onCreate(db)
        }

        fun addTravelPlace(travelList: TravelListModel): Long {
            val db = this.writableDatabase

            val contentValues = ContentValues()
            contentValues.put(KEY_TITLE, travelList.title)
            contentValues.put(KEY_IMAGE, travelList.image)
            contentValues.put(
                KEY_DESCRIPTION,
                travelList.description
            )
            contentValues.put(KEY_DATE, travelList.date)
            contentValues.put(KEY_LOCATION, travelList.location)
            contentValues.put(KEY_LATITUDE, travelList.latitude)
            contentValues.put(KEY_LONGITUDE, travelList.longitude)

            val result = db.insert(TABLE_TRAVEL_LIST, null, contentValues)

            db.close()
            return result
        }

    fun updateTravelPlace(travelList: TravelListModel): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, travelList.title)
        contentValues.put(KEY_IMAGE, travelList.image)
        contentValues.put(
            KEY_DESCRIPTION,
            travelList.description
        )
        contentValues.put(KEY_DATE, travelList.date)
        contentValues.put(KEY_LOCATION, travelList.location)
        contentValues.put(KEY_LATITUDE, travelList.latitude)
        contentValues.put(KEY_LONGITUDE, travelList.longitude)

        val success = db.update(
            TABLE_TRAVEL_LIST,
            contentValues,
            KEY_ID + "=" +travelList.id, null)

        db.close()
        return success
    }

    fun deleteTravelPlace(travelPlace: TravelListModel): Int {
        val db = this.writableDatabase
        val success = db.delete(
            TABLE_TRAVEL_LIST,
            KEY_ID + "=" + travelPlace.id,
            null)
        db.close()
        return success
    }

    fun getTravelPlacesList(): ArrayList<TravelListModel> {

        val travelPlaceList = ArrayList<TravelListModel>()
        val selectQuery = "SELECT * FROM $TABLE_TRAVEL_LIST"
        val db = this.readableDatabase

        try {
            val cursor : Cursor = db.rawQuery(selectQuery, null)

            if(cursor.moveToFirst()) {
                do {
                    val place = TravelListModel(
                        cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_TITLE)),
                        cursor.getString(cursor.getColumnIndex(KEY_IMAGE)),
                        cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndex(KEY_DATE)),
                        cursor.getString(cursor.getColumnIndex(KEY_LOCATION)),
                        cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE)),
                        cursor.getDouble(cursor.getColumnIndex(KEY_LONGITUDE))
                    )
                    travelPlaceList.add(place)
                } while(cursor.moveToNext())
            }
            cursor.close()

        } catch(e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        return travelPlaceList
    }
}