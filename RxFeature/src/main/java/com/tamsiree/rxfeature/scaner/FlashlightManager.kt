package com.tamsiree.rxfeature.scaner

import android.os.IBinder
import com.tamsiree.rxkit.TLog.v
import com.tamsiree.rxkit.TLog.w
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

/**
 * @author Tamsiree
 */
internal object FlashlightManager {
    private val TAG = FlashlightManager::class.java.simpleName
    private var iHardwareService: Any? = null
    private var setFlashEnabledMethod: Method? = null

    @JvmStatic
    fun enableFlashlight() {
        setFlashlight(false)
    }

    @JvmStatic
    fun disableFlashlight() {
        setFlashlight(false)
    }

    private val hardwareService: Any?
        private get() {
            val serviceManagerClass = maybeForName("android.os.ServiceManager") ?: return null
            val getServiceMethod = maybeGetMethod(serviceManagerClass, "getService", String::class.java)
                    ?: return null
            val hardwareService = invoke(getServiceMethod, null, "hardware") ?: return null
            val iHardwareServiceStubClass = maybeForName("android.os.IHardwareService\$Stub")
                    ?: return null
            val asInterfaceMethod = maybeGetMethod(iHardwareServiceStubClass, "asInterface", IBinder::class.java)
                    ?: return null
            return invoke(asInterfaceMethod, null, hardwareService)
        }

    private fun getSetFlashEnabledMethod(iHardwareService: Any?): Method? {
        if (iHardwareService == null) {
            return null
        }
        val proxyClass: Class<*> = iHardwareService.javaClass
        return maybeGetMethod(proxyClass, "setFlashlightEnabled", Boolean::class.javaPrimitiveType!!)
    }

    private fun maybeForName(name: String): Class<*>? {
        return try {
            Class.forName(name)
        } catch (cnfe: ClassNotFoundException) {
            // OK
            null
        } catch (re: RuntimeException) {
            w(TAG, "Unexpected error while finding class $name", re)
            null
        }
    }

    private fun maybeGetMethod(clazz: Class<*>, name: String, vararg argClasses: Class<*>): Method? {
        return try {
            clazz.getMethod(name, *argClasses)
        } catch (nsme: NoSuchMethodException) {
            // OK
            null
        } catch (re: RuntimeException) {
            w(TAG, "Unexpected error while finding method $name", re)
            null
        }
    }

    private operator fun invoke(method: Method?, instance: Any?, vararg args: Any): Any? {
        return try {
            method!!.invoke(instance, *args)
        } catch (e: IllegalAccessException) {
            w(TAG, "Unexpected error while invoking $method", e)
            null
        } catch (e: InvocationTargetException) {
            w(TAG, "Unexpected error while invoking $method", e.cause)
            null
        } catch (re: RuntimeException) {
            w(TAG, "Unexpected error while invoking $method", re)
            null
        }
    }

    private fun setFlashlight(active: Boolean) {
        if (iHardwareService != null) {
            invoke(setFlashEnabledMethod, iHardwareService, active)
        }
    }

    init {
        iHardwareService = hardwareService
        setFlashEnabledMethod = getSetFlashEnabledMethod(iHardwareService)
        if (iHardwareService == null) {
            v(TAG, "This device does supports control of a flashlight")
        } else {
            v(TAG, "This device does not support control of a flashlight")
        }
    }
}