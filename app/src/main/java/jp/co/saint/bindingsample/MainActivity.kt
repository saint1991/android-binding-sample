package jp.co.saint.bindingsample

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import jp.co.saint.bindingsample.databinding.ActivityMainBinding
import jp.co.saint.bindingsample.handlers.MainActivityHandlers
import jp.co.saint.bindingsample.models.BindableUser
import java.util.*

class MainActivity : AppCompatActivity() {

    private var user: BindableUser = BindableUser("Seiya", "Mizuno", 24)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.setUser(user)
        binding.setMyHandler(MainActivityHandlers())
    }

    fun getUser() = user

    fun increaseAge() = user.increaseAge()
    fun decreaseAge() = user.decreaseAge()
}
