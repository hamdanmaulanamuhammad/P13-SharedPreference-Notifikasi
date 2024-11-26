package com.example.p13_sharedpreference

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.p13_sharedpreference.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var prefManager: PrefManager

    //P14
    private val channelId = "TEST_NOTIF"
    private val notifId = 90



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        prefManager = PrefManager.getInstance(this)
        checkLoginStatus()
        val notifManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        with(binding){
            btnLogin.setOnClickListener{
                val usernameUntukLogin = "admin"
                val passwordUntukLogin = "12345"
                val username = edtUsername.text.toString()
                val password = edtPassword.text.toString()
                if (username.isEmpty() || password.isEmpty()){
                    Toast.makeText(this@MainActivity, "Username dan Password Harus diisi!", Toast.LENGTH_SHORT).show()
                }else if (username.equals(usernameUntukLogin)&& password.equals(passwordUntukLogin)){
                    prefManager.saveUsername(username)
                    checkLoginStatus()
                }else{
                    Toast.makeText(this@MainActivity, "Username atau Password Salah!", Toast.LENGTH_SHORT).show()
                }
            }
            btnLogout.setOnClickListener{
                prefManager.saveUsername("")
                checkLoginStatus()
            }
            btnClear.setOnClickListener{
                prefManager.clear()
                checkLoginStatus()
            }
            //P14
            btnNotif.setOnClickListener {
                val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PendingIntent.FLAG_IMMUTABLE
                } else {
                    0
                }

                // Intent buka MainActivity
                val intent = Intent(this@MainActivity, MainActivity::class.java)
                val pendingIntent = PendingIntent.getActivity(this@MainActivity, 0, intent, flag)

                // Intent logout
                val logoutIntent = Intent(this@MainActivity, NotifReceiver::class.java).apply {
                    action = "ACTION_LOGOUT"  // Aksi untuk logout
                }
                val logoutPendingIntent = PendingIntent.getBroadcast(this@MainActivity, 1, logoutIntent, flag)

                // Tombol logout di notif
                val builder = NotificationCompat.Builder(this@MainActivity, channelId)
                    .setSmallIcon(R.drawable.baseline_notifications_24)
                    .setContentTitle("Notifku")
                    .setContentText("Hello World!")
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)  // Intent untuk buka aplikasi saat notifikasi diklik
                    .addAction(
                        R.drawable.baseline_exit_to_app_24,
                        "Logout",
                        logoutPendingIntent  // PendingIntent logout
                    )

                // show notif
                val notifManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val notifChannel = NotificationChannel(
                        channelId,
                        "Notifku",
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                    notifManager.createNotificationChannel(notifChannel)
                }
                notifManager.notify(notifId, builder.build())
            }

            btnUpdate.setOnClickListener {
                val notifImage = BitmapFactory.decodeResource(resources,
                    R.drawable.buku)
                val builder = NotificationCompat.Builder(this@MainActivity, channelId)
                    .setSmallIcon(R.drawable.baseline_notifications_24)
                    .setContentTitle("Notifku")
                    .setContentText("Ini update notifikasi")
                    .setStyle(
                        NotificationCompat.BigPictureStyle()
                            .bigPicture(notifImage)
                    )
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                notifManager.notify(notifId, builder.build())
           }
        }
    }
    fun checkLoginStatus(){
        val isLoggedIn = prefManager.getUsername()
        if (isLoggedIn.isEmpty()){
            binding.llLogin.visibility = View.VISIBLE
            binding.llLogged.visibility = View.VISIBLE
        }else{
            binding.llLogin.visibility = View.GONE
            binding.llLogged.visibility = View.VISIBLE
        }
    }
}