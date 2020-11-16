package com.android.charly.ui.recipe.recipelist.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.charly.R
import com.android.charly.persistence.Note
import com.android.charly.ui.recipe.recipelist.adapter.NoteAdapter
import com.android.charly.ui.viewmodel.NoteViewModel
import com.android.charly.utils.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject


class ListFragment : DaggerFragment()
{

    private lateinit var noteAdapter: NoteAdapter
    private lateinit var navController: NavController
    private lateinit var noteViewModel: NoteViewModel
    @Inject
    lateinit var viewmodelProviderFactory: ViewModelProviderFactory
    private lateinit var allNotes: List<Note>
    private lateinit var mImageButton: ImageButton
    private lateinit var mTextView: TextView


    // Method #1
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        // set toolbar as support action bar
        (activity as AppCompatActivity?)?.setSupportActionBar(toolbar)
        mTextView = view.findViewById(R.id.header)
        mImageButton = view.findViewById(R.id.nav_button)
        mImageButton.setImageResource(R.drawable.add)
        mTextView.setText(R.string.str_all_recipes)
        allNotes = mutableListOf()
        navController = Navigation.findNavController(requireActivity(),R.id.container)
        return view
    }

    // Method #2
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()    // Step 1
        initRecyclerView()  // Step 2
        observerLiveData()  // Step 3
    }

    // Method #3
    private fun observerLiveData() {
        noteViewModel.getAllNotes().observe(viewLifecycleOwner, Observer { lisOfNotes ->
            lisOfNotes?.let {
                allNotes = it
                noteAdapter.swap(it)
            }
        })
    }

    // Method #4
    private fun initRecyclerView() {
        recyclerView.apply {
            noteAdapter = NoteAdapter(
                allNotes)
            layoutManager = LinearLayoutManager(this@ListFragment.context)
            adapter = noteAdapter
        }

        // toolbar button click listener
        nav_button.setOnClickListener {
            onAddButtonClicked()
        }

    }

    // Method #5
    private fun setupViewModel() {
        noteViewModel =
            ViewModelProvider(this, viewmodelProviderFactory).get(NoteViewModel::class.java)
    }


    // Method #6
    private fun onAddButtonClicked() {
        navController.navigate(R.id.action_listFragment_to_addFragment)
    }

}


