package jp.co.saint.bindingsample.handlers

import android.view.View
import android.widget.Toast

import jp.co.saint.bindingsample.MainActivity
import jp.co.saint.bindingsample.R
import jp.co.saint.bindingsample.models.BindableUser

/**
 * Created by Seiya on 2016/03/27.
 */
class MainActivityHandlers {

    fun onClickDataContent(view: View) {
        val activity = view.context as MainActivity
        val userInfo: BindableUser = activity?.getUser()
        val message: String = "Hello " + userInfo?.name + ", " + userInfo?.age + " years old"
        Toast.makeText(view.context, message, Toast.LENGTH_LONG).show()
    }

    fun onClickPlus(view: View) {
        val activity = view.context as MainActivity
        activity?.increaseAge()
    }

    fun onClickMinus(view: View) {
        val activity = view.context as MainActivity
        activity?.decreaseAge()
    }
}