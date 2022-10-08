package com.example.kmmfirstdemo

import android.content.Context
import com.example.kmmfirstdemo.sqldelight.AppDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver


actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(AppDatabase.Schema, context, Constant.NOTE_DATABASE)
    }
}
