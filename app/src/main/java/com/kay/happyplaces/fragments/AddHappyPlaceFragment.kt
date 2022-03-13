package com.kay.happyplaces.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kay.happyplaces.databinding.FragmentAddHappyPlaceBinding

class AddHappyPlaceFragment : Fragment() {
    private var _binding: FragmentAddHappyPlaceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddHappyPlaceBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Hide actionBar on this fragment
        // (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        // toolbar setup for fragments
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarAddPlace)
        // Back button setup on toolbar for fragments
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // navigate back with navHost
        binding.toolbarAddPlace.setNavigationOnClickListener {
            findNavController().popBackStack()
            // customDialogForBackButton()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
