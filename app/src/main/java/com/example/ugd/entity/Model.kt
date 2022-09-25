package com.example.ugd.entity

import com.example.ugd.R

class Model (var name : String, var harga : String, var gambar: Int ) {
    companion object{
        @JvmField
        var listOfModel = arrayOf(
            Model("Model Rambut Paquito", "Rp.30.000", R.drawable.paquito),
            Model("Model Rambut Mowhak", "Rp.36.000", R.drawable.mohawk),
            Model("Model Rambut Bros", "Rp.20.000", R.drawable.bros),
            Model("Model Rambut Comma Hair", "Rp.25.000", R.drawable.comahair),
            Model("Model Rambut Under Cut", "Rp.30.000", R.drawable.undercut),
            Model("Model Rambut Layer Hair Cut", "Rp.60.000", R.drawable.layerhaircut),
            Model("Model Rambut Bob Cut", "Rp.50.000", R.drawable.bobcut),
            Model("Model Rambut Wavy Short Hair", "Rp.50.000", R.drawable.wavyshorthair),
            Model("Model Rambut Medium Short Hair", "Rp.50.000", R.drawable.mediumshorthair),
            Model("Model Rambut Soft Layer", "Rp.50.000", R.drawable.softlayer),
        )
    }
}