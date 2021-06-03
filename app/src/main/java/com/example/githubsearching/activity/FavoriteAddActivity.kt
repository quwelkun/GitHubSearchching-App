package com.example.githubsearching.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubsearching.R
import com.example.githubsearching.UserSource
import com.example.githubsearching.database.Favorite
import com.example.githubsearching.databinding.ActivityFavoriteAddBinding
import com.example.githubsearching.helper.ViewModelFactory
import com.example.githubsearching.insert.AddFavViewModel

class FavoriteAddActivity : AppCompatActivity() {
    companion object {
        const val USER_FAV_SOURCE = "user_fav_source"
        const val EXTRA_NOTE = "extra_note"
        const val EXTRA_POSITION = "extra_position"
        const val REQUEST_ADD = 100
        const val RESULT_ADD = 101
        const val REQUEST_UPDATE = 200
        const val RESULT_UPDATE = 201
        const val RESULT_DELETE = 301
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20
    }
    private var isEdit = false
    private var fav: Favorite? = null
    private var position = 0

    private lateinit var addFavViewModel: AddFavViewModel

    private var _activityFavAddBinding: ActivityFavoriteAddBinding? = null
    private val binding get() = _activityFavAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityFavAddBinding = ActivityFavoriteAddBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        addFavViewModel = obtainViewModel(this@FavoriteAddActivity)

        //get parcel from intent
        val sourceFav = intent.getParcelableExtra<UserSource>(USER_FAV_SOURCE) as UserSource
        binding?.tvItemTypeFav?.text = sourceFav.name
        binding?.tvItemTypeFav?.text = sourceFav.type
        Glide.with(this)
                .load(sourceFav.avatar)
                .apply(RequestOptions().override(350,550))
                .into(binding?.profileImageFav!!)

        fav = intent.getParcelableExtra(EXTRA_NOTE)
        if(fav != null) {
            position = intent.getIntExtra(EXTRA_POSITION, 0)
        } else {
            fav = Favorite()
        }

        val actionBarTitle: String
        val btnTitle: String

        if(isEdit) {
            actionBarTitle = getString(R.string.change)
            btnTitle = getString(R.string.update)
            if(fav != null) {
                fav?.let { fav ->
                    binding?.tvItemTitleFav?.setText(fav.login)
                    binding?.tvItemTypeFav?.setText(fav.type)
                }
            }
        } else {
            actionBarTitle = getString(R.string.add)
            btnTitle = getString(R.string.save)
        }

        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding?.btnSubmit?.text = btnTitle

        binding?.btnSubmit?.setOnClickListener {
            fav.let { fav ->
                fav?.login = sourceFav.name
                fav?.type = sourceFav.type
                fav?.avatar = sourceFav.avatar
            }
            val intent = Intent().apply {
                putExtra(EXTRA_NOTE, fav)
                putExtra(EXTRA_POSITION, position)
            }
            addFavViewModel.insert(fav as Favorite)
            setResult(RESULT_ADD, intent)
            Toast.makeText(this, "Berhasil di tambah", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@FavoriteAddActivity, DisplayFavoriteActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (isEdit) {
            menuInflater.inflate(R.menu.menu_form, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> showAlertDialog(ALERT_DIALOG_DELETE)
            android.R.id.home -> showAlertDialog(ALERT_DIALOG_CLOSE)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE)
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityFavAddBinding = null
    }

    private fun obtainViewModel(activity: AppCompatActivity): AddFavViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(AddFavViewModel::class.java)
    }

    private fun showAlertDialog(type: Int) {
        val isDialogClose = type == ALERT_DIALOG_CLOSE
        val dialogTitle: String
        val dialogMessage: String
        if (isDialogClose) {
            dialogTitle = getString(R.string.cancel)
            dialogMessage = getString(R.string.message_cancel)
        } else {
            dialogMessage = getString(R.string.message_delete)
            dialogTitle = getString(R.string.delete)
        }
        val alertDialogBuilder = AlertDialog.Builder(this)
        with(alertDialogBuilder) {
            setTitle(dialogTitle)
            setMessage(dialogMessage)
            setCancelable(false)
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                if (!isDialogClose) {
                    addFavViewModel.delete(fav as Favorite)
                    val intent = Intent()
                    intent.putExtra(EXTRA_POSITION, position)
                    setResult(RESULT_DELETE, intent)
                }
                finish()
            }
            setNegativeButton(getString(R.string.no)) { dialog, _ -> dialog.cancel() }
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}