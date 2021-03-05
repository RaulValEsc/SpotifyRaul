package com.example.spoify.modelos

class Cancion{
    var artista: String? = null
    var id: Int? = null
    var nombre: String? = null
    var url: String? = null

    constructor(){}

    constructor(artista: String,id: Int, nombre: String, url: String){
        this.id = id
        this.nombre = nombre
        this.artista = artista
        this.url = url
    }
}