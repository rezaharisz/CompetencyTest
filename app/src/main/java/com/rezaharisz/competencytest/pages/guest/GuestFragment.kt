package com.rezaharisz.competencytest.pages.guest

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.rezaharisz.competencytest.R
import com.rezaharisz.competencytest.databinding.FragmentGuestBinding
import com.rezaharisz.competencytest.utils.StatusBar
import com.rezaharisz.competencytest.helper.ViewModelFactory
import com.rezaharisz.competencytest.utils.Status
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class GuestFragment : Fragment(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private var _binding: FragmentGuestBinding? = null
    private val binding get() = _binding
    private lateinit var guestAdapter: GuestAdapter
    private lateinit var viewModel: GuestViewModel
    private lateinit var gridLayoutManager: GridLayoutManager
    private var page = 1
    private var perPage = 10
    private var totalPages = 1
    private var isLoading = false

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

            getData(false)

            with(binding?.rvGuests){
                this?.layoutManager = gridLayoutManager
                this?.setHasFixedSize(true)
                this?.adapter = guestAdapter
            }

            binding?.swipeRefresh?.setOnRefreshListener(this)
            binding?.btnBack?.setOnClickListener(this)
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): GuestViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[GuestViewModel::class.java]
    }

    private fun initAdapter(){
        guestAdapter = GuestAdapter()
        gridLayoutManager = GridLayoutManager(context, 2)

        with(binding?.rvGuests){
            this?.layoutManager = gridLayoutManager
            this?.setHasFixedSize(true)
            this?.adapter = guestAdapter

//            this?.addOnScrollListener(object : RecyclerView.OnScrollListener(){
//                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                    val visibleItemCount = gridLayoutManager.childCount
//                    val firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition()
//                    val total = guestAdapter.itemCount
//
//                    if (!isLoading && page < totalPages){
//                        if (visibleItemCount + firstVisibleItem >= total){
//                            page++
//                            getData(false)
//                        }
//                    }
//
//                    super.onScrolled(recyclerView, dx, dy)
//                }
//            })
        }
    }

    private fun getData(isRefresh: Boolean){
//        isLoading = true

//        if (!isRefresh){
//            binding?.progressBar?.visibility = View.VISIBLE
//        }

        val params = HashMap<String, String>()
        params["page"] = page.toString()
        params["per_page"] = perPage.toString()

        viewModel.getAllUser(params).observe(viewLifecycleOwner){ data ->
            runBlocking {
                val value = async { data }
                val result = value.await()
                if (result != null){
                    when(data.status){
                        Status.LOADING -> {
                            binding?.progressBar?.visibility = View.VISIBLE
                            Log.e("STATUS_LOADING", "STATUS_LOADING")
                        }
                        Status.SUCCESS -> {
                            binding?.progressBar?.visibility = View.GONE
                            guestAdapter.submitList(result.data)
                            guestAdapter.notifyDataSetChanged()
                            binding?.swipeRefresh?.isRefreshing = false
                            Log.e("STATUS_SUCCESS", "STATUS_SUCCESS")
                        }
                        Status.ERROR -> {
                            binding?.progressBar?.visibility = View.GONE
                            Log.e("STATUS_ERROR", "STATUS_ERROR")
                        }
                    }
                }
            }

//            runBlocking {
//                val value = async { data }
//                val result = value.await()
//
//                if (data != null){
//                    when(data.status){
//                        Status.LOADING -> binding?.progressBar?.visibility = View.VISIBLE
//                        Status.SUCCESS -> {
//                            binding?.progressBar?.visibility = View.GONE
//                            guestAdapter.submitList(data.data)
//                        }
//                        Status.ERROR -> {
//                            binding?.progressBar?.visibility = View.GONE
//                        }
//                    }
//                }
//
//                result.totalPages?.let { data -> totalPages = data }
//                result.data?.let { data -> guestAdapter.set(data) }
//                binding?.progressBar?.visibility = View.GONE
//                isLoading = false
//                binding?.swipeRefresh?.isRefreshing = false
//            }
        }
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btn_back -> parentFragmentManager.popBackStack()
        }
    }

    override fun onRefresh() {
        viewModel.clearData()
        page = 1
        getData(true)
    }

}