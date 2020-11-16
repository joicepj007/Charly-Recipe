package com.android.charly.ui.recipe.recipedetail.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.charly.R
import com.android.charly.utils.ImageListener
import com.android.charly.utils.ImagePicker
import com.android.charly.ui.recipe.recipedetail.adapter.RecipeImageRecycleAdapter
import com.android.charly.persistence.Note
import com.android.charly.ui.viewmodel.NoteViewModel
import com.android.charly.utils.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject


class AddFragment : DaggerFragment(), RecipeImageRecycleAdapter.CallbackInterface {

    @Inject
    lateinit var viewmodelProviderFactory: ViewModelProviderFactory
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var recipeImageRecycleAdapter: RecipeImageRecycleAdapter
    private lateinit var imagePicker: ImagePicker
    private val REQ_GALLARY = 2
    private lateinit var mRecyclerView:RecyclerView
    private lateinit var mImageButton: ImageButton
    private lateinit var mTextView: TextView
    private lateinit var image: MutableList<String?>
    // Method #1
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add, container, false)
        // set toolbar as support action bar
        (activity as AppCompatActivity?)?.setSupportActionBar(toolbar)
        imagePicker = ImagePicker.Builder(this)
            .setListener(imageListener)
            .build()
        mImageButton = view.findViewById(R.id.nav_button)
        mTextView = view.findViewById(R.id.header)
        mImageButton.setImageResource(R.drawable.ic_save)
        mTextView.setText(R.string.str_add_recipe)
        //getting recyclerview from xml
        mRecyclerView = view?.findViewById(R.id.recycle_product_image) as RecyclerView
        val recylerViewLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(
                activity,
            LinearLayoutManager.HORIZONTAL, false
        )
        mRecyclerView.layoutManager = recylerViewLayoutManager
        recipeImageRecycleAdapter = RecipeImageRecycleAdapter(this)
        mRecyclerView.adapter = recipeImageRecycleAdapter


        recipeImageRecycleAdapter.setImageRecycleClick(object :
            RecipeImageRecycleAdapter.ImageRecycleClick {
            override fun onItemClick(view: View?, position: Int) {
            }
            override fun onDeleteClick(
                view: View?,
                position: Int
            ) {
                recipeImageRecycleAdapter.deleteImage(position)
            }
            override fun onimagePickerClick() {
                imagePicker.performImgPicAction(
                        REQ_GALLARY,
                        1
                )
            }
        })
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let { imagePicker.onActivityResult(requestCode, resultCode, it) }
    }

    // Method #2
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()

        nav_button.setOnClickListener {
            view.hideKeyboard()
            saveNoteToDatabase()
        }
    }

    // Method #3
    private fun saveNoteToDatabase() =/* VALIDATIONS FOR SAVING NOTES

        if all the property of notes is not null i.e title , description , images
        then note will saved
        show toast "Note is saved"
         */

            if (validations()) {
                saveNote()
            } else
                Toast.makeText(activity, getString(R.string.str_validation_recipe), Toast.LENGTH_SHORT).show()

    // Method #5
    private fun saveNote() {
        val note =
            Note(0,addTitle.text.toString(),addDescription.text.toString(),image)


        when {
            addTitle.text.isNullOrEmpty() -> {
                Toast.makeText(activity, getString(R.string.str_validation_title), Toast.LENGTH_SHORT).show()
            }
            addDescription.text.isNullOrEmpty() -> {
                Toast.makeText(activity, getString(R.string.str_validation_desc), Toast.LENGTH_SHORT).show()
            }
            image.isEmpty() -> { Toast.makeText(activity, getString(R.string.str_validation_images), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(activity, getString(R.string.str_saved), Toast.LENGTH_SHORT).show()
                //Call viewmodel to save the data
                noteViewModel.insert(note)
                Navigation.findNavController(requireActivity(),R.id.container).popBackStack()
            }
        }

    }

    // Method #6
    private fun validations(): Boolean {
        return !(addTitle.text.isNullOrEmpty()
                && addDescription.text.isNullOrEmpty() && image.toString().isEmpty())
    }


    // Method #7
    private fun setupViewModel() {
        noteViewModel = ViewModelProvider(this,viewmodelProviderFactory).get(NoteViewModel::class.java)
    }

    private var imageListener: ImageListener = object : ImageListener {
        override fun onImagePick(reqCode: Int, path: String?) {
            activity?.runOnUiThread {

                recipeImageRecycleAdapter.addData(path)
                mRecyclerView.layoutManager?.scrollToPosition(recipeImageRecycleAdapter.itemCount)
                recycleViewPositionChange(recipeImageRecycleAdapter.itemCount - 2)
            }
        }

        override fun onError(s: String?) {
            activity?.runOnUiThread {
                Toast.makeText(
                    activity,
                    s,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    private fun recycleViewPositionChange(position: Int) {
        recipeImageRecycleAdapter.changePosition(position)
    }
    override fun passDataCallback(img: MutableList<String?>) {
        image=img
    }
    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

}