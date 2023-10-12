package com.syafiqfajrianemha.storyapp.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.syafiqfajrianemha.storyapp.R
import com.syafiqfajrianemha.storyapp.adapter.LoadingStateAdapter
import com.syafiqfajrianemha.storyapp.adapter.StoryAdapter
import com.syafiqfajrianemha.storyapp.databinding.ActivityMainBinding
import com.syafiqfajrianemha.storyapp.view.ViewModelFactory
import com.syafiqfajrianemha.storyapp.view.addstory.AddStoryActivity
import com.syafiqfajrianemha.storyapp.view.maps.MapsActivity
import com.syafiqfajrianemha.storyapp.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
    }

    private fun setupView() {
        binding.rvStories.setHasFixedSize(true)
        binding.rvStories.layoutManager = LinearLayoutManager(this)

        mainViewModel.getUser().observe(this) { user ->
            if (user.isLogin) {
                val adapter = StoryAdapter()
                binding.rvStories.adapter = adapter.withLoadStateFooter(
                    footer = LoadingStateAdapter {
                        adapter.retry()
                    }
                )

//                mainViewModel.getStories(user.token).observe(this) {
//                    adapter.submitData(lifecycle, it)
//                }
                mainViewModel.stories.observe(this) {
                    adapter.submitData(lifecycle, it)
                }
            } else {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }
    }

    private fun setupAction() {
        binding.btnAddStory.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.maps -> {
                startActivity(Intent(this, MapsActivity::class.java))
            }

            R.id.logout -> {
                AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setCancelable(false)
                    .setPositiveButton("Logout") { _, _ ->
                        mainViewModel.logout()
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.cancel()
                    }
                    .show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}