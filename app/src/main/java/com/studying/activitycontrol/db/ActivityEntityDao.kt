package com.studying.activitycontrol.db

import androidx.room.*
import io.reactivex.Flowable

@Dao
interface ActivityEntityDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(action: ActivityEntity)

    @Query("SELECT * FROM ActivityEntity")
    fun selectAll(): Flowable<List<ActivityEntity>>

    @Delete()
    fun delete(activityEntity: ActivityEntity)
}