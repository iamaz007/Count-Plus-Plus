package com.project.countplusplus.dashboard

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RemoteViews
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.project.countplusplus.R
import com.project.countplusplus.adapters.DashboardSwipeAdapter
import com.project.countplusplus.adapters.PaymentRecyclerAdapter
import com.project.countplusplus.auth.LoginActivity
import com.project.countplusplus.chat.RecentChats
import com.project.countplusplus.database.PaymentDatabaseHandler
import com.project.countplusplus.database.UsersDatabaseHandler
import com.project.countplusplus.models.DashboardSwipeModel
import com.project.countplusplus.models.PaymentModel
import com.project.countplusplus.payment.AddPaymentActivity
import com.project.countplusplus.payment.PaymentActivity
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_payment.*

class DashboardActivity : AppCompatActivity() {
//    firebase variables
    var myEmail=""
    lateinit var auth: FirebaseAuth
    var databaseReference :  DatabaseReference? = null
    var database: FirebaseDatabase? = null
//    dashboard variables
    private lateinit var adapter: DashboardSwipeAdapter
    private lateinit var models: ArrayList<DashboardSwipeModel>
    private lateinit var viewPager: ViewPager
    var sliderDotspanel: LinearLayout? = null
    private var dotscount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        auth = FirebaseAuth.getInstance()


        loadProfile()
        dashboard()
        dashboardMenu()
        notifyForPayments()
    }

    private fun dashboardMenu() {
        paymentCard.setOnClickListener {
            startActivity(Intent(this@DashboardActivity, PaymentActivity::class.java))
        }

        chatCard.setOnClickListener {
            startActivity(Intent(this@DashboardActivity, RecentChats::class.java))
        }
    }

    private fun loadProfile() {
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("users")
        val user = auth.currentUser
        val userreference = databaseReference?.child(user?.uid!!)

        logoutView.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this@DashboardActivity, LoginActivity::class.java))
            finish()
        }
    }

    fun dashboard()
    {
        viewPager = findViewById(R.id.view_pager)
        sliderDotspanel = findViewById(R.id.slider_dots)

        models = ArrayList()
        models.add(DashboardSwipeModel("Total Money Given this Month", "Rs. 0"))
        models.add(DashboardSwipeModel("Total Payments Received this Month", "Rs. 0"))
        models.add(DashboardSwipeModel("Total Agreements", "0"))


        adapter = DashboardSwipeAdapter(models, this@DashboardActivity)
        viewPager.adapter = adapter



        viewPager.setPadding(30, 0, 30, 0)
        dotscount = adapter.count

        val dots = arrayOfNulls<ImageView>(dotscount)

        for (i in 0 until dotscount) {
            dots[i] = ImageView(this)
            dots[i]!!.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.non_active_dot))
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.setMargins(8, 0, 8, 0)
            sliderDotspanel!!.addView(dots[i], params)
        }
        dots[0]?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.active_dot))

        viewPager.setOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int){}

            override fun onPageSelected(position: Int) {
                for (i in 0 until dotscount) {
                    dots[i]?.setImageDrawable(ContextCompat.getDrawable(this@DashboardActivity,R.drawable.non_active_dot))
                }
                dots[position]?.setImageDrawable(ContextCompat.getDrawable(this@DashboardActivity,R.drawable.active_dot))
            }
        })

        viewPager.setCurrentItem(1)
    }

    private fun notifyForPayments(): ArrayList<PaymentModel> {
        val user = auth.currentUser
        if (user != null) {
            myEmail = user.email.toString()
        }
        val userDatabaseHandler: UsersDatabaseHandler = UsersDatabaseHandler(this)
        val myId = userDatabaseHandler.getUserId(myEmail)
        //creating the instance of DatabaseHandler class
        val databaseHandler: PaymentDatabaseHandler = PaymentDatabaseHandler(this)
        //calling the viewEmployee method of DatabaseHandler class to read the records
        val empList: ArrayList<PaymentModel> = databaseHandler.notifyForPayments(myId)

        val intent = Intent(this,DashboardActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notificationManager : NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        var notificationChannel : NotificationChannel
        var builder : Notification.Builder
        var channelId = "com.project.countplusplus"
        var desc = "You will receive payment from ${empList[0].sender_name} today"
        val contentView = RemoteViews(packageName,R.layout.notification_layout)
        contentView.setTextViewText(R.id.tv_title,"Payment Notification")
        contentView.setTextViewText(R.id.tv_content,desc)


        if (empList.size > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                notificationChannel = NotificationChannel(channelId,desc,NotificationManager.IMPORTANCE_HIGH)
                notificationManager.createNotificationChannel(notificationChannel)

                builder = Notification.Builder(this,channelId)
                        .setContent(contentView)
                        .setSmallIcon(R.drawable.dollar)
                        .setContentIntent(pendingIntent)
                notificationManager.notify(1235,builder.build())
            }
            Log.d("notify","yes")
        }
        else
        {
            Log.d("notify","no")
        }
        return empList;
    }
}