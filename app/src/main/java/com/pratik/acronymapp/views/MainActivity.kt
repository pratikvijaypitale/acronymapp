package com.pratik.acronymapp.views

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.pratik.acronymapp.R
import com.pratik.acronymapp.adapters.GenericListAdapter
import com.pratik.acronymapp.api.AcronymApiService
import com.pratik.acronymapp.api.ApiResponse
import com.pratik.acronymapp.api.RetrofitHelper
import com.pratik.acronymapp.databinding.ActivityMainBinding
import com.pratik.acronymapp.models.AcronymValidation
import com.pratik.acronymapp.models.Lf
import com.pratik.acronymapp.repository.AcronymRepository
import com.pratik.acronymapp.utils.KeyboardUtil
import com.pratik.acronymapp.utils.NetworkUtil
import com.pratik.acronymapp.viewmodels.MainViewModel
import com.pratik.acronymapp.viewmodels.MainViewModelFactory

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

        val apiService = RetrofitHelper.getInstance().create(AcronymApiService::class.java)
        val repository = AcronymRepository(apiService)
        mViewModel =
            ViewModelProvider(this, MainViewModelFactory(repository)).get(MainViewModel::class.java)

        initLiveData()
        setListener()
        initAdapter()
    }

    //Init live data observers
    private fun initLiveData() {
        //acronymApiResponseLiveData observe the acronym API response
        mViewModel.acronymApiResponseLiveData.observe(this) {
            it?.let {
                mBinding.recyclerView.visibility = View.VISIBLE //Visible Recycler view
                mBinding.progressBar.visibility = View.GONE //Remove progress bar
                when (it) {
                    is ApiResponse.Error -> {
                        notifyUser(it.errorMessage ?: getString(R.string.common_error_message))
                    }
                    is ApiResponse.Success -> {
                        if (it.data != null && it.data.isNotEmpty()) {
                            updateAdapter(it.data[0].lfs)
                        } else {
                            notifyUser(getString(R.string.no_data_found))
                            updateAdapter(mutableListOf())
                        }
                    }
                }
            }
        }
    }

    //Basic click listener
    private fun setListener() {
        mBinding.searchButton.setOnClickListener {
            onSearchClick()
        }
    }

    //On search click
    private fun onSearchClick() {
        if (NetworkUtil.isNetworkAvailable(this)) {
            val etInput: String = mBinding.editText.text.toString()
            if (AcronymValidation.isValidInput(etInput)) {
                KeyboardUtil.closeSoftKeyboard(mBinding.editText) //Hide soft keyboard
                mBinding.recyclerView.visibility = View.INVISIBLE //Invisible Recycler view
                mBinding.progressBar.visibility = View.VISIBLE //Visible progress bar
                mViewModel.getAcronym(etInput)
            } else {
                notifyUser(getString(R.string.char_issue))
            }
        } else {
            notifyUser(getString(R.string.no_network))
        }
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
        Snackbar.make(mBinding.editText, message, Snackbar.LENGTH_SHORT).show()
    }
}