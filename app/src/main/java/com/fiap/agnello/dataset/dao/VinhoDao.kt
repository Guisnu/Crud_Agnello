package com.fiap.agnello.dataset.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.fiap.agnello.model.Vinho

@Dao
interface VinhoDao {
    @Insert
    fun salvar(Vinho: Vinho): Long
    @Update
    fun atualizar(Vinho: Vinho): Int
    @Delete
    fun excluir(Vinho: Vinho): Int
    @Query("SELECT * FROM tbl_Vinho WHERE id = :id")
    fun buscarVinhoPeloId(id: Int): Vinho
    @Query("SELECT * FROM tbl_Vinho ORDER BY nome")
    fun listarVinhos(): List<Vinho>

}