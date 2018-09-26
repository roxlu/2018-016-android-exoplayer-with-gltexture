#!/bin/sh
export ANDROID_SERIAL=emulator-5554
./gradlew :app:installDebug && adb shell am start -n rox.lu/rox.lu.MainActivity
