package com.example.spoify.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.spoify.R
import com.example.spoify.modelos.Cancion
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_cancion.view.*
import kotlinx.android.synthetic.main.item_cancion_fav.view.*

class CancionesFavAdapter(val listaCanciones: List<Cancion>): RecyclerView.Adapter<CancionesFavAdapter.CancionesFavHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CancionesFavHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CancionesFavHolder(layoutInflater.inflate(R.layout.item_cancion_fav, parent, false))
    }

    override fun onBindViewHolder(favHolder: CancionesFavHolder, position: Int) {
        favHolder.render(listaCanciones[position])
        favHolder.view.fbQuitarFav.setOnClickListener{
            var idCancion : String = ""
            var borrado : Boolean = false
            var newListaCanciones : ArrayList<String> = ArrayList()
            var database : DatabaseReference
            database = FirebaseDatabase.getInstance().getReference().child("canciones")
            database.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (!borrado) {
                        for (child: DataSnapshot in dataSnapshot.children) {
                            if (child.child("nombre").getValue().toString()
                                    .equals(favHolder.view.tvFavTituloCancion.text.toString())
                            ) {
                                idCancion = child.child("id").getValue().toString()
                            }
                        }
                        newListaCanciones.clear()
                        database = Firebase.database.reference.child("usuarios").child(
                            FirebaseAuth.getInstance().currentUser!!.uid
                        ).child("listaCanciones")
                        val postListener = object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                if (!borrado) {
                                    newListaCanciones.clear()
                                    for (child: DataSnapshot in dataSnapshot.children) {
                                        newListaCanciones.add(child.getValue().toString())
                                    }
                                    var listaCancioness: ArrayList<String> = ArrayList()
                                    for (i in 0 until newListaCanciones.size) {
                                        if (newListaCanciones.get(i).equals(idCancion)) {

                                        } else {
                                            listaCancioness.add(newListaCanciones.get(i))
                                        }
                                    }
                                    database =
                                        FirebaseDatabase.getInstance().getReference()
                                            .child("usuarios")
                                            .child(FirebaseAuth.getInstance().currentUser!!.uid)
                                            .child("listaCanciones")
                                    borrado = true
                                    database.setValue(listaCancioness)
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {}
                        }
                        database.addValueEventListener(postListener)
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }
        Toast.makeText(favHolder.view.context,"Cancion elminada de favoritos",Toast.LENGTH_SHORT).show()
    }

    override fun getItemCount(): Int {
        return listaCanciones.size
    }

    class CancionesFavHolder(val view: View):RecyclerView.ViewHolder(view) {
        fun render(cancion: Cancion) {
            view.tvFavArtista.text = cancion.artista
            view.tvFavTituloCancion.text = cancion.nombre
            Picasso.get().load(Uri.parse(cancion.url)).into(view.ivFavCancion)
        }
    }
}