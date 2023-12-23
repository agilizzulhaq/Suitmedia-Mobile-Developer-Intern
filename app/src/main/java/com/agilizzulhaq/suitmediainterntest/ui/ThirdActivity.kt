package com.agilizzulhaq.suitmediainterntest.ui

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.agilizzulhaq.suitmediainterntest.R
import com.agilizzulhaq.suitmediainterntest.data.api.ApiClient
import com.agilizzulhaq.suitmediainterntest.data.api.ApiResponse
import com.agilizzulhaq.suitmediainterntest.data.model.User
import com.agilizzulhaq.suitmediainterntest.data.model.UserAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ThirdActivity : AppCompatActivity() {

    private lateinit var userAdapter: UserAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var currentPage = 1
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        setupActionBar()
        setupRecyclerView()
        setupSwipeRefreshLayout()
        fetchData()
    }

    private fun setupActionBar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar_third_screen)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        setStatusBarColor(ContextCompat.getColor(this, android.R.color.white))
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setStatusBarColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = color
            if (ColorUtils.calculateLuminance(color) > 0.5) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
            } else {
                window.decorView.systemUiVisibility = 0
            }
        }
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        userAdapter = UserAdapter(mutableListOf()) { selectedUser ->
            updateSelectedUserName(selectedUser)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = userAdapter

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                    ) {
                        currentPage++
                        fetchData()
                    }
                }
            }
        })
    }

    private fun setupSwipeRefreshLayout() {
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            currentPage = 1
            fetchData()
        }
    }

    private fun fetchData() {
        isLoading = true
        val perPage = 10
        val call: Call<ApiResponse> = ApiClient.apiService.getUsers(currentPage, perPage)

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val userList = response.body()?.data ?: emptyList()
                    if (currentPage == 1) {
                        userAdapter.clearUsers()
                    }
                    userAdapter.addUsers(userList)
                } else {
                    Toast.makeText(
                        this@ThirdActivity,
                        "Failed to fetch data",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                swipeRefreshLayout.isRefreshing = false
                isLoading = false
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Toast.makeText(this@ThirdActivity, "Network error", Toast.LENGTH_SHORT).show()

                swipeRefreshLayout.isRefreshing = false
                isLoading = false
            }
        })
    }

    private fun updateSelectedUserName(selectedUser: User) {
        val resultIntent = Intent()
        resultIntent.putExtra(
            "selectedUserName",
            "${selectedUser.first_name} ${selectedUser.last_name}"
        )
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}