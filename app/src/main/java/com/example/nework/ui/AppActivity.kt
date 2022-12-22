package com.example.nework.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.nework.R
import com.example.nework.auth.AppAuth
import com.example.nework.databinding.ActivityAppBinding
import com.example.nework.databinding.ActivityAppBinding.inflate
import com.example.nework.viewmodel.AuthViewModel
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class AppActivity : AppCompatActivity() {
    private val viewModel: AuthViewModel by viewModels()

    @Inject
    lateinit var appAuth: AppAuth

    @Inject
    lateinit var googleApiAvailability: GoogleApiAvailability

    companion object {
        private const val MAPKIT_API_KEY = "4020182c-79bf-4ab9-b30b-08aaddd23117"
    }

    lateinit var binding: ActivityAppBinding

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey(MAPKIT_API_KEY)
        binding = inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigation = binding.bottomNavigation

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment

        val navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.postFeedFragment, R.id.eventFeedFragment, R.id.contactsFragment, R.id.userProfileFragment -> bottomNavigation.visibility =
                    View.VISIBLE
                else -> bottomNavigation.visibility = View.GONE
            }
        }
        val appBar = AppBarConfiguration(
            setOf(
                R.id.postFeedFragment, R.id.eventFeedFragment, R.id.contactsFragment, R.id.userProfileFragment
            )
        )
        setupActionBarWithNavController(navController, appBar)
        bottomNavigation.setupWithNavController(navController)


        viewModel.data.observe(this) {
            invalidateOptionsMenu()
        }
        checkGoogleApiAvailability()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_authorization, menu)

        menu.let {
            it.setGroupVisible(R.id.unauthenticated, !viewModel.authenticated)
            it.setGroupVisible(R.id.authenticated, viewModel.authenticated)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.signin -> {
                findNavController(R.id.navHostFragment).navigate(R.id.signInFragment)
                true
            }
            R.id.signup -> {
                findNavController(R.id.navHostFragment).navigate(R.id.registerFragment)
                true
            }
            R.id.signout -> {
                appAuth.removeAuth()
                findNavController(R.id.navHostFragment).navigateUp()
                true
            }
            else -> false
        }
    }

    private fun checkGoogleApiAvailability() {
        with(googleApiAvailability) {
            val code = isGooglePlayServicesAvailable(this@AppActivity)
            if (code == ConnectionResult.SUCCESS) {
                return@with
            }
            if (isUserResolvableError(code)) {
                getErrorDialog(this@AppActivity, code, 9000)?.show()
                return
            }
            Toast.makeText(this@AppActivity, R.string.google_api_unavailable_message, Toast.LENGTH_LONG)
                .show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.navHostFragment)
        return navController.navigateUp()
    }
}