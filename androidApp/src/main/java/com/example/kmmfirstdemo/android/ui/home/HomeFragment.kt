package com.example.kmmfirstdemo.android.ui.home

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.kmmfirstdemo.DatabaseDriverFactory
import com.example.kmmfirstdemo.android.R
import com.example.kmmfirstdemo.android.databinding.FragmentHomeBinding
import com.example.kmmfirstdemo.android.ui.MyViewModelFactory
import com.example.kmmfirstdemo.android.ui.adapter.NoteAdapter
import com.example.kmmfirstdemo.datasource.NoteRepositoryImpl

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private var noteAdapter: NoteAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val noteRepositoryImpl = NoteRepositoryImpl(DatabaseDriverFactory(requireContext()))
        val viewModelFactory = MyViewModelFactory(noteRepositoryImpl)
        viewModel =
            ViewModelProvider(requireActivity(), viewModelFactory)[HomeViewModel::class.java]
        setHasOptionsMenu(true)
        initView()
        initEvent()
        observeViewModel()
        viewModel.getAllNote()
    }

    private fun initView() {
        binding.recyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        }
    }

    private fun initEvent() {
        binding.addButton.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailFragment())
        }
        noteAdapter = NoteAdapter { noteId ->
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment()
            action.id = noteId
            findNavController().navigate(action)
        }
        binding.recyclerView.adapter = noteAdapter
    }

    private fun observeViewModel() {
        viewModel.noteList.observe(viewLifecycleOwner) {
            noteAdapter?.loadData(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.home_menu, menu)
        val menuItem = menu.findItem(R.id.itemSearch)
        val searchView = menuItem.actionView as? SearchView
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                viewModel.searchNote(
                    title = p0, content = p0
                )
                return true
            }
        })
    }
}
