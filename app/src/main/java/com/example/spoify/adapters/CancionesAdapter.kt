package com.example.spoify.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.spoify.R
import com.example.spoify.modelos.Cancion
import com.example.spoify.modelos.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_cancion.view.*

class CancionesAdapter(val listaCanciones: List<Cancion>): RecyclerView.Adapter<CancionesAdapter.CancionesHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CancionesHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CancionesHolder(layoutInflater.inflate(R.layout.item_cancion, parent, false))
    }

    override fun onBindViewHolder(holder: CancionesHolder, position: Int) {
        holder.render(listaCanciones[position])
        holder.view.fbAnadir.setOnClickListener{

            var boolean: Boolean = false
            var listaCancionesFavs : ArrayList<Int> = ArrayList()
            var newUsuario: Usuario = Usuario()
            listaCancionesFavs.clear()
            var databasee : DatabaseReference = Firebase.database.reference.child("usuarios").child(FirebaseAuth.getInstance().currentUser!!.uid).child("listaCanciones")
            val postListenerr = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (!boolean) {
                        for (child: DataSnapshot in dataSnapshot.children) {
                            listaCancionesFavs.add(Integer.parseInt(child.getValue().toString()))
                        }
                        var database: DatabaseReference = Firebase.database.reference.child("usuarios").child(FirebaseAuth.getInstance().currentUser!!.uid)
                        val postListener = object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                if (!boolean) {
                                    for (child: DataSnapshot in dataSnapshot.children) {
                                        if (child.key!!.equals(FirebaseAuth.getInstance().currentUser!!.uid)) {
                                            newUsuario = child.getValue(Usuario::class.java)!!
                                            boolean = true
                                            database.removeValue()
                                        }
                                    }
                                    listaCancionesFavs.add(listaCanciones[position].id!!)
                                    newUsuario.listaCanciones = listaCancionesFavs
                                    database = Firebase.database.reference.child("usuarios").child(FirebaseAuth.getInstance().currentUser!!.uid)
                                    boolean = true
                                    database.setValue(newUsuario)
                                    database = FirebaseDatabase.getInstance().getReference().child("usuarios").child(FirebaseAuth.getInstance().currentUser!!.uid).child("listaCanciones")
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {}
                        }
                        database.addValueEventListener(postListener)
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {}
            }
            databasee.addValueEventListener(postListenerr)
            Toast.makeText(holder.view.context,"Cancion añadida de favoritos", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return listaCanciones.size
    }

    class CancionesHolder(val view: View):RecyclerView.ViewHolder(view) {
        fun render(cancion: Cancion) {
            view.tvArtista.text = cancion.artista
            view.tvTituloCancion.text = cancion.nombre
            Picasso.get().load(cancion.url).into(view.ivCancion)
        }
    }

}