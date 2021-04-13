package com.example.kuboss.warehouse

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kuboss.R

class ReceiveItemFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //val binding : ReceiveItemFragmentBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_receive_item,co)
        return inflater.inflate(R.layout.fragment_receive_item, container, false)
    }

}