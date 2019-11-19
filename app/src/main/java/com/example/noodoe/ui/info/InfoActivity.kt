package com.example.noodoe.ui.info

import android.app.Activity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.noodoe.R
import com.example.noodoe.data.model.LoggedInUser
import com.example.noodoe.data.model.UpdateTimeAt
import com.example.noodoe.service.MyAPIService
import com.example.noodoe.ui.login.afterTextChanged
import io.reactivex.disposables.CompositeDisposable

class InfoActivity : AppCompatActivity() {
    private lateinit var retrofitManager: MyAPIService.RetrofitManager
    private lateinit var infoViewModel: InfoViewModel
    private lateinit var compositeDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_info)

        infoViewModel = ViewModelProviders.of(this)
            .get(InfoViewModel::class.java)

        val timezoneUpdate = findViewById<Button>(R.id.timezone_modify)
        val timezoneModifyTo = findViewById<EditText>(R.id.timezone_modify_to)
        compositeDisposable = CompositeDisposable()
        infoViewModel.compositeDisposable = compositeDisposable

        infoViewModel.infoFormState.observe(this@InfoActivity, Observer {
            val infoTimezoneState = it ?: return@Observer

            timezoneUpdate.isEnabled = infoTimezoneState.isDataValid

            if (infoTimezoneState.timezoneError != null) {
                timezoneModifyTo.error = getString(infoTimezoneState.timezoneError)
            }
        })

        infoViewModel.updateResultState.observe(this@InfoActivity, Observer {
            val updateResultState = it ?: return@Observer

            if (updateResultState.error != null) {
                showUpdateFailed(updateResultState.error)
            }
            if (updateResultState.success != null) {
                timezoneUpdate(updateResultState.success, updateResultState.timezone)
            }
            setResult(Activity.RESULT_OK)
        })

        timezoneModifyTo.apply {
            afterTextChanged {
                infoViewModel.timezoneChanged(timezoneModifyTo.text.toString().toInt())
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        infoViewModel.timezoneChanged(timezoneModifyTo.text.toString().toInt())
                }
                false
            }

            timezoneUpdate.setOnClickListener {
                val timezone :String = timezoneModifyTo.text.toString()
                if (timezone.isDigitsOnly()) {
                    infoViewModel.timezoneModify(timezone.toInt())
                    timezoneModifyTo.text.clear()
                }
            }
        }

        retrofitManager = MyAPIService.RetrofitManager.instance
        // TODO Check loggedInUser is exist
        val loggedInUser = retrofitManager.loggedInUser
        setUserInfo(loggedInUser)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    private fun setUserInfo(loggedInUser: LoggedInUser) {
        findViewById<TextView>(R.id.username).text = loggedInUser.username
        findViewById<TextView>(R.id.createdAt).text = loggedInUser.createdAt
        findViewById<TextView>(R.id.updatedAt).text = loggedInUser.updatedAt
        findViewById<TextView>(R.id.timezone).text = loggedInUser.timezone.toString()
    }

    private fun timezoneUpdate(loggedInUser: UpdateTimeAt, timezone:Int) {
        findViewById<TextView>(R.id.updatedAt).text = loggedInUser.updatedAt
        findViewById<TextView>(R.id.timezone).text = timezone.toString()
        infoViewModel.loggedInfoUpdate(loggedInUser.updatedAt, timezone)
    }

    private fun showUpdateFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}
