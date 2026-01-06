package com.livestreaming.tv.ui.activity

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.livestreaming.tv.R
import com.livestreaming.tv.ui.fragments.profile.ProfileMenuFragment
import com.livestreaming.tv.utils.NetworkManager
import com.livestreaming.tv.utils.fragmentsWithTabBar
import com.livestreaming.tv.utils.toast
import com.livestreaming.tv.viewmodels.TabBarViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private lateinit var navController: NavController
    private lateinit var tabs: List<TextView>
    private var currentTab = 0
    private var isTabBarVisible = false
    private lateinit var networkManager: NetworkManager
    private val tabBarViewModel: TabBarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chekInternetConnection()
        if (savedInstanceState == null) {
            setupNavigation()
        }
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
            ?: return

        navController = navHostFragment.navController
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)

        // Setup tabs
        setupTabs()

        // Listen to navigation changes
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // Show/hide tab bar based on current destination
            val topBar = findViewById<View>(R.id.layout_top_bar)
            if (fragmentsWithTabBar.contains(destination.id)) {
                topBar?.visibility = View.VISIBLE
                isTabBarVisible = true
                updateTabSelection(destination.id)
            } else {
                topBar?.visibility = View.GONE
                isTabBarVisible = false
            }

            //Show or Hide Search Icon
            val searchIcon = topBar?.findViewById<View>(R.id.iv_search)
            if (destination.id == R.id.searchScreenFragment) {
                searchIcon?.visibility = View.GONE  // hide on SearchFragment
            } else {
                searchIcon?.visibility = View.VISIBLE  // show on other fragments
            }
        }



        navGraph.setStartDestination(R.id.splashScreenFragment)
        navController.graph = navGraph

        /*
        // Check if onboarding is completed and set appropriate start destination
        lifecycleScope.launch {
            mainViewModel.userPreferences.collect { preferences ->
                if (preferences.isOnboardingCompleted) {
                    // User has completed onboarding, go directly to ForYou tab
                    navGraph.setStartDestination(R.id.forYouFragment)
                } else {
                    // Show onboarding flow
                    navGraph.setStartDestination(R.id.languageSelectionFragment)
                }
                navController.graph = navGraph
            }
        }
         */
    }

    private fun setupTabs() {
        val topBarView = findViewById<View>(R.id.layout_top_bar) ?: return

        //Search Icon
        val searchIcon = topBarView.findViewById<View>(R.id.iv_search)
        searchIcon.setOnClickListener {
            navController.navigate(R.id.searchScreenFragment)
            tabs[0].isSelected = false
            tabs[1].isSelected = false
            tabs[2].isSelected = false
            tabs[3].isSelected = false
            tabs[4].isSelected = false
        }
        searchIcon.visibility = View.VISIBLE

        val profileIcon = topBarView.findViewById<ImageView>(R.id.iv_profile)
        profileIcon.setOnClickListener {
            ProfileMenuFragment().show(supportFragmentManager, ProfileMenuFragment.TAG)
        }

        tabs = listOf(
            topBarView.findViewById(R.id.tab_for_you),
            topBarView.findViewById(R.id.tab_movies),
            topBarView.findViewById(R.id.tab_live),
            topBarView.findViewById(R.id.tab_tv_shows),
            topBarView.findViewById(R.id.tab_subscriptions),
        )

        // Set For You tab as selected by default
        tabs[0].isSelected = true

        // Set up tab click listeners
        setupTabListener(tabs[0], R.id.forYouFragment, 0)
        setupTabListener(tabs[1], R.id.moviesFragment, 1)
        setupTabListener(tabs[2], R.id.liveFragment, 2)
        setupTabListener(tabs[3], R.id.tvShowsFragment, 3)
        setupTabListener(tabs[4], R.id.subscriptionFragment, 4)

    }

    private fun setupTabListener(tab: TextView, destinationId: Int, tabIndex: Int) {
        tab.setOnClickListener {
            navigateToTab(destinationId, tabIndex)
        }

        tab.setOnKeyListener { _, keyCode, event ->
            if (event.keyCode == KeyEvent.ACTION_DOWN) {
                when (keyCode) {
                    KeyEvent.KEYCODE_DPAD_CENTER,
                    KeyEvent.KEYCODE_ENTER,
                        -> {
                        navigateToTab(destinationId, tabIndex)
                        true
                    }

                    else -> false
                }
            } else false
        }
    }

    private fun navigateToTab(destinationId: Int, tabIndex: Int) {
        navController.navigate(destinationId)
        updateTabSelectionManual(tabIndex)
    }

    private fun updateTabSelectionManual(selectedIndex: Int) {
        if (currentTab == selectedIndex) return

        // Deselect previous tab
        tabs[currentTab].isSelected = false

        // Select new tab
        currentTab = selectedIndex
        tabs[currentTab].isSelected = true
    }

    private fun updateTabSelection(destinationId: Int) {
        val tabIndex = when (destinationId) {
            R.id.forYouFragment -> 0
            R.id.moviesFragment -> 1
            R.id.liveFragment -> 2
            R.id.tvShowsFragment -> 3
            R.id.subscriptionFragment -> 4
            else -> return
        }
        updateTabSelectionManual(tabIndex)
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        // Intercept D-pad UP to navigate to tab bar when visible
        if (event.action == KeyEvent.ACTION_DOWN &&
            event.keyCode == KeyEvent.KEYCODE_DPAD_UP && isTabBarVisible
        ) {
            val currentFocusView = currentFocus
            // Check if focus is NOT already on tab bar
            if (::tabs.isInitialized) {
                val isOnTabBar = tabs.any { it == currentFocusView }
                if (!isOnTabBar) {
                    // Redirect focus to current selected tab
                    tabs[currentTab].requestFocus()
                    tabBarViewModel.requestScrollToTop()
                    return true
                }
            }
        }
        return super.dispatchKeyEvent(event)
    }

    private fun chekInternetConnection() {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        // Create an instance of NetworkManager
        networkManager = NetworkManager(
            connectivityManager = connectivityManager,
        )

        // Observe the LiveData for network status updates
        networkManager.isConnected.observe(this, Observer { isConnected ->
            if (isConnected) {
                // Device is connected to the internet
                Log.d("MAin", "Device is connected to the internet.")
            } else {
                // Device is not connected to Wi-Fi or has no internet access
                Log.d("MAin", "Not Connected")
            }
        })

        // Check network status when the activity is created
        networkManager.checkNetworkStatus()
    }
}
