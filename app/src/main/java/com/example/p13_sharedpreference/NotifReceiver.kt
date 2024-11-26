package com.example.p13_sharedpreference

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class NotifReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // Menangani logout
        if (intent?.action == "ACTION_LOGOUT") {
            // Menghapus data login (SharedPreferences)
            val prefManager = PrefManager.getInstance(context!!)
            prefManager.saveUsername("")

            // Menampilkan Toast untuk memberi tahu bahwa logout berhasil
            Toast.makeText(context, "Logout berhasil", Toast.LENGTH_SHORT).show()

            // Mengarahkan kembali ke halaman login
            val loginIntent = Intent(context, MainActivity::class.java)
            loginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(loginIntent)
        } else {
            // Jika ada pesan dari notifikasi
            val msg = intent?.getStringExtra("MESSAGE")
            if (msg != null) {
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
            }
        }
    }
}
