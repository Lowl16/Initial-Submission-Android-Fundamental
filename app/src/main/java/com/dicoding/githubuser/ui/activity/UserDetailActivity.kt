package com.dicoding.githubuser.ui.activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.githubuser.R
import com.dicoding.githubuser.data.response.GitHubUserDetailResponse
import com.dicoding.githubuser.databinding.ActivityDetailUserBinding
import com.dicoding.githubuser.ui.adapter.SectionPagerAdapter
import com.dicoding.githubuser.ui.viewmodel.UserDetailViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private val userDetailViewModel by viewModels<UserDetailViewModel>()
    private lateinit var adapter: SectionPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "GitHub User Detail"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val username = intent.getStringExtra("username")

        userDetailViewModel.detailUser(username.toString())

        userDetailViewModel.user.observe(this) { user ->
            setUser(user)
        }

        userDetailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val sectionPagerAdapter = SectionPagerAdapter(this)
        adapter = sectionPagerAdapter

        if (username != null) {
            adapter.username = username
        }

        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionPagerAdapter
        val tabLayout: TabLayout = binding.tabLayout

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setUser(user: GitHubUserDetailResponse) {
        with(binding) {
            tvName.text = user.name
            tvUsername.text = user.login
            tvFollowers.text = "${user.followers} Followers"
            tvRepositories.text = "${user.publicRepos} Repositories"
            tvFollowing.text = "${user.following} Following"
            Glide.with(this@UserDetailActivity)
                .load(user.avatarUrl)
                .into(imgUserPhoto)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }
}