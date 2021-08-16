package com.yeseul.part3.chapter04.model

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yeseul.part3.chapter04.dao.HistoryDao
import com.yeseul.part3.chapter04.dao.ReviewDao

@Database(entities = [History::class, Review::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun historyDao(): HistoryDao
    abstract fun reviewDao(): ReviewDao

}