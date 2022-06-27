
package net.top.repo.db

import android.content.Context
import io.objectbox.BoxStore


object ObjectBox {
    lateinit var boxStore: BoxStore

    fun init(context: Context) {
        /*boxStore = MyObjectBox.builder()
            .androidContext(context.applicationContext)
            .build()

        if (BuildConfig.DEBUG) {
            Log.d("ObjectBox", "Using ObjectBox ${BoxStore.getVersion()} (${BoxStore.getVersionNative()})")
            AndroidObjectBrowser(boxStore).start(context.applicationContext)
        }*/
    }
}