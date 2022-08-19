package com.education.notes.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

private const val TABLE_NAME = "tasks_table"

@Parcelize
@Entity(tableName = TABLE_NAME)
data class TasksModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val text: String
) : Parcelable

