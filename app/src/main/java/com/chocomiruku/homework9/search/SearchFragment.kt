package com.chocomiruku.homework9.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import com.chocomiruku.homework9.databinding.FragmentSearchBinding
import com.jakewharton.rxbinding4.widget.queryTextChanges
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val disposeBag = CompositeDisposable()

    private val viewModel: SearchViewModel by lazy {
        ViewModelProvider(
            this,
            SearchViewModelFactory(requireActivity().application)
        )[SearchViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        val adapter = ModelAdapter(viewModel.oldFilteredModels)
        binding.parsedModelsList.adapter = adapter

        val searchQueryObservable = binding.search.queryTextChanges()

        disposeBag.add(searchQueryObservable
            .debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.isNullOrBlank()) {
                    hideResultsList()
                } else {
                    viewModel.search(it.toString())
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            val diffResult = DiffUtil.calculateDiff(
                                ModelAdapter.ModelsDiffUtilCallback(
                                    viewModel.oldFilteredModels,
                                    viewModel.filteredModels
                                )
                            )
                            viewModel.oldFilteredModels.clear()
                            viewModel.oldFilteredModels.addAll(viewModel.filteredModels)
                            diffResult.dispatchUpdatesTo(adapter)
                            showResultsList()
                        }
                }
            }
        )

        return binding.root
    }

    private fun showResultsList() {
        binding.parsedModelsList.visibility = View.VISIBLE
        binding.noResultsText.visibility = View.GONE
    }

    private fun hideResultsList() {
        binding.parsedModelsList.visibility = View.GONE
        binding.noResultsText.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        disposeBag.dispose()
    }
}