package com.rezaharisz.competencytest.pages.guest

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.rezaharisz.competencytest.R
import com.rezaharisz.competencytest.databinding.FragmentGuestBinding
import com.rezaharisz.competencytest.utils.StatusBar
import com.rezaharisz.competencytest.helper.ViewModelFactory
import com.rezaharisz.competencytest.utils.Status

class GuestFragment : Fragment(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private var _binding: FragmentGuestBinding? = null
    private val binding get() = _binding
    private lateinit var guestAdapter: GuestAdapter
    private lateinit var viewModel: GuestViewModel
    private lateinit var gridLayoutManager: GridLayoutManager
    private var page = 1
    private var perPage = 10
    private var visibleItem = 0
    private var itemCount = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentGuestBinding.inflate(inflater, container, false)

        val statusBarHeight = StatusBar().getHeight(resources)
        binding?.toolbarGuests?.setPadding(0, statusBarHeight, 0, 0)

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null){
            viewModel = obtainViewModel(activity as AppCompatActivity)

            guestAdapter = GuestAdapter()
            gridLayoutManager = GridLayoutManager(context, 2)

            getData()

            binding?.swipeRefresh?.setOnRefreshListener(this)
            binding?.btnBack?.setOnClickListener(this)
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): GuestViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[GuestViewModel::class.java]
    }

    private fun initAdapter(){
        with(binding?.rvGuests){
            this?.layoutManager = gridLayoutManager
            this?.setHasFixedSize(true)
            this?.adapter = guestAdapter
            this?.addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val visibleItemCount = gridLayoutManager.childCount
                    val firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition()
                    visibleItem = visibleItemCount + firstVisibleItem
                    itemCount = guestAdapter.itemCount

                    Log.e("CHECK3", visibleItem.toString())
                    Log.e("CHECK4", itemCount.toString())

                    super.onScrolled(recyclerView, dx, dy)
                }
            })
        }
    }

    private fun getData(){
        val params = HashMap<String, String>()
        params["page"] = page.toString()
        params["per_page"] = perPage.toString()

        lifecycleScope.launchWhenCreated {
            viewModel.getAllUser(visibleItem, itemCount).observe(viewLifecycleOwner){ data ->
                if (data != null){
                    when(data.status){
                        Status.LOADING -> {
                            binding?.progressBar?.visibility = View.VISIBLE
                        }
                        Status.SUCCESS -> {
                            binding?.progressBar?.visibility = View.GONE

                            initAdapter()
                            guestAdapter.submitList(data.data)

                            binding?.swipeRefresh?.isRefreshing = false
                        }
                        Status.ERROR -> {
                            binding?.progressBar?.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btn_back -> parentFragmentManager.popBackStack()
        }
    }

    override fun onRefresh() {
        viewModel.clearData()
        getData()
    }

}