package com.example.kmmfirstdemo

import com.example.kmmfirstdemo.sqldelight.AppDatabase
import kotlinx.coroutines.CoroutineScope
import note.Note
import persondb.Person

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}