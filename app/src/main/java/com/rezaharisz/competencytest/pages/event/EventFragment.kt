package com.rezaharisz.competencytest.pages.event

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.rezaharisz.competencytest.R
import com.rezaharisz.competencytest.databinding.FragmentEventBinding
import com.rezaharisz.competencytest.helper.EventClickListener
import com.rezaharisz.competencytest.utils.DummyEvent
import com.rezaharisz.competencytest.utils.Event
import com.rezaharisz.competencytest.utils.StatusBar
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class EventFragment : Fragment(), View.OnClickListener, OnMapReadyCallback {

    private var _binding: FragmentEventBinding? = null
    private val binding get() = _binding
    private lateinit var mMap: GoogleMap
    private var stateIcon = false
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEventBinding.inflate(inflater, container, false)

        val statusBarHeight = StatusBar().getHeight(resources)
        binding?.toolbarEvent?.setPadding(0, statusBarHeight, 0, 0)

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {
            initAdapter()
            getDataEvent()
            initMaps()

            binding?.btnBack?.setOnClickListener(this)
            binding?.btnMap?.setOnClickListener(this)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_back -> parentFragmentManager.popBackStack()
            R.id.btn_map -> setStateIcon()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setOnMarkerClickListener {
            binding?.containerCardEvent?.visibility = View.VISIBLE
            false
        }
    }

    private fun setStateIcon() {
        if (stateIcon) {
            binding?.btnMap?.setImageResource(R.drawable.ic_list_view)
            binding?.frameMap?.visibility = View.VISIBLE
            binding?.rvEvents?.visibility = View.GONE
            eventAdapter.clear()
        } else {
            binding?.btnMap?.setImageResource(R.drawable.ic_map_white)
            binding?.rvEvents?.visibility = View.VISIBLE
            binding?.frameMap?.visibility = View.GONE
            binding?.containerCardEvent?.visibility = View.GONE
            getDataEvent()
        }
        stateIcon = !stateIcon
    }

    private fun initMaps() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun moveLocation(mapPosition: LatLng) {
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(mapPosition))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapPosition, 15f))
    }

    private fun getCardMapsData(data: Event) {
        binding?.cardEvent?.tvTitle?.text = data.title
        binding?.cardEvent?.tvDescription?.text = data.description
        binding?.cardEvent?.tvDate?.text = data.date
        binding?.cardEvent?.tvTime?.text = data.time
    }

    private fun initAdapter() {
        eventAdapter = EventAdapter(EventClickListener {
            val mapPosition = LatLng(it.latitude.toDouble(), it.longitude.toDouble())
            moveLocation(mapPosition)
            stateIcon = true
            setStateIcon()
            getCardMapsData(it)
        })

        with(binding?.rvEvents) {
            this?.layoutManager = LinearLayoutManager(context)
            this?.setHasFixedSize(true)
            this?.adapter = eventAdapter
        }
    }

    private fun getDataEvent() {
        val data = context?.let { DummyEvent.getData(it) }

        runBlocking {
            val value = async { data }
            val result = value.await()

            if (result != null) {
                eventAdapter.set(result)
            }
        }
    }

}