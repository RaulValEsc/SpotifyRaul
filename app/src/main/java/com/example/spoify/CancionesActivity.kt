package com.example.spoify

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spoify.adapters.CancionesAdapter
import com.example.spoify.adapters.CancionesFavAdapter
import com.example.spoify.modelos.Cancion
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_canciones.*

class CancionesActivity : AppCompatActivity() {

    var listaCanciones : ArrayList<Cancion> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canciones)


        cargarCanciones()
    }

    private fun cargarCanciones() {
        var database = FirebaseDatabase.getInstance().getReference()
        database.child("canciones").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listaCanciones.clear()
                rvCanciones.setLayoutManager(LinearLayoutManager(applicationContext))
                rvCanciones.addItemDecoration(DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL))
                rvCanciones.setAdapter(CancionesAdapter(listaCanciones))
                for (child: DataSnapshot in dataSnapshot.children) {
                    listaCanciones.add(child.getValue(Cancion::class.java)!!)
                    rvCanciones.setLayoutManager(LinearLayoutManager(applicationContext))
                    rvCanciones.addItemDecoration(DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL))
                    rvCanciones.setAdapter(CancionesAdapter(listaCanciones))
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}