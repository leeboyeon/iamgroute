package com.ssafy.groute.src.main.my

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentBoardWriteBinding
import com.ssafy.groute.databinding.FragmentMyTravelBinding
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.viewmodel.PlanViewModel
import kotlinx.coroutines.runBlocking

class MyTravelFragment : BaseFragment<FragmentMyTravelBinding>(FragmentMyTravelBinding::bind, R.layout.fragment_my_travel) {
//    private lateinit var binding: FragmentMyTravelBinding
    private var mytravelAdapter:MyTravelAdapter = MyTravelAdapter()
    private lateinit var mainActivity:MainActivity
    private val planViewModel:PlanViewModel by activityViewModels()
    val ing = mutableListOf<MyTravel>()
    val ed = mutableListOf<MyTravel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.userplan = planViewModel
        runBlocking {
            planViewModel.getPlanMyList(ApplicationClass.sharedPreferencesUtil.getUser().id)
        }
        planedAdapter()
    }
    fun planedAdapter(){
        planViewModel.planMyList.observe(viewLifecycleOwner, Observer {
            mytravelAdapter = MyTravelAdapter()
            mytravelAdapter.list = it
            mytravelAdapter.setItemClickListener(object: MyTravelAdapter.ItemClickListener{
                override fun onClick(view: View, position: Int, id:Int) {
                    mainActivity.moveFragment(7,"planId", it[position].id)
                }

            })
            binding.mytravelRvIng.apply{
                layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
                adapter = mytravelAdapter
                adapter!!.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }
        })
    }
    companion object {
        @JvmStatic
        fun newInstance(key: String, value: String) =
            MyTravelFragment().apply {

            }
    }
}