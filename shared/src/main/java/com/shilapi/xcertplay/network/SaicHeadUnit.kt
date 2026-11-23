package com.shilapi.xcertplay.network

import android.content.Context

/**
 * Identifies a SAIC head unit (MG, Roewe) by its stock HMI packages.
 *
 * These units record silence on the voice-recognition microphone source, exactly like the Geely unit.
 * Their own CarPlay / projection recorder (`com.allgo.audiorecorder.AapAudioRecorder` in the firmware)
 * captures the plain [android.media.MediaRecorder.AudioSource.MIC] source and, around capture, toggles a
 * vendor `AudioManager` flag (`SSE_AA_VR_Wifi` for a wireless session) that routes the microphone to the
 * recording app. Without that flag every source records silence, which is why plugged-in native CarPlay
 * hears the driver but a wireless receiver does not. [com.shilapi.xcertplay.media.MicrophoneUplink]
 * matches that recipe when this returns true.
 *
 * Detected by package presence rather than [android.os.Build], so it works across the SWI firmware
 * variants, and never fires on a phone or under Robolectric (where the SAIC packages are absent).
 */
object SaicHeadUnit {

    /** Stock SAIC HMI apps; any one present means this is a SAIC head unit. */
    private val PACKAGES = listOf(
        "com.saicmotor.hmi.launcher",
        "com.saicmotor.hmi.systemsettings",
        "com.saicmotor.hmi.radio",
        "com.saicmotor.hmi.hvac",
    )

    @Volatile private var cached: Boolean? = null

    fun detected(context: Context): Boolean {
        cached?.let { return it }
        val pm = context.packageManager
        val found = PACKAGES.any { pkg ->
            runCatching { pm.getPackageInfo(pkg, 0); true }.getOrDefault(false)
        }
        cached = found
        return found
    }
}
