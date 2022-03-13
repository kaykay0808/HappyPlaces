package com.kay.happyplaces.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kay.happyplaces.R
import com.kay.happyplaces.databinding.FragmentListHappyPlacesBinding

class ListHappyPlacesFragment : Fragment() {

    private var _binding: FragmentListHappyPlacesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentListHappyPlacesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Hide actionBar on this fragment
        // (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        // Navigation with floating button to add fragment
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_happyPlacesFragment_to_addHappyPlaceFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
