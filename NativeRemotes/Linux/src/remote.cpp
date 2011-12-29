#include <jni.h>
#include "remote.h"
#include <X11/Xlib.h>
#include <X11/extensions/XTest.h>
#include <string>
using namespace std;

void simulatePress(string keysym_s){
    // Simulate Key-Press:
    Display *display;
    display = XOpenDisplay(NULL);

    // Get KeySym from Mapping-String:
    KeySym keysym = XStringToKeysym( keysym_s.c_str() );
    // Get useable keycode from KeySym:
    unsigned int keycode = XKeysymToKeycode(display, keysym);

    // Execute key-press with keycode:
    XTestFakeKeyEvent(display, keycode, true, 0);
    XTestFakeKeyEvent(display, keycode, false, 0);
    XFlush(display);
}

// Pause/Play method:
JNIEXPORT void JNICALL Java_org_knuth_multimediaremote_server_model_remotes_NativeRemote_nPausePlay
  (JNIEnv *, jobject){
    simulatePress("XF86AudioPlay");
}

// Stop method:
JNIEXPORT void JNICALL Java_org_knuth_multimediaremote_server_model_remotes_NativeRemote_nStop
  (JNIEnv *, jobject){
    simulatePress("XF86AudioStop");
}

// NextTrack method:
JNIEXPORT void JNICALL Java_org_knuth_multimediaremote_server_model_remotes_NativeRemote_nNextTrack
  (JNIEnv *, jobject){
    simulatePress("XF86AudioNext");
}

// PreviousTrack method:
JNIEXPORT void JNICALL Java_org_knuth_multimediaremote_server_model_remotes_NativeRemote_nPreviousTrack
  (JNIEnv *, jobject){
    simulatePress("XF86AudioPrev");
}

// VolUp method:
JNIEXPORT void JNICALL Java_org_knuth_multimediaremote_server_model_remotes_NativeRemote_nVolUp
  (JNIEnv *, jobject){
    simulatePress("XF86AudioRaiseVolume");
}

// VolDown method:
JNIEXPORT void JNICALL Java_org_knuth_multimediaremote_server_model_remotes_NativeRemote_nVolDown
  (JNIEnv *, jobject){
    simulatePress("XF86AudioLowerVolume");
}

// Mute method:
JNIEXPORT void JNICALL Java_org_knuth_multimediaremote_server_model_remotes_NativeRemote_nMute
  (JNIEnv *, jobject){
    simulatePress("XF86AudioMute");
}