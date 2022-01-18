package com.ssafy.groute.src.main.my

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.groute.R
import com.ssafy.groute.databinding.FragmentSaveTravelBinding
import com.ssafy.groute.src.main.route.RouteListRecyclerviewAdapter

class SaveTravelFragment : Fragment() {
    private lateinit var binding: FragmentSaveTravelBinding
    lateinit var RouteListAdapter: RouteListRecyclerviewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSaveTravelBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
    }
    fun initAdapter(){
        RouteListAdapter = RouteListRecyclerviewAdapter()
        binding.myRvSave.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = RouteListAdapter
        }
    }
    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SaveTravelFragment().apply {

            }
    }
}