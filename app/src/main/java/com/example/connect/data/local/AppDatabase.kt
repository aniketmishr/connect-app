package com.example.connect.data.local

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.connect.data.converter.BitmapConverter
import com.example.connect.data.converter.BooleanConverter
import com.example.connect.data.converter.DateConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Database class with a singleton Instance object.
 */
@Database(entities = [Contact::class, Note::class, Tags::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class, BitmapConverter::class,BooleanConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun contactDao(): ContactDao
    abstract fun noteDao(): NoteDao
    abstract fun tagsDao(): TagsDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "item_database")
                    .addCallback(object : RoomDatabase.Callback() {

                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Populate the database with initial data
                            // For example:
                            val coroutineScope = CoroutineScope(Dispatchers.IO)
                            val prepopulateData = listOf<Tags>(
                                Tags(1, "No Tag"),
                                Tags(2, "Friend"),
                                Tags(3, "Colleague"),
                                Tags(4, "Family")
                            )
                            Log.d("MainActivity","Coroutine Started")
                            coroutineScope.launch {
                                val tagDao = Instance?.tagsDao()
                                tagDao?.insert(prepopulateData)
                                Log.d("DB","Coroutine Ended")
                            }
                            Log.d("MainActivity","Build Started")

                        }
                    })
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
