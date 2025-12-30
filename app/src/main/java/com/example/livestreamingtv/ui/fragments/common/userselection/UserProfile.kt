package com.example.livestreamingtv.ui.fragments.common.userselection


data class UserProfile(
    val id: String,
    val name: String,
    val imageUrl: String? = null,
    val isAddNew: Boolean = false,
)
