package com.education.notes.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

private const val TABLE_NAME = "notes_table"

@Parcelize
@Entity(tableName = TABLE_NAME)
data class NotesEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    val title: String,
    val description: String,
    val imageURL: String
):Parcelable
