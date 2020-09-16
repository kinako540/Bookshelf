package com.example.bookshelf.ui.gallery

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import com.example.bookshelf.ui.create.CreateActivity
import com.example.bookshelf.R


class GalleryFragment : Fragment() {

    private lateinit var GalleryFragment: GalleryViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        GalleryFragment =
                ViewModelProviders.of(this).get(GalleryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_create_top, container, false)
        //GalleryViewModel.text.observe(viewLifecycleOwner, Observer {
        //})




        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toastButton: Button = view.findViewById(R.id.button_self)
        toastButton.setOnClickListener {
            Toast.makeText(requireContext(), "こんにちは", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), CreateActivity::class.java)
            startActivity(intent)
        }

    }
}