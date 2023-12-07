package com.vp.favorites

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.widget.ViewAnimator
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vp.favorites.viewmodel.FavoriteViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class FavoriteActivity : DaggerAppCompatActivity(), ListAdapter.OnItemClickListener {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var listAdapter: ListAdapter
    private lateinit var viewAnimator: ViewAnimator
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        recyclerView = findViewById(R.id.recyclerView)
        viewAnimator = findViewById(R.id.viewAnimator)
        initList()
        val favoriteViewModel = ViewModelProvider(this, factory)[FavoriteViewModel::class.java]
        favoriteViewModel.allMovies.observe(this) {
            listAdapter.setItems(it)
        }
    }

    private fun initList() {
        listAdapter = ListAdapter()
        listAdapter.setOnItemClickListener(this)
        recyclerView.adapter = listAdapter
        recyclerView.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(
            this,
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3
        )
        recyclerView.layoutManager = layoutManager

    }

    override fun onItemClick(imdbID: String) {
        val dataUri = Uri.parse("app://movies/detail?imdbID=$imdbID")
        val intent = Intent(Intent.ACTION_VIEW, dataUri)
        startActivity(intent)
    }
}