package com.example.noodoe.ui.info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.noodoe.R

import com.example.noodoe.data.model.TimeZone
import com.example.noodoe.data.model.UpdateTimeAt
import com.example.noodoe.service.MyAPIService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class InfoViewModel : ViewModel() {

    private val _infoForm = MutableLiveData<InfoTimezoneState>()
    val infoFormState: LiveData<InfoTimezoneState> = _infoForm

    private val _updateResult = MutableLiveData<InfoUpdateResult>()
    val updateResultState: LiveData<InfoUpdateResult> = _updateResult

    private val retrofitManager = MyAPIService.RetrofitManager.instance

    var compositeDisposable: CompositeDisposable? = null

    fun timezoneModify(timezone: Int) {
        val loggedInUser = retrofitManager.loggedInUser

        if (timezone != retrofitManager.loggedInUser.timezone) {
            val observable: Observable<UpdateTimeAt> = retrofitManager.api.modifyTimeZone(
                loggedInUser.objectId, loggedInUser.sessionToken, TimeZone(timezone))
            compositeDisposable!!.add(
                observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ updateTimeAt ->
                        _updateResult.value =
                            InfoUpdateResult(success = updateTimeAt, timezone = timezone)
                    }, { error ->
                        _updateResult.value =
                            InfoUpdateResult(error = R.string.update_failed, timezone = timezone)
                    })
            )
        }

    }

    fun loggedInfoUpdate(updateAt: String, timezone: Int) {
        val loggedInUser = retrofitManager.loggedInUser
        loggedInUser.updatedAt = updateAt
        loggedInUser.timezone = timezone

    }

    fun timezoneChanged(timezone: Int) {
        if (!timezoneValid(timezone)) {
            _infoForm.value = InfoTimezoneState(timezoneError = R.string.invalid_timezone)
        } else {
            _infoForm.value = InfoTimezoneState(isDataValid = true)
        }
    }

    private fun timezoneValid(timezone: Int) :Boolean {
        return timezone <= 14 && timezone >= -12
    }
}
