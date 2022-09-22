package com.example.ugd.entity

class Model (var name : String, var harga : String) {
    companion object{
        @JvmField
        var listOfModel = arrayOf(
            Model("Model Rambut Paquito", "Rp.30.000"),
            Model("Model Rambut Mowhak", "Rp.36.000"),
            Model("Model Rambut bros", "Rp.20.000"),
            Model("Model Rambut Comma Hair", "Rp.25.000"),
            Model("Model Rambut Under Cut", "Rp.30.000"),
            Model("Model Rambut Upper Cut", "Rp.40.000"),
            Model("Model Rambut Quiff", "Rp.50.000"),
            Model("Model Rambut Pompador", "Rp.40.000"),
            Model("Model Rambut Edgar Cut", "Rp.40.000"),
            Model("Model Rambut French Crop", "Rp.30.000"),
        )
    }
}