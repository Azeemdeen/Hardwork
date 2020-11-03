package com.azeem.hardwork

import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.nav_header_main.*


class MainPageActivity : AppCompatActivity() {


    var mProgress: ProgressDialog? = null
    var runnable: Runnable? = null
    private val handler = Handler()

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)


        mProgress = ProgressDialog(this);
        mProgress!!.setTitle("Logging out...");
        mProgress!!.setMessage("Please wait...");
        mProgress!!.setCancelable(false);
        mProgress!!.setIndeterminate(true);

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_logout -> {
                    val mAlertDialog = AlertDialog.Builder(this)
                    mAlertDialog.setTitle("Log out")
                    mAlertDialog.setMessage("Are you sure want to logout")
                    mAlertDialog.setPositiveButton("LOG OUT") { mAlertDialog, id ->
                        val sharedPreferences: SharedPreferences = getSharedPreferences("sharedpref", Context.MODE_PRIVATE)
                        val editor: Editor = sharedPreferences.edit()
                        editor.clear()
                        editor.apply()

                        runnable = Runnable {
                            if (!isFinishing) {
                                openActivity(LoginActivity::class.java)
                                mProgress!!.dismiss()
                                finish()
                            }
                        }
                        mProgress!!.show();
                        handler.postDelayed(runnable!!, 2000)
                        mAlertDialog.dismiss()

                    }
                    mAlertDialog.setNegativeButton("Cancel") { mAlertDialog, id ->
                        mAlertDialog.dismiss()
                    }
                    mAlertDialog.show()
                }
            }
            true
        }
        this.tv_ViewUserName.setText("Elegant media")
       // tv_ViewUserName.setText("Elegant media")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_page, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}