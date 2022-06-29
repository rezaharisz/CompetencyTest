package com.rezaharisz.competencytest.pages.on_board

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rezaharisz.competencytest.R
import com.rezaharisz.competencytest.databinding.FragmentOnBoardBinding
import com.rezaharisz.competencytest.pages.event.EventFragment
import com.rezaharisz.competencytest.pages.guest.GuestFragment
import com.rezaharisz.competencytest.utils.PREFERENCES
import com.rezaharisz.competencytest.utils.USER_NAME

class OnBoardFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentOnBoardBinding? = null
    private val binding get() = _binding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentOnBoardBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null){
            val sharedPreferences = context?.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
            val name = sharedPreferences?.getString(USER_NAME, "")

            binding?.tvUser?.text = StringBuilder("$name!")
            binding?.btnEvent?.setOnClickListener(this)
            binding?.btnGuest?.setOnClickListener(this)
        }
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btn_event -> {
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.frame_container, EventFragment(), EventFragment::class.java.canonicalName)
                    addToBackStack(null)
                    commit()
                }
            }

            R.id.btn_guest -> {
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.frame_container, GuestFragment(), GuestFragment::class.java.canonicalName)
                    addToBackStack(null)
                    commit()
                }
            }
        }
    }

}