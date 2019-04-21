package tw.chikuo.linetvrecruitment.controller

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

class Application : android.app.Application() {

    companion object {
        var DEVICE_DENSITY_DPI = 0
    }

    override fun onCreate() {
        super.onCreate()

        // DisplayMetrics
        val metrics = DisplayMetrics()
        val windowManager = applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(metrics)
        DEVICE_DENSITY_DPI = metrics.densityDpi
    }


}
