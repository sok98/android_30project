package com.yeseul.part2.chapter04

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yeseul.part2.chapter04.dao.HistoryDao
import com.yeseul.part2.chapter04.model.History

@Database(entities = [History::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}