package jp.co.saint.bindingsample.models

import android.databinding.BaseObservable
import android.databinding.Bindable
import jp.co.saint.bindingsample.BR

/**
 * Created by Seiya on 2016/03/27.
 */
class BindableUser(firstName: String, lastName: String, age: Int): BaseObservable() {

    private var _name: String = firstName + " " + lastName
    var name: String
    @Bindable get() = _name
    set (value) {
        _name = value
        notifyPropertyChanged(BR.name)
    }

    private var _age: Int = age
    var age: String
    @Bindable get() = _age.toString()
    set (value) {
        this._age = Integer.parseInt(value)
        notifyPropertyChanged(BR.age)
    }

    fun increaseAge() {
        age = (_age + 1).toString()
    }
    fun decreaseAge() {
        age = (_age - 1).toString()
    }

    // Primary Constructor
    init {
        this.name = firstName + " " + lastName
        this.age = age.toString()
    }
}