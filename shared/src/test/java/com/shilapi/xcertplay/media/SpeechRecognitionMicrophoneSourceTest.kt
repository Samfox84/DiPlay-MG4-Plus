package com.shilapi.xcertplay.media

import android.media.MediaRecorder
import org.junit.Assert.assertEquals
import org.junit.Test

class SpeechRecognitionMicrophoneSourceTest {
    private fun source(geely: Boolean = false, saic: Boolean = false) =
        speechRecognitionMicrophoneSource(geelyHeadUnit = geely, saicHeadUnit = saic)

    @Test fun aStockHeadUnitUsesVoiceRecognitionLikeTheCarPlayService() {
        assertEquals(MediaRecorder.AudioSource.VOICE_RECOGNITION, source())
    }

    @Test fun geelyUsesVoiceCommunicationBecauseVoiceRecognitionIsSilentThere() {
        assertEquals(MediaRecorder.AudioSource.VOICE_COMMUNICATION, source(geely = true))
    }

    @Test fun saicUsesThePlainMicrophoneLikeItsOwnCarPlayRecorder() {
        assertEquals(MediaRecorder.AudioSource.MIC, source(saic = true))
    }

    @Test fun saicWinsWhenBothFlagsSomehowReportTrue() {
        assertEquals(MediaRecorder.AudioSource.MIC, source(geely = true, saic = true))
    }
}
