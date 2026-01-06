package com.example.oil_forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.oil_forecast.Extension.launchAndRepeatWithViewLifecycle
import com.example.oil_forecast.ViewModels.AQIViewModel
import com.example.oil_forecast.databinding.FragmentAqiListBinding
import com.example.oil_forecast.ui.Adapter.AqiAdapter
import com.example.oil_forecast.ui.Adapter.AqiListItem
import com.example.oil_forecast.ui.Decorator.CustomDividerItemDecoration
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AqiListFragment : Fragment() {
    private var _binding: FragmentAqiListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AQIViewModel by viewModel()

    private lateinit var aqiAdapter: AqiAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAqiListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObservers()
    }

    override fun onCreateOptionsMenu(
        menu: Menu,
        inflater: MenuInflater,
    ) {
        inflater.inflate(R.menu.option_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_show_by_dscending) {
            viewModel.sortByDescending()
            return true
        }
        if (item.itemId == R.id.menu_show_by_ascending) {
            viewModel.sortByAscending()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initView() {
        setHasOptionsMenu(true)
        aqiAdapter =
            AqiAdapter { item ->
                AQIDetailFragment
                    .newInstance(item)
                    .show(parentFragmentManager, getString(R.string.forecast_dialog_tag))
            }
        binding.rvAqiList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = aqiAdapter
            addItemDecoration(CustomDividerItemDecoration(requireContext()))
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchAQIs()
        }
    }

    private fun initObservers() {
        launchAndRepeatWithViewLifecycle {
            launch {
                viewModel.aqiAllLocations.collect { locations ->
                    if (locations.isEmpty()) {
                        aqiAdapter.submit(emptyList())
                        return@collect
                    }

                    val groupedData = mutableListOf<AqiListItem>()
                    val countsByStatus = locations.groupingBy { it.status }.eachCount()

                    var currentStatus: String? = null
                    var rank = 1
                    locations.forEach { aqiEntity ->
                        if (aqiEntity.status != currentStatus) {
                            currentStatus = aqiEntity.status
                            countsByStatus[currentStatus]?.let {
                                groupedData.add(AqiListItem.HeaderItem(currentStatus, it))
                            }
                        }
                        groupedData.add(AqiListItem.AqiItem(aqiEntity, rank++))
                    }
                    aqiAdapter.submit(groupedData)
                }
            }
            launch {
                viewModel.isLoading.collect { isLoading ->
                    binding.swipeRefreshLayout.isRefreshing = isLoading
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
