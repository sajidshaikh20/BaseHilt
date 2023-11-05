package com.base.hilt

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.base.hilt.base.LocaleManager
import com.base.hilt.base.ToolbarModel
import com.base.hilt.databinding.ActivityMainBinding
import com.base.hilt.utils.DebugLog
import com.base.hilt.utils.MyPreference
import com.base.hilt.utils.PrefKey
import com.base.hilt.utils.Utils
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var dialog: Dialog? = null
    private lateinit var binding: ActivityMainBinding


    @Inject
    lateinit var localeManager: LocaleManager

    @Inject
    lateinit var mPref: MyPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DebugLog.e("onCreate")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController




        navView.setupWithNavController(navController)

        binding.layToolbar.sivback.setOnClickListener {
            navController.popBackStack()
            binding.layToolbar.sivback.visibility = View.GONE
        }
        binding.layToolbar.sivEdit.setOnClickListener {
            findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.refineFragment)
        }


    }

    /**
     * Show Progress dialog
     * @param t: true show progress bar
     *
     **/
    fun displayProgress(t: Boolean) {
        // binding.loading = t
        if (t) {
            if (dialog == null) {
                dialog = Utils.progressDialog(this)
            }
            dialog?.show()
        } else {
            dialog?.dismiss()
        }
    }

    /**
     * This method is used to hide the keyboard.
     */
    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    /**
     * Toolbar manages items and visibility according to
     */
    fun toolBarManagement(toolbarModel: ToolbarModel?) {
        if (toolbarModel != null) {
            when {
                toolbarModel.isVisible -> {
                    binding.layToolbar.tvTitle.text = toolbarModel.title

                    binding.layToolbar.cvtoolbar.visibility = View.VISIBLE

                    binding.layToolbar.sivlogo.visibility = View.VISIBLE
                    binding.layToolbar.tvName.visibility = View.VISIBLE
                    binding.layToolbar.tvaddress.visibility = View.VISIBLE
                    binding.layToolbar.sivEdit.visibility = View.VISIBLE

                    if (toolbarModel.backbtn) {
                        binding.layToolbar.sivback.visibility = View.VISIBLE

                        binding.layToolbar.sivlogo.visibility = View.GONE
                        binding.layToolbar.tvName.visibility = View.GONE
                        binding.layToolbar.tvaddress.visibility = View.GONE
                        binding.layToolbar.sivEdit.visibility = View.GONE

                    }

                }

                else -> {
                    binding.layToolbar.cvtoolbar.visibility = View.GONE
                }
            }
            if (toolbarModel.isBottomNavVisible)
                binding.navView.visibility = View.VISIBLE
            else
                binding.navView.visibility = View.GONE
        }
    }


    /**
     * Override method for fragments attach
     */
    override fun attachBaseContext(base: Context) {
        DebugLog.e("attachBaseContext")
        // super.attachBaseContext(base);
        val languageCode = MyApp.applicationContext().mPref.getValueString(
            PrefKey.SELECTED_LANGUAGE,
            PrefKey.EN_CODE
        )
        useCustomConfig(base, languageCode)?.let { super.attachBaseContext(it) }
    }


    /**
     * Method called when language changes done
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        localeManager.setLocale(this)
    }

    /**
     * After applied locale changes override method called
     */
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val languageCode = mPref.getValueString(PrefKey.SELECTED_LANGUAGE, PrefKey.EN_CODE)
        window.decorView.layoutDirection =
            if (languageCode == PrefKey.AR_CODE) View.LAYOUT_DIRECTION_RTL else View.LAYOUT_DIRECTION_LTR
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun useCustomConfig(context: Context, languageCode: String?): Context? {
        Locale.setDefault(Locale(languageCode!!))
        return if (Build.VERSION.SDK_INT >= 17) {
            val config = Configuration()
            config.setLocale(Locale(languageCode))
            context.createConfigurationContext(config)
        } else {
            val res: Resources = context.resources
            val config =
                Configuration(res.configuration)
            config.locale = Locale(languageCode)
            res.updateConfiguration(config, res.displayMetrics)
            context
        }
    }


    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        if (overrideConfiguration != null) {
            val uiMode = overrideConfiguration.uiMode
            overrideConfiguration.setTo(baseContext.resources.configuration)
            overrideConfiguration.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }

}