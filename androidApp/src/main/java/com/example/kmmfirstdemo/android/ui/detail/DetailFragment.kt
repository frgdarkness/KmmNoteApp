package com.example.kmmfirstdemo.android.ui.detail

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.kmmfirstdemo.DatabaseDriverFactory
import com.example.kmmfirstdemo.android.R
import com.example.kmmfirstdemo.android.databinding.FragmentDetailBinding
import com.example.kmmfirstdemo.android.ui.MyViewModelFactory
import com.example.kmmfirstdemo.android.util.Constant
import com.example.kmmfirstdemo.android.util.Constant.EMPTY_STRING
import com.example.kmmfirstdemo.datasource.NoteRepositoryImpl

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private lateinit var viewModel: DetailViewModel
    private var itemUndo: MenuItem? = null
    private var itemRedo: MenuItem? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = DetailFragmentArgs.fromBundle(requireArguments()).id
        val noteRepository = NoteRepositoryImpl(DatabaseDriverFactory(requireContext()))
        val viewModelFactory = MyViewModelFactory(noteRepository)
        viewModel =
            ViewModelProvider(requireActivity(), viewModelFactory)[DetailViewModel::class.java]

        if (id != Constant.INVALID_ID) {
            viewModel.loadNote(id)
        } else {
            viewModel.createNote()
        }
        observeViewModel()
        initEvent()
    }

    private fun initEvent() {
        binding.edtContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(editable: Editable?) {
                viewModel.updateContent(editable?.toString() ?: EMPTY_STRING)
            }
        })
    }

    private fun observeViewModel() {
        viewModel.note.observe(viewLifecycleOwner) { note ->
            binding.apply {
                edtTitle.setText(note.title)
                edtTitle.setSelection(note.title?.length ?: 0)
                edtContent.setText(note.content)
                edtContent.setSelection(note.content?.length ?: 0)
            }
        }
        viewModel.undoAvailable.observe(viewLifecycleOwner) { available ->
            itemUndo?.icon = if (available) {
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_undo_24)
            } else {
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_undo_deactive_24)
            }
        }
        viewModel.redoAvailable.observe(viewLifecycleOwner) { available ->
            itemRedo?.icon = if (available) {
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_redo_24)
            } else {
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_redo_deactive_24)
            }

        }
    }

    override fun onStop() {
        val title = binding.edtTitle.text.toString()
        val content = binding.edtContent.text.toString()
        if (title.isEmpty() && content.isEmpty()) {
            viewModel.deleteNote()
        } else {
            updateNote()
        }
        super.onStop()
    }

    override fun onDestroyOptionsMenu() {
        itemUndo = null
        itemRedo = null
        super.onDestroyOptionsMenu()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.detail_note_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        itemUndo = menu.findItem(R.id.itemUndo)
        itemRedo = menu.findItem(R.id.itemRedo)
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemSave -> {
                updateNote()
                findNavController().popBackStack()
            }
            R.id.itemDelete -> {
                viewModel.deleteNote()
                findNavController().popBackStack()
            }
            R.id.itemUndo -> {
                viewModel.undo()
            }
            R.id.itemRedo -> {
                viewModel.redo()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateNote() {
        viewModel.updateNote(
            binding.edtTitle.text.toString(),
            binding.edtContent.text.toString()
        )
    }
}
