package com.example.ugd

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ugd.entity.Model


class FragmentList : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list,container,false)
    }
    // anjay mabar mantap
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        val adapter: RVModelAdapter = RVModelAdapter(Model.listOfModel)

        val rvModel : RecyclerView = view.findViewById(R.id.rv_model)

        rvModel.layoutManager=layoutManager
        rvModel.setHasFixedSize(true)
        rvModel.adapter=adapter
    }
}