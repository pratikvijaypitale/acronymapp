package com.pratik.acronymapp.views

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.pratik.acronymapp.R
import com.pratik.acronymapp.adapters.GenericListAdapter
import com.pratik.acronymapp.api.ApiResponse
import com.pratik.acronymapp.databinding.ActivityMainBinding
import com.pratik.acronymapp.models.AcronymValidation
import com.pratik.acronymapp.models.Lf
import com.pratik.acronymapp.utils.NetworkUtil
import com.pratik.acronymapp.viewmodels.MainViewModel

/*
* Created by Pratik Pitale
* App's functional requirement:
* 1. User can enter an acronym or initialism
* 2. User then presented with a list od corresponding meanings
* */
class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding //Data binding object
    private lateinit var mViewModel: MainViewModel //ViewModel
    private var mAdapter: GenericListAdapter<Lf>? = null //Adapter class
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        mViewModel =
            ViewModelProvider(this).get(MainViewModel::class.java)
        mBinding.viewModel = mViewModel
        mBinding.lifecycleOwner = this

        initLiveData()
        setListener()
        initAdapter()

    }

    //Init live data observers
    private fun initLiveData() {
        //acronymApiResponseLiveData observe the acronym API response
        mViewModel.acronymApiResponseLiveData.observe(this) {
            it?.let {
                when (it) {
                    is ApiResponse.Loader -> {
                        mViewModel.progressBarLiveData.postValue(View.VISIBLE)
                    }
                    is ApiResponse.Error -> {
                        mViewModel.progressBarLiveData.postValue(View.GONE)
                        notifyUser(it.errorMessage ?: getString(R.string.common_error_message))
                    }
                    is ApiResponse.Success -> {
                        mViewModel.progressBarLiveData.postValue(View.GONE)
                        if (it.data != null && it.data.isNotEmpty()) {
                            updateAdapter(it.data[0].lfs)
                        } else {
                            notifyUser(getString(R.string.no_data_found))
                        }
                    }
                }
            }
        }
    }

    //Basic click listener
    private fun setListener() {
        //Search view text change listener
        mBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(data: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(data: String?): Boolean {
                if (NetworkUtil.isNetworkAvailable(applicationContext)) {
                    if (AcronymValidation.isValidInput(data)) {
                        mViewModel.onSearchTextChanged(data ?: "")
                    }
                    updateAdapter(mutableListOf()) //Update empty list
                } else {
                    notifyUser(getString(R.string.no_network))
                }
                return true
            }

        })
    }

    //Initialize recyclerview data adapter
    private fun initAdapter() {
        mAdapter = GenericListAdapter(dataList = mutableListOf(), onForeignBind = ::onAdapterBind)
        mBinding.recyclerView.adapter = mAdapter
    }

    //To update recyclerview content list
    private fun updateAdapter(dataList: List<Lf>) {
        mAdapter?.refreshList(dataList as MutableList<Lf>)
    }

    //Callback function of generic adapter class
    private fun onAdapterBind(
        holder: GenericListAdapter<Lf>.GenericViewHolder,
        data: Lf,
        position: Int
    ) {
        holder.binding.tvField.text = data.lf
        holder.binding.cardView.setOnClickListener {
            notifyUser(data.lf)
        }
    }

    //Display message to user on screen
    private fun notifyUser(message: String) {
        Snackbar.make(mBinding.parentLay, message, Snackbar.LENGTH_SHORT).show()
    }
}