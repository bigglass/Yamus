package kg.delletenebre.yamus

import android.app.Activity
import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kg.delletenebre.yamus.api.YandexUser
import kg.delletenebre.yamus.fragments.MediaItemFragment
import kg.delletenebre.yamus.ui.login.LoginActivity
import kg.delletenebre.yamus.viewmodels.MainActivityViewModel
import androidx.navigation.Navigation
import kg.delletenebre.yamus.api.YandexApi
import kg.delletenebre.yamus.utils.InjectorUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var navigationController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setContentView(R.layout.activity_main)

        volumeControlStream = AudioManager.STREAM_MUSIC

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.nav_view)
        navigationController = findNavController(R.id.fragmentContainer)
        bottomNavigationView.setupWithNavController(navigationController)

        viewModel = ViewModelProvider(this, InjectorUtils.provideMainActivityViewModel(this))
                .get(MainActivityViewModel::class.java)
//
//        viewModel.navigateToFragment.observe(this, Observer {
//            it?.getContentIfNotHandled()?.let { fragmentRequest ->
//                val transaction = supportFragmentManager.beginTransaction()
//                transaction.replace(
//                        R.id.fragmentContainer, fragmentRequest.fragment, fragmentRequest.tag)
//                if (fragmentRequest.backStack) transaction.addToBackStack(null)
//                transaction.commit()
//            }
//        })
//
//        viewModel.rootMediaId.observe(this,
//                Observer<String> { rootMediaId ->
//                    if (rootMediaId != null) {
//                        navigateToMediaItem(rootMediaId)
//                    }
//                })
//
//        viewModel.navigateToMediaItem.observe(this, Observer {
//            it?.getContentIfNotHandled()?.let { mediaId ->
//                navigateToMediaItem(mediaId)
//            }
//        })
    }

    override fun onResume() {
        super.onResume()

        if (!YandexUser.isAuth()) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivityForResult(intent, 1)
        } else {
            GlobalScope.launch {
                YandexUser.updateAccountStatus()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            if (resultCode != Activity.RESULT_OK) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivityForResult(intent, 1)
            }
        }
    }

//    private fun navigateToMediaItem(mediaId: String) {
//        var fragment: MediaItemFragment? = getBrowseFragment(mediaId)
//        if (fragment == null) {
//            fragment = MediaItemFragment.newInstance(mediaId)
//            // If this is not the top level media (root), we add it to the fragment
//            // back stack, so that actionbar toggle and Back will work appropriately:
//            viewModel.showFragment(fragment, !isRootId(mediaId), mediaId)
//        }
//    }
//
//    private fun isRootId(mediaId: String) = mediaId == viewModel.rootMediaId.value
//
//    private fun getBrowseFragment(mediaId: String): MediaItemFragment? {
//        return supportFragmentManager.findFragmentByTag(mediaId) as MediaItemFragment?
//    }
}
