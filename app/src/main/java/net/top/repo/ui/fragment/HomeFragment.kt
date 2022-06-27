package net.top.repo.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import net.top.repo.R
import net.top.repo.api.response.home_page.GitResponse
import net.top.repo.databinding.FragmentHomeBinding
import net.top.repo.ui.adapter.HomeAdapter
import net.top.repo.utilities.*
import net.top.repo.viewmodels.HomeViewModel

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel by viewModels<HomeViewModel>()
    private lateinit var sessionHelper: SessionHelper
    private lateinit var homeAdapter: HomeAdapter
    private var selectedPosition = 0
    private lateinit var getRepoList: MutableList<GitResponse.Item>
    private lateinit var getRepoListByDate: MutableList<GitResponse.Item>
    private lateinit var getRepoListByStar: MutableList<GitResponse.Item>
    private var isDateFilterSelected: Boolean = true


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        initialSetup()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        homeViewModel.clearData()
        getRepoList.clear()
        getRepoListByDate.clear()
        getRepoListByStar.clear()
        _binding = null
    }


    private fun initialSetup() {
        getRepoList = mutableListOf()
        getRepoListByDate = mutableListOf()
        getRepoListByStar = mutableListOf()
        sessionHelper = SessionHelper(requireContext())
        sessionHelper = SessionHelper(requireContext().applicationContext)

        binding.btnDateFilter.setOnClickListener {
            sessionHelper.setDateFilterValue("1")
            sessionHelper.setStarFilterValue("0")
            isDateFilterSelected = true
            changeButtonBg()
            getRepoList.sortByDescending{it.updated_at}
            homeAdapter.setData(
                getRepoList
            )
        }

        binding.btnStarFilter.setOnClickListener {
            sessionHelper.setDateFilterValue("0")
            sessionHelper.setStarFilterValue("1")
            isDateFilterSelected = false
            changeButtonBg()
            getRepoList.sortByDescending{it.stargazers_count}
            homeAdapter.setData(
                getRepoList
            )
        }

        setUpRepoAdapter()
        getAndroidRepoList()
        observeRepoList()

    }
    private fun getAndroidRepoList() {
        homeViewModel.getRepoList(
            AppUtils.isNetworkAvailable(requireContext().applicationContext)
        )
    }

    private fun changeButtonBg() {
        if (isDateFilterSelected) {
            binding.btnDateFilter.setBackgroundResource(R.drawable.btn_dark_shape)
            binding.btnStarFilter.setBackgroundResource(R.drawable.btn_light_shape)
            binding.btnDateFilter.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
            binding.btnStarFilter.setTextColor(
                ContextCompat.getColor(
                    requireContext(), R.color.upg_app_color
                )
            )
        } else {
            binding.btnDateFilter.setBackgroundResource(R.drawable.btn_light_shape)
            binding.btnStarFilter.setBackgroundResource(R.drawable.btn_dark_shape)
            binding.btnDateFilter.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.upg_app_color
                )
            )
            binding.btnStarFilter.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
        }
    }
    private fun setUpRepoAdapter() {
        homeAdapter = HomeAdapter()
        homeAdapter.setOnclickListener(object : OnItemClickListener {
            override fun onItemClick(view: View, item: Any, position: Int) {
                selectedPosition = position
                val data = item as GitResponse.Item
                requireActivity().supportFragmentManager.commit {
                    replace(
                        net.top.repo.R.id.mainFragmentContainer,
                        RepoDetailsFragment.newInstance(
                            data
                           /* data.full_name,
                            data.description,
                            data.owner?.avatar_url,
                            data.name*/
                        ),
                        RepoDetailsFragment::class.java.canonicalName
                    )
                    setReorderingAllowed(true)
                    addToBackStack(RepoDetailsFragment::class.java.canonicalName)
                }

            }
        })
        binding.rvHomeList.layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.VERTICAL,
            false
        )
        binding.rvHomeList.adapter = homeAdapter
    }


   /* private fun observeRepoList() {
        homeViewModel.getRepoListResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Success -> {
                    hideProgressBar()
                        if (!it.data?.items.isNullOrEmpty()) {
                            getRepoList.addAll(it.data!!.items)

                            if(sessionHelper.getDateFilterValue().equals("1")){
                                isDateFilterSelected = true
                                changeButtonBg()
                                getRepoList.sortByDescending{it.updated_at}
                                homeAdapter.setData(
                                    getRepoList
                                )
                            }else if(sessionHelper.getStarFilterValue().equals("1")){
                                isDateFilterSelected = false
                                changeButtonBg()
                                getRepoList.sortByDescending{it.stargazers_count}
                                homeAdapter.setData(
                                    getRepoList
                                )
                            }else{
                                homeAdapter.setData(
                                    getRepoList
                                )
                            }

                        } else {

                            "${("No Data found")} ${it.message}".toast(
                                requireActivity()
                            )

                        }

                }
                is Resource.Error -> {
                    hideProgressBar()
                    "${("Failed to load due to ")} ${it.message}".toast(
                        requireActivity()
                    )
                }
                is Resource.Empty -> {
                    //do nothing
                }
            }
        }
    }*/


    private fun observeRepoList() {
        homeViewModel.getRepoListResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Success -> {
                    hideProgressBar()
                    if (it.data != null) {
                        if (!it.data?.items.isNullOrEmpty()) {
                            getRepoList.addAll(it.data!!.items)

                            if(sessionHelper.getDateFilterValue().equals("1")){
                                isDateFilterSelected = true
                                changeButtonBg()
                                getRepoList.sortByDescending{it.updated_at}
                                homeAdapter.setData(
                                    getRepoList
                                )
                            }else if(sessionHelper.getStarFilterValue().equals("1")){
                                isDateFilterSelected = false
                                changeButtonBg()
                                getRepoList.sortByDescending{it.stargazers_count}
                                homeAdapter.setData(
                                    getRepoList
                                )
                            }else{
                                homeAdapter.setData(
                                    getRepoList
                                )
                            }

                        } else {

                            "${("No Data found")} ${it.message}".toast(
                                requireActivity()
                            )

                        }


                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    val msg = "${it.message}"
                    msg.toast(
                        requireActivity(),
                        Toast.LENGTH_LONG
                    )
                }
                is Resource.Empty -> {
                    //do nothing
                }
            }
        }
    }

    private fun showProgressBar() {
        if (!binding.progressLayout.root.isVisible) {
            binding.progressLayout.root.visibility = View.VISIBLE
            requireActivity().window.disableUserInteraction()
        }
    }

    private fun hideProgressBar() {
        if (binding.progressLayout.root.isVisible) {
            binding.progressLayout.root.visibility = View.GONE
            requireActivity().window.enableUserInteraction()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment().apply {}
    }

}