package com.example.spoify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.spoify.modelos.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setup()
    }

    private fun setup() {
        bLogin.setOnClickListener{
            if(etEmail.text.isNotEmpty() && etPassword.text.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(etEmail.text.toString(), etPassword.text.toString()).addOnCompleteListener{
                    if(it.isSuccessful){
                        val homeIntent = Intent(this, CancionesFavActivity::class.java).apply {
                            putExtra("email", etEmail.text)
                        }
                        startActivity(homeIntent)
                    }else{
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("Error")
                        builder.setMessage("Se ha producido un error logueando al usuario")
                        builder.setPositiveButton("Aceptar",null)
                        val dialog: AlertDialog = builder.create()
                        dialog.show()
                    }
                }
            }else{
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Error")
                builder.setMessage("Rellena todos los campos")
                builder.setPositiveButton("Aceptar",null)
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        }
        bRegister.setOnClickListener {
            if(etEmail.text.isNotEmpty() && etPassword.text.isNotEmpty()){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(etEmail.text.toString(), etPassword.text.toString()).addOnCompleteListener{
                    if(it.isSuccessful){
                        val database = FirebaseDatabase.getInstance().getReference().child("usuarios").child(FirebaseAuth.getInstance().currentUser?.uid.toString())
                        var user = Usuario(etEmail.text.toString())
                        database.setValue(user)
                        val homeIntent = Intent(this, CancionesFavActivity::class.java).apply {
                            putExtra("email", etEmail.text)
                        }
                        startActivity(homeIntent)
                    }else{
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("Error")
                        builder.setMessage("Se ha producido un error registrando al usuario")
                        builder.setPositiveButton("Aceptar",null)
                        val dialog: AlertDialog = builder.create()
                        dialog.show()
                    }
                }
            }else{
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Error")
                builder.setMessage("Rellena todos los campos")
                builder.setPositiveButton("Aceptar",null)
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        }
    }
}