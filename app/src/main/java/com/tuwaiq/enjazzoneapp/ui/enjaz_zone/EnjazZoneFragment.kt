package com.tuwaiq.enjazzoneapp.ui.enjaz_zone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tuwaiq.enjazzoneapp.R

class EnjazZoneFragment : Fragment() {

    private lateinit var enjazZoneViewModel: EnjazZoneViewModel
    private lateinit var tvWelcomeMessage: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_enjaz_zone, container, false)
        enjazZoneViewModel =
            ViewModelProvider(this).get(EnjazZoneViewModel::class.java)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvWelcomeMessage = view.findViewById(R.id.text_enjaz_zone)
    }
}