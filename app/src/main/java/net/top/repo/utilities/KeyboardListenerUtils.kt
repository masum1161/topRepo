package net.top.repo.utilities

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import java.util.*


/**
 * Based on the following Stackoverflow answer:
 * http://stackoverflow.com/questions/2150078/how-to-check-visibility-of-software-keyboard-in-android
 */
class KeyboardListenerUtils private constructor(
    act: AppCompatActivity,
    listener: SoftKeyboardToggleListener,
) :
    OnGlobalLayoutListener {
    private var mCallback: SoftKeyboardToggleListener?
    private val mRootView: View
    private var prevValue: Boolean? = null
    private val mScreenDensity: Float

    interface SoftKeyboardToggleListener {
        fun onToggleSoftKeyboard(isVisible: Boolean)
    }

    override fun onGlobalLayout() {
        val r = Rect()
        mRootView.getWindowVisibleDisplayFrame(r)
        val heightDiff = mRootView.rootView.height - (r.bottom - r.top)
        val dp = heightDiff / mScreenDensity
        val isVisible: Boolean = dp > MAGIC_NUMBER
        if (mCallback != null && (prevValue == null || isVisible != prevValue)) {
            prevValue = isVisible
            mCallback!!.onToggleSoftKeyboard(isVisible)
        }
    }

    private fun removeListener() {
        mCallback = null
        mRootView.viewTreeObserver.removeOnGlobalLayoutListener(this)
    }

    companion object {
        private const val MAGIC_NUMBER = 200
        private val sListenerMap: HashMap<SoftKeyboardToggleListener, KeyboardListenerUtils> =
            HashMap<SoftKeyboardToggleListener, KeyboardListenerUtils>()

        /**
         * Add a new keyboard listener
         * @param act calling activity
         * @param listener callback
         */
        fun addKeyboardToggleListener(
            act: AppCompatActivity?,
            listener: SoftKeyboardToggleListener?,
        ) {
            removeKeyboardToggleListener(listener)
            listener?.let {
                act?.let { it1 -> KeyboardListenerUtils(it1, listener) }?.let { it2 ->
                    sListenerMap.put(it,
                        it2)
                }
            }
        }

        /**
         * Remove a registered listener
         * @param listener [SoftKeyboardToggleListener]
         */
        fun removeKeyboardToggleListener(listener: SoftKeyboardToggleListener?) {
            if (sListenerMap.containsKey(listener)) {
                val k: KeyboardListenerUtils? = sListenerMap.get(listener)
                k?.removeListener()
                sListenerMap.remove(listener)
            }
        }

        /**
         * Remove all registered keyboard listeners
         */
        fun removeAllKeyboardToggleListeners() {
            for (l in sListenerMap.keys) sListenerMap.get(l)?.removeListener()
            sListenerMap.clear()
        }

        /**
         * Manually toggle soft keyboard visibility
         * @param context calling context
         */
        fun toggleKeyboardVisibility(context: Context) {
            val inputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }

        /**
         * Force closes the soft keyboard
         * @param activeView the view with the keyboard focus
         */
        fun forceCloseKeyboard(activeView: View) {
            val inputMethodManager =
                activeView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(activeView.windowToken, 0)
        }
    }

    init {
        mCallback = listener
        mRootView = (act.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0)
        mRootView.viewTreeObserver.addOnGlobalLayoutListener(this)
        mScreenDensity = act.resources.displayMetrics.density
    }
}
