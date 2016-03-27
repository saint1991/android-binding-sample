package jp.co.saint.bindingsample

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import jp.co.saint.bindingsample.databinding.ActivityMainBinding
import jp.co.saint.bindingsample.models.BindableUser

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.user = BindableUser("Seiya", "Mizuno", 24)
    }
}
