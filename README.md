# GeneralsZH Port Remake by cutiekyle._.

Native Android mod launcher for the [GeneralsZH Android Port](https://github.com/MYSOREZ/GeneralsZH-Android-Port).

Built with **Kotlin + Jetpack Compose**. Full native app — no WebView, no HTML.

## 📥 Download APK

**[Go to Releases →](../../releases/latest)**

APK is auto-built by GitHub Actions on every push. Download `app-debug.apk`, enable **Install unknown apps** on Android, tap and install.

## Features

| Tab | What it does |
|---|---|
| **Launch** | Mod load order resolver, launch command builder, game dir config |
| **Mods** | Add/edit/delete mods, toggles, drag priority, compat matrix (conflict/dep detection) |
| **Graphics** | Renderer (Vulkan/OpenGL ES/Turnip), resolution, texture, shadow, particles, MSAA |
| **Performance** | CPU governor, thread count, FPS cap, VSync, low-latency, heap limit |
| **Controls** | Touch gesture toggles, pan/zoom/long-press/dead-zone sensitivity sliders |
| **Network** | GeneralsOnline server, STUN/TURN/ICE, multicast lock, bandwidth |
| **Profiles** | Quick-apply named configurations |
| **Logs** | Live launcher log with level filter |

## Build yourself

```bash
git clone https://github.com/cutiekyle640-commits/generalszh-port-remake
cd generalszh-port-remake
./gradlew assembleDebug
# APK: app/build/outputs/apk/debug/app-debug.apk
```

Requires JDK 17 + Android SDK.
