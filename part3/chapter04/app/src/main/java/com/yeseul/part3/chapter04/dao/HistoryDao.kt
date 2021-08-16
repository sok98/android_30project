package com.yeseul.part3.chapter04.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.yeseul.part3.chapter04.model.History

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history")
    fun getAll(): List<History>

    @Insert
    fun insertHistory(history: History)

    @Query("DELETE FROM history WHERE keyword == :keyword")
    fun delete(keyword: String)

}