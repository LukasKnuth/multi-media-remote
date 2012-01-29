#include <jni.h>
#include <iostream>
#include "remote.h"
#include <windows.h>
using namespace std;

// Constants for operations:
const int PAUSE_PLAY = 0;
const int STOP = 1;
const int NEXT = 2;
const int PREVIOUS = 3;
const int MUTE = 4;
const int VOL_UP = 5;
const int VOL_DOWN = 6;

void simulatePress(int operation_id){
	/* We MUST set ALL propertys to a value, even those which
	 are not used. In those cases, "0" can be used! */
	INPUT *operation = new INPUT[2];
	// Press Key
	operation[0].type = INPUT_KEYBOARD;	// Input from Keyboard
	operation[0].ki.wScan = 0;
	operation[0].ki.dwExtraInfo = 0;
	operation[0].ki.dwFlags = 0;
	operation[0].ki.time = 0;
	// Release the key:
	operation[1].type = INPUT_KEYBOARD;
	operation[1].ki.dwFlags = KEYEVENTF_KEYUP;
	operation[1].ki.time = 0;
	operation[1].ki.dwExtraInfo = 0;
	operation[1].ki.wScan = 0;

	// Add the Key-Code depending on the desired operation:
	switch (operation_id){
	case PAUSE_PLAY:
		operation[0].ki.wVk = VK_MEDIA_PLAY_PAUSE;	// Key-Pres
		operation[1].ki.wVk = VK_MEDIA_PLAY_PAUSE;	// Key-Release
		break;
	case STOP:
		operation[0].ki.wVk = VK_MEDIA_STOP;
		operation[1].ki.wVk = VK_MEDIA_STOP;
		break;
	case NEXT:
		operation[0].ki.wVk = VK_MEDIA_NEXT_TRACK;
		operation[1].ki.wVk = VK_MEDIA_NEXT_TRACK;
		break;
	case PREVIOUS:
		operation[0].ki.wVk = VK_MEDIA_PREV_TRACK;
		operation[1].ki.wVk = VK_MEDIA_PREV_TRACK;
		break;
	case MUTE:
		operation[0].ki.wVk = VK_VOLUME_MUTE;
		operation[1].ki.wVk = VK_VOLUME_MUTE;
		break;
	case VOL_UP:
		operation[0].ki.wVk = VK_VOLUME_UP;
		operation[1].ki.wVk = VK_VOLUME_UP;
		break;
	case VOL_DOWN:
		operation[0].ki.wVk = VK_VOLUME_DOWN;
		operation[1].ki.wVk = VK_VOLUME_DOWN;
		break;
	}
	// Send key-press:
	SendInput(2, operation, sizeof(INPUT));
}

// Pause/Play method:
JNIEXPORT void JNICALL Java_org_knuth_multimediaremote_server_model_remotes_NativeRemote_nPausePlay
  (JNIEnv *, jobject){
    simulatePress(PAUSE_PLAY);
}

// Stop method:
JNIEXPORT void JNICALL Java_org_knuth_multimediaremote_server_model_remotes_NativeRemote_nStop
  (JNIEnv *, jobject){
    simulatePress(STOP);
}

// NextTrack method:
JNIEXPORT void JNICALL Java_org_knuth_multimediaremote_server_model_remotes_NativeRemote_nNextTrack
  (JNIEnv *, jobject){
    simulatePress(NEXT);
}

// PreviousTrack method:
JNIEXPORT void JNICALL Java_org_knuth_multimediaremote_server_model_remotes_NativeRemote_nPreviousTrack
  (JNIEnv *, jobject){
    simulatePress(PREVIOUS);
}

// VolUp method:
JNIEXPORT void JNICALL Java_org_knuth_multimediaremote_server_model_remotes_NativeRemote_nVolUp
  (JNIEnv *, jobject){
    simulatePress(VOL_UP);
}

// VolDown method:
JNIEXPORT void JNICALL Java_org_knuth_multimediaremote_server_model_remotes_NativeRemote_nVolDown
  (JNIEnv *, jobject){
    simulatePress(VOL_DOWN);
}

// Mute method:
JNIEXPORT void JNICALL Java_org_knuth_multimediaremote_server_model_remotes_NativeRemote_nMute
  (JNIEnv *, jobject){
    simulatePress(MUTE);
}
