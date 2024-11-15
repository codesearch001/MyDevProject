package com.snofed.publicapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

object ClientPrefrences {

    private const val PREFERENCE_NAME = "app-preference"
    private const val CLIENT_IDS_KEY = "client_ids_key"

    private fun getEditor(context: Context): SharedPreferences.Editor {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
    }

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    // Save a client ID
    @SuppressLint("MutatingSharedPrefs")
    fun saveClientId(context: Context, clientId: String) {
        val sharedPreferences = getSharedPreferences(context)
        val clientIds = sharedPreferences.getStringSet(CLIENT_IDS_KEY, mutableSetOf()) ?: mutableSetOf()
        clientIds.add(clientId)
        getEditor(context).putStringSet(CLIENT_IDS_KEY, clientIds).apply()
    }

    // Remove a client ID
    @SuppressLint("MutatingSharedPrefs")
    fun removeClientId(context: Context, clientId: String) {
        val sharedPreferences = getSharedPreferences(context)
        val clientIds = sharedPreferences.getStringSet(CLIENT_IDS_KEY, mutableSetOf()) ?: mutableSetOf()
        clientIds.remove(clientId)
        getEditor(context).putStringSet(CLIENT_IDS_KEY, clientIds).apply()
    }

    // Get all client IDs
    fun getClientIds(context: Context): Set<String> {
        return getSharedPreferences(context).getStringSet(CLIENT_IDS_KEY, mutableSetOf()) ?: emptySet()
    }

    // Clear all client IDs
    fun clearClientIds(context: Context) {
        getEditor(context).remove(CLIENT_IDS_KEY).apply()
    }

    // Check if a client ID is present
    fun containsClientId(context: Context, clientId: String): Boolean {
        val clientIds = getClientIds(context)
        return clientIds.contains(clientId)
    }

    fun saveSwitchState(context: Context, key: String, isChecked: Boolean) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("switch_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, isChecked)
        editor.apply()
    }

    fun getSwitchState(context: Context, key: String): Boolean {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("switch_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(key, false) // Default is false
    }
}
