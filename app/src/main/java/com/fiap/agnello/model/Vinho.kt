package com.fiap.agnello.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_Vinho")
data class Vinho(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nome: String  = "",
    val tipo: String  = "",
    val preco: Double = 0.00
)