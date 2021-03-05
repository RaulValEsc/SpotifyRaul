package com.example.spoify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spoify.adapters.CancionesAdapter
import com.example.spoify.adapters.CancionesFavAdapter
import com.example.spoify.modelos.Cancion
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_canciones.*
import kotlinx.android.synthetic.main.activity_canciones_fav.*

class CancionesFavActivity : AppCompatActivity() {

    var database: DatabaseReference = FirebaseDatabase.getInstance().getReference()
    var listaCanciones : ArrayList<Cancion> = ArrayList()
    var listaCancionesFavs : ArrayList<String> = ArrayList()
    var booleano : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canciones_fav)
        cargarLista()

        setup()
    }

    private fun cargarLista() {
        listaCanciones.clear()
        database = Firebase.database.reference.child("usuarios").child(
            FirebaseAuth.getInstance().currentUser!!.uid).child("listaCanciones")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listaCancionesFavs.clear()
                for (child : DataSnapshot in dataSnapshot.children){
                    listaCancionesFavs.add(child.getValue().toString())
                }
                cargarFavoritas()
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        }
        database.addValueEventListener(postListener)
    }

    private fun setup() {
        fbCanciones.setOnClickListener{
            val CancionIntent = Intent(this, CancionesActivity::class.java)
            startActivity(CancionIntent)
        }
    }

    private fun cargarFavoritas() {

        database = Firebase.database.reference.child("canciones")
        val postListenerC = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listaCanciones.clear()
                for (child : DataSnapshot in dataSnapshot.children){
                    for (i : String in listaCancionesFavs) {
                        if (child.child("id").getValue().toString().equals(i)){
                            var newCancion:Cancion = child.getValue(Cancion::class.java)!!
                            listaCanciones.add(newCancion)
                        }
                    }
                    rvCancionesFav.setLayoutManager(LinearLayoutManager(applicationContext))
                    rvCancionesFav.addItemDecoration(DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL))
                    rvCancionesFav.setAdapter(CancionesFavAdapter(listaCanciones))
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        }
        database.addValueEventListener(postListenerC)

    }
}