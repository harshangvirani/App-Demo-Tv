package com.example.livestreamingtv.ui.activity

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.livestreamingtv.R
import com.example.livestreamingtv.utils.fragmentsWithTabBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    
    private lateinit var navController: NavController
    private lateinit var tabs: List<TextView>
    private var currentTab = 0
    private var isTabBarVisible = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
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
        
        tabs = listOf(
            topBarView.findViewById(R.id.tab_for_you),
            topBarView.findViewById(R.id.tab_movies),
            topBarView.findViewById(R.id.tab_live),
            topBarView.findViewById(R.id.tab_tv_shows),
            topBarView.findViewById(R.id.tab_subscriptions),
        )
        
        // Set For You tab as selected by default
        tabs[0].isSelected = true
        
        // Set up tab listeners for both touch and TV remote
        setupTabListener(tabs[0], R.id.forYouFragment, 0)
        setupTabListener(tabs[1], R.id.moviesFragment, 1)
        setupTabListener(tabs[2], R.id.liveFragment, 2)
        setupTabListener(tabs[3], R.id.tvShowsFragment, 3)
        setupTabListener(tabs[4], R.id.subscriptionFragment, 4)
        
        // Request focus on first tab for TV remote navigation
        tabs[0].post {
            tabs[0].requestFocus()
        }
    }
    
    private fun setupTabListener(tab: TextView, destinationId: Int, tabIndex: Int) {
        // For touch/mouse
        tab.setOnClickListener {
            navigateToTab(destinationId, tabIndex)
        }
        
        // For TV remote D-pad CENTER button
        tab.setOnKeyListener { _, keyCode, event ->
            if (event.action == android.view.KeyEvent.ACTION_DOWN) {
                when (keyCode) {
                    android.view.KeyEvent.KEYCODE_DPAD_CENTER,
                    android.view.KeyEvent.KEYCODE_ENTER -> {
                        navigateToTab(destinationId, tabIndex)
                        true
                    }
                    else -> false
                }
            } else {
                false
            }
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
            event.keyCode == KeyEvent.KEYCODE_DPAD_UP && isTabBarVisible) {
            val currentFocusView = currentFocus
            // Check if focus is NOT already on tab bar
            if (::tabs.isInitialized) {
                val isOnTabBar = tabs.any { it == currentFocusView }
                if (!isOnTabBar) {
                    // Redirect focus to current selected tab
                    tabs[currentTab].requestFocus()
                    return true
                }
            }
        }
        return super.dispatchKeyEvent(event)
    }
}
