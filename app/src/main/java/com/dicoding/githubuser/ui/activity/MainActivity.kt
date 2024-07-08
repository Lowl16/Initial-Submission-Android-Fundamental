package com.dicoding.githubuser.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.data.response.ItemsItem
import com.dicoding.githubuser.databinding.ActivityMainBinding
import com.dicoding.githubuser.ui.adapter.UserAdapter
import com.dicoding.githubuser.ui.viewmodel.UserViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val userViewModel by viewModels<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.text = searchView.text
                    searchView.hide()
                    if (searchBar.text?.isEmpty() == true) {
                        userViewModel.findUser()
                        Toast.makeText(this@MainActivity, "Enter a Character", Toast.LENGTH_SHORT).show()
                    } else {
                        userViewModel.findUser(searchBar.text.toString())
                    }
                    false
                }
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)

        userViewModel.user.observe(this) { user ->
            setUserData(user)
        }

        userViewModel.isLoading.observe(this) { loading ->
            showLoading(loading)
        }
    }

    private fun setUserData(user: List<ItemsItem>) {
        val adapter = UserAdapter()
        adapter.submitList(user)
        binding.rvUser.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}