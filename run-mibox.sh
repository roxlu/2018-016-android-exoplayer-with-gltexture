#!/bin/sh
export ANDROID_SERIAL=13878580469799
./gradlew :app:installDebug && adb shell am start -n rox.lu/rox.lu.MainActivity
