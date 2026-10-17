# Generals ZH Launcher — Android

Winlator-style mod compatibility launcher for the [GeneralsZH Android Port](https://github.com/MYSOREZ/GeneralsZH-Android-Port).

## Features
- **Mods tab** — add/edit/delete mods, toggle, drag-to-reorder priority, compatibility matrix (conflict / dependency detection), known-mod search database
- **Graphics tab** — renderer picker (Vulkan/OpenGL ES 3/2/Turnip), resolution, scaling, upscale quality, texture/shadow/particle/lighting/AF/MSAA toggles
- **Performance tab** — CPU governor, thread count, FPS cap, VSync, low-latency, heap limit, texture streaming, benchmark sim
- **Controls tab** — all touch gesture toggles, pan/zoom/dead-zone/long-press sensitivity sliders, HUD opacity + size
- **Network tab** — GeneralsOnline server, STUN/TURN, ICE, multicast lock, bandwidth cap
- **Profiles tab** — named setting profiles with emoji icons
- **Logs tab** — live launcher log with level filter
- Import/Export JSON config
- PWA — also installable from Chrome on Android without an APK

## Download APK
Go to the [Releases](../../releases) tab — APK is auto-built on every push by GitHub Actions.

## Build it yourself
```bash
git clone https://github.com/cutiekyle640-commits/generalszh-android-launcher
cd generalszh-android-launcher
./gradlew assembleDebug
# APK at: app/build/outputs/apk/debug/app-debug.apk
```

## Install
1. Enable **Install unknown apps** on your Android device
2. Download the APK from [Releases](../../releases)
3. Tap the APK file and install

## PWA (no APK needed)
Host the `index.html` + `sw.js` anywhere, open in Chrome on Android, tap **Add to Home Screen**.
