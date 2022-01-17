package com.ssafy.groute.src.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.ssafy.groute.databinding.FragmentRouteBinding


class RouteFragment : Fragment() {
    lateinit var binding: FragmentRouteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRouteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pagerAdapter = RouteTabPageAdapter(requireActivity())

        val tabList = arrayListOf("당일치기", "1박 2일", "2박 3일", "3박 4일", "4박 5일")

        pagerAdapter.addFragment(RouteListFragment())
        pagerAdapter.addFragment(RouteListFragment())
        pagerAdapter.addFragment(RouteListFragment())
        pagerAdapter.addFragment(RouteListFragment())
        pagerAdapter.addFragment(RouteListFragment())

        binding.pager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = tabList.get(position)
        }.attach()

    }


}