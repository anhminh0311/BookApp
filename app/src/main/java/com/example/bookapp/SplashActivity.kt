package com.example.bookapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.delay

class SplashActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        firebaseAuth = FirebaseAuth.getInstance()

        Handler().postDelayed(Runnable {
            checkUser()
        }, 2500) //means around 2,5sec

    }

    private fun checkUser() {
        //get current user, if logged in or not
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null){
            //user not logged in, goto main screen
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        else{
            //user logged in
            val ref = FirebaseDatabase.getInstance().getReference("Users")
            ref.child(firebaseUser.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        //get user type
                        val userType = snapshot.child("userType").value
                        if (userType == "user"){
                            //open user dashboard
                            startActivity(Intent(this@SplashActivity, DashboardUserActivity::class.java))
                            finish()
                        }
                        else if (userType == "admin"){
                            //open admin dashboard
                            startActivity(Intent(this@SplashActivity, DashboardAdminActivity::class.java))
                            finish()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
    }
}