package com.fiap.agnello.dataset.repository

import android.content.Context
import com.fiap.agnello.dataset.dao.VinhoDb
import com.fiap.agnello.model.Vinho

class VinhoRepository(context: Context) {

    var db = VinhoDb.getDataBase(context).VinhoDao()

    fun salvar(vinho: Vinho) : Long {
        return db.salvar(vinho)
    }

    fun atualizar(vinho: Vinho) : Int {
        return db.atualizar(vinho)
    }

    fun excluir(vinho: Vinho) : Int {
        return db.excluir(vinho)
    }

    fun buscarVinhoPeloId(id: Int) : Vinho {
        return db.buscarVinhoPeloId(id)
    }

    fun listarVinhos() : List<Vinho>{
        return db.listarVinhos()
    }

}