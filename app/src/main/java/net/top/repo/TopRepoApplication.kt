package net.top.repo

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import androidx.work.Configuration
import net.top.repo.db.ObjectBox

@HiltAndroidApp
public class TopRepoApplication : Application(),Configuration.Provider {
    companion object {
        private lateinit var instance: TopRepoApplication
        public fun getContext(): Context {
            return instance
        }
    }
    //region WorkManager
    @Inject
    lateinit var workerFactory: HiltWorkerFactory
    override fun getWorkManagerConfiguration() = Configuration.Builder()
        .setWorkerFactory(workerFactory)
        .build()
    //endregion

    override fun onCreate() {
        super.onCreate()
        instance = this;
        ObjectBox.init(this)
    }
}