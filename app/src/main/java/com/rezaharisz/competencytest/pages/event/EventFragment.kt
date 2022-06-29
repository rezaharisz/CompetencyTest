package com.rezaharisz.competencytest.pages.event

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rezaharisz.competencytest.R
import com.rezaharisz.competencytest.databinding.FragmentEventBinding
import com.rezaharisz.competencytest.utils.StatusBar

class EventFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentEventBinding? = null
    private val binding get() = _binding
    private var stateIcon = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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
        if (activity != null){
            binding?.btnBack?.setOnClickListener(this)
            binding?.btnMap?.setOnClickListener(this)
        }
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btn_back -> parentFragmentManager.popBackStack()
            R.id.btn_map -> setIcon()
        }
    }

    private fun setIcon(){
        if (stateIcon){
            binding?.btnMap?.setImageResource(R.drawable.ic_list_view)
        } else{
            binding?.btnMap?.setImageResource(R.drawable.ic_map_white)
        }
        stateIcon = !stateIcon
    }

}