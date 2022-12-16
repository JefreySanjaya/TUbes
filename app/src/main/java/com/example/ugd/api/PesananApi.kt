package com.example.ugd.api

class PesananApi {
    companion object{
        val BASE_URL = "http://10.53.13.83/tubes_api/public/api/"

        val register = BASE_URL + "register"
        val login = BASE_URL + "login"
        val getUserID = BASE_URL + "user/"
        val updateUser = BASE_URL + "user/"

        val GET_ALL_URL = BASE_URL + "pesanan/"
        val GET_BY_ID_URL = BASE_URL + "pesanan/"
        val ADD_URL = BASE_URL + "pesanan"
        val UPDATE_URL = BASE_URL + "pesanan/"
        val DELETE_URL = BASE_URL + "pesanan/"
    }
}
