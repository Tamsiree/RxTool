/*
 * Copyright 2014-2017 Eduard Ereza Martínez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tamsiree.rxkit.crash

import android.app.Activity
import androidx.annotation.DrawableRes
import androidx.annotation.IntDef
import java.io.Serializable
import java.lang.reflect.Modifier

class RxCrashConfig : Serializable {
    private var backgroundMode = BACKGROUND_MODE_SHOW_CUSTOM
    private var enabled = true
    private var showErrorDetails = true
    private var showRestartButton = true
    private var logErrorOnRestart = true
    private var trackActivities = false
    private var minTimeBetweenCrashesMs = 3000
    internal var errorDrawable: Int? = null
    private var errorActivityClass: Class<out Activity?>? = null
    internal var restartActivityClass: Class<out Activity?>? = null
    private var eventListener: RxCrashTool.EventListener? = null

    @BackgroundMode
    fun getBackgroundMode(): Int {
        return backgroundMode
    }

    fun setBackgroundMode(@BackgroundMode backgroundMode: Int) {
        this.backgroundMode = backgroundMode
    }

    fun isEnabled(): Boolean {
        return enabled
    }

    fun setEnabled(enabled: Boolean) {
        this.enabled = enabled
    }

    fun isShowErrorDetails(): Boolean {
        return showErrorDetails
    }

    fun setShowErrorDetails(showErrorDetails: Boolean) {
        this.showErrorDetails = showErrorDetails
    }

    fun isShowRestartButton(): Boolean {
        return showRestartButton
    }

    fun setShowRestartButton(showRestartButton: Boolean) {
        this.showRestartButton = showRestartButton
    }

    fun isLogErrorOnRestart(): Boolean {
        return logErrorOnRestart
    }

    fun setLogErrorOnRestart(logErrorOnRestart: Boolean) {
        this.logErrorOnRestart = logErrorOnRestart
    }

    fun isTrackActivities(): Boolean {
        return trackActivities
    }

    fun setTrackActivities(trackActivities: Boolean) {
        this.trackActivities = trackActivities
    }

    fun getMinTimeBetweenCrashesMs(): Int {
        return minTimeBetweenCrashesMs
    }

    fun setMinTimeBetweenCrashesMs(minTimeBetweenCrashesMs: Int) {
        this.minTimeBetweenCrashesMs = minTimeBetweenCrashesMs
    }

    @DrawableRes
    fun getErrorDrawable(): Int? {
        return errorDrawable
    }

    fun setErrorDrawable(@DrawableRes errorDrawable: Int?) {
        this.errorDrawable = errorDrawable
    }

    fun getErrorActivityClass(): Class<out Activity?>? {
        return errorActivityClass
    }

    fun setErrorActivityClass(errorActivityClass: Class<out Activity?>?) {
        this.errorActivityClass = errorActivityClass
    }

    fun getRestartActivityClass(): Class<out Activity?>? {
        return restartActivityClass
    }

    fun setRestartActivityClass(restartActivityClass: Class<out Activity?>?) {
        this.restartActivityClass = restartActivityClass
    }

    fun getEventListener(): RxCrashTool.EventListener? {
        return eventListener
    }

    fun setEventListener(eventListener: RxCrashTool.EventListener?) {
        this.eventListener = eventListener
    }

    @IntDef(BACKGROUND_MODE_CRASH, BACKGROUND_MODE_SHOW_CUSTOM, BACKGROUND_MODE_SILENT)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    private annotation class BackgroundMode { //I hate empty blocks
    }

    class Builder {
        private var config: RxCrashConfig? = null

        /**
         * Defines if the error activity must be launched when the app is on background.
         * BackgroundMode.BACKGROUND_MODE_SHOW_CUSTOM: launch the error activity when the app is in background,
         * BackgroundMode.BACKGROUND_MODE_CRASH: launch the default system error when the app is in background,
         * BackgroundMode.BACKGROUND_MODE_SILENT: crash silently when the app is in background,
         * The default is BackgroundMode.BACKGROUND_MODE_SHOW_CUSTOM (the app will be brought to front when a crash occurs).
         */
        fun backgroundMode(@BackgroundMode backgroundMode: Int): Builder {
            config!!.backgroundMode = backgroundMode
            return this
        }

        /**
         * Defines if CustomActivityOnCrash crash interception mechanism is enabled.
         * Set it to true if you want CustomActivityOnCrash to intercept crashes,
         * false if you want them to be treated as if the library was not installed.
         * The default is true.
         */
        fun enabled(enabled: Boolean): Builder {
            config!!.enabled = enabled
            return this
        }

        /**
         * Defines if the error activity must shown the error details button.
         * Set it to true if you want to show the full stack trace and device info,
         * false if you want it to be hidden.
         * The default is true.
         */
        fun showErrorDetails(showErrorDetails: Boolean): Builder {
            config!!.showErrorDetails = showErrorDetails
            return this
        }

        /**
         * Defines if the error activity should show a restart button.
         * Set it to true if you want to show a restart button,
         * false if you want to show a close button.
         * Note that even if restart is enabled but you app does not have any launcher activities,
         * a close button will still be used by the default error activity.
         * The default is true.
         */
        fun showRestartButton(showRestartButton: Boolean): Builder {
            config!!.showRestartButton = showRestartButton
            return this
        }

        /**
         * Defines if the stack trace must be logged again once the custom activity is shown.
         * Set it to true if you want to log the stack trace again,
         * false if you don't want the extra logging.
         * This option exists because the default Android Studio logcat view only shows the output
         * of the current process, and since the error activity runs on a new process,
         * you can't see the previous output easily.
         * Internally, it's logged when getConfigFromIntent() is called.
         * The default is true.
         */
        fun logErrorOnRestart(logErrorOnRestart: Boolean): Builder {
            config!!.logErrorOnRestart = logErrorOnRestart
            return this
        }

        /**
         * Defines if the activities visited by the user should be tracked
         * so they are reported when an error occurs.
         * The default is false.
         */
        fun trackActivities(trackActivities: Boolean): Builder {
            config!!.trackActivities = trackActivities
            return this
        }

        /**
         * Defines the time that must pass between app crashes to determine that we are not
         * in a crash loop. If a crash has occurred less that this time ago,
         * the error activity will not be launched and the system crash screen will be invoked.
         * The default is 3000.
         */
        fun minTimeBetweenCrashesMs(minTimeBetweenCrashesMs: Int): Builder {
            config!!.minTimeBetweenCrashesMs = minTimeBetweenCrashesMs
            return this
        }

        /**
         * Defines which drawable to use in the default error activity image.
         * Set this if you want to use an image other than the default one.
         * The default is R.drawable.customactivityoncrash_error_image (a cute upside-down bug).
         */
        fun errorDrawable(@DrawableRes errorDrawable: Int?): Builder {
            config!!.errorDrawable = errorDrawable
            return this
        }

        /**
         * Sets the error activity class to launch when a crash occurs.
         * If null, the default error activity will be used.
         */
        fun errorActivity(errorActivityClass: Class<out Activity?>?): Builder {
            config!!.errorActivityClass = errorActivityClass
            return this
        }

        /**
         * Sets the main activity class that the error activity must launch when a crash occurs.
         * If not set or set to null, the default launch activity will be used.
         * If your app has no launch activities and this is not set, the default error activity will close instead.
         */
        fun restartActivity(restartActivityClass: Class<out Activity?>?): Builder {
            config!!.restartActivityClass = restartActivityClass
            return this
        }

        /**
         * Sets an event listener to be called when events occur, so they can be reported
         * by the app as, for example, Google Analytics events.
         * If not set or set to null, no events will be reported.
         *
         * @param eventListener The event listener.
         * @throws IllegalArgumentException if the eventListener is an inner or anonymous class
         */
        fun eventListener(eventListener: RxCrashTool.EventListener?): Builder {
            require(!(eventListener != null && eventListener.javaClass.enclosingClass != null && !Modifier.isStatic(eventListener.javaClass.modifiers))) { "The event listener cannot be an inner or anonymous class, because it will need to be serialized. Change it to a class of its own, or make it a static inner class." }
            config!!.eventListener = eventListener
            return this
        }

        fun get(): RxCrashConfig {
            return config!!
        }

        fun apply() {
            RxCrashTool.setConfig(config!!)
        }

        companion object {
            fun create(): Builder {
                val builder = Builder()
                val currentConfig = RxCrashTool.getConfig()
                val config = RxCrashConfig()
                config.backgroundMode = currentConfig.backgroundMode
                config.enabled = currentConfig.enabled
                config.showErrorDetails = currentConfig.showErrorDetails
                config.showRestartButton = currentConfig.showRestartButton
                config.logErrorOnRestart = currentConfig.logErrorOnRestart
                config.trackActivities = currentConfig.trackActivities
                config.minTimeBetweenCrashesMs = currentConfig.minTimeBetweenCrashesMs
                config.errorDrawable = currentConfig.errorDrawable
                config.errorActivityClass = currentConfig.errorActivityClass
                config.restartActivityClass = currentConfig.restartActivityClass
                config.eventListener = currentConfig.eventListener
                builder.config = config
                return builder
            }
        }
    }

    companion object {
        const val BACKGROUND_MODE_SILENT = 0
        const val BACKGROUND_MODE_SHOW_CUSTOM = 1
        const val BACKGROUND_MODE_CRASH = 2
    }
}