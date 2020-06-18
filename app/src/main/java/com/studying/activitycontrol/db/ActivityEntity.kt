package com.studying.activitycontrol.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ActivityEntity(
    val num: Int,
    @PrimaryKey(autoGenerate = true)
    var uId: Long? = null
)