package com.kay.happyplaces.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kay.happyplaces.R
import com.kay.happyplaces.databinding.FragmentAddHappyPlaceBinding
import com.kay.happyplaces.databinding.FragmentHappyPlacesBinding


class AddHappyPlaceFragment : Fragment() {

    private var _binding: FragmentAddHappyPlaceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddHappyPlaceBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}