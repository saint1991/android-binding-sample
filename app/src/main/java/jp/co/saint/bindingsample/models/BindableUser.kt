package jp.co.saint.bindingsample.models

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.ObservableField
import android.databinding.ObservableInt
import jp.co.saint.bindingsample.BR

/**
 * Created by Seiya on 2016/03/27.
 */
class BindableUser(firstName: String, lastName: String, age: Int): BaseObservable() {

    private var name: String = firstName + " " + lastName
    set (value) {
        name = value
        notifyPropertyChanged(BR.name)
    }
    @Bindable
    fun getName(): String = name

    private var age: Int = age
    set (value) {
        age = value
        notifyPropertyChanged(BR.age)
    }
    @Bindable
    fun getAge() = age

    // Primary Constructor
    init {
        this.name = firstName + " " + lastName
        this.age = age
    }
}