package com.kay.happyplaces.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kay.happyplaces.R
import com.kay.happyplaces.databinding.FragmentAddHappyPlaceBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddHappyPlaceFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentAddHappyPlaceBinding? = null
    private val binding get() = _binding!!

    private var cal = Calendar.getInstance()
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener

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

        // initiate dateSetListener
        dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
        }
        binding.etDate.setOnClickListener(this)
    }

    override fun onClick(theViewWeClicked: View?) {
        when(theViewWeClicked!!.id){
            R.id.et_date ->{
                context?.let { DatePickerDialog(it, dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show() }
            }
        }
    }

    private fun updateDateInView(){
        val myFormat = "dd.MM.yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        binding.etDate.setText(sdf.format(cal.time).toString())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
