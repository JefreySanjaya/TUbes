package com.example.ugd

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager

class FragmentHome : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // Proses menghubungkan layout fragment_home.xml dengan fragment ini
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        val adapter : RVHomeAdapter = RVHomeAdapter()

        val btnRequestUser = view.findViewById<Button>(R.id.btnRequestUser)

        btnRequestUser.setOnClickListener{
            requestPermission()
        }
    }

    private fun writeExternalStoragePermission() =
        ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED

    private fun LocationPermission() =
        ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED

    private fun LocationBackgroundPermission() =
        ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.ACCESS_BACKGROUND_LOCATION) ==
                PackageManager.PERMISSION_GRANTED

    private fun requestPermission(){
        var requestPermissionUser = mutableListOf<String>()
        if (!writeExternalStoragePermission()){
            requestPermissionUser.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (!LocationPermission()){
            requestPermissionUser.add(android.Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        if (!LocationBackgroundPermission()){
            requestPermissionUser.add(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }

        if(requestPermissionUser.isNotEmpty()){
            ActivityCompat.requestPermissions(requireActivity(), requestPermissionUser.toTypedArray(),0)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0 && grantResults.isNotEmpty()){
            for (i in grantResults.indices){
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED){
                    Log.d("PermissionUser", "${permissions} yang anda jalankan berhasil...")
                }
            }
        }
    }
}