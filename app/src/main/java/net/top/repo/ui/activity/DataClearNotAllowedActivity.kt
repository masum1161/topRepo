package net.top.repo.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import net.top.repo.R


class DataClearNotAllowedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_clear_not_allowed)
    }

    fun close(view: View) {
        finish()
    }
}