package com.mhmdawad.superest.presentation.main.fragment.track

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.mhmdawad.superest.R
import com.mhmdawad.superest.databinding.FragmentTrackOrdersBinding


class TrackOrdersFragment : Fragment() {

    private lateinit var binding: FragmentTrackOrdersBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_track_orders, container, false)
        return binding.root
    }


}