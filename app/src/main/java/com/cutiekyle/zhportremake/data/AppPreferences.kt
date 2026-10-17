package com.cutiekyle.zhportremake.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "zh_prefs")

object PrefKeys {
    val GAME_DIR      = stringPreferencesKey("game_dir")
    val GAME_EXE      = stringPreferencesKey("game_exe")
    val RENDERER      = stringPreferencesKey("renderer")
    val MODS_JSON     = stringPreferencesKey("mods_json")
    val FPS_CAP       = intPreferencesKey("fps_cap")
    val HEAP_LIMIT    = intPreferencesKey("heap_limit")
    val CPU_GOV       = stringPreferencesKey("cpu_gov")
    val THREAD_COUNT  = intPreferencesKey("thread_count")
    val RESOLUTION    = stringPreferencesKey("resolution")
    val VSYNC         = booleanPreferencesKey("vsync")
    val LOW_LATENCY   = booleanPreferencesKey("low_latency")
    val SHADOW        = stringPreferencesKey("shadow")
    val TEX_QUALITY   = stringPreferencesKey("tex_quality")
    val PARTICLES     = booleanPreferencesKey("particles")
    val DYN_LIGHTING  = booleanPreferencesKey("dyn_lighting")
    val MSAA          = booleanPreferencesKey("msaa")
    val GO_SERVER     = stringPreferencesKey("go_server")
    val STUN_SERVER   = stringPreferencesKey("stun_server")
    val PAN_SPEED     = intPreferencesKey("pan_speed")
    val ZOOM_SPEED    = intPreferencesKey("zoom_speed")
    val UI_SCALE      = intPreferencesKey("ui_scale")
    val HUD_OPACITY   = intPreferencesKey("hud_opacity")
    val PROFILES_JSON = stringPreferencesKey("profiles_json")
}

data class AppSettings(
    val gameDir: String = "",
    val gameExe: String = "generals.exe",
    val renderer: String = "vulkan",
    val mods: List<ModInfo> = emptyList(),
    val fpsCap: Int = 60,
    val heapLimit: Int = 1800,
    val cpuGov: String = "balanced",
    val threadCount: Int = 4,
    val resolution: String = "native",
    val vsync: Boolean = true,
    val lowLatency: Boolean = false,
    val shadow: String = "medium",
    val texQuality: String = "medium",
    val particles: Boolean = true,
    val dynLighting: Boolean = true,
    val msaa: Boolean = false,
    val goServer: String = "https://api.generalsonline.net",
    val stunServer: String = "",
    val panSpeed: Int = 5,
    val zoomSpeed: Int = 5,
    val uiScale: Int = 100,
    val hudOpacity: Int = 80
)

class PrefsRepository(private val context: Context) {
    private val gson = Gson()
    private val modListType = object : TypeToken<List<ModInfo>>() {}.type

    val settingsFlow: Flow<AppSettings> = context.dataStore.data.map { prefs ->
        val modsJson = prefs[PrefKeys.MODS_JSON] ?: ""
        val mods: List<ModInfo> = if (modsJson.isNotBlank())
            try { gson.fromJson(modsJson, modListType) } catch (e: Exception) { emptyList() }
        else defaultMods()
        AppSettings(
            gameDir     = prefs[PrefKeys.GAME_DIR]     ?: "",
            gameExe     = prefs[PrefKeys.GAME_EXE]     ?: "generals.exe",
            renderer    = prefs[PrefKeys.RENDERER]     ?: "vulkan",
            mods        = mods,
            fpsCap      = prefs[PrefKeys.FPS_CAP]      ?: 60,
            heapLimit   = prefs[PrefKeys.HEAP_LIMIT]   ?: 1800,
            cpuGov      = prefs[PrefKeys.CPU_GOV]      ?: "balanced",
            threadCount = prefs[PrefKeys.THREAD_COUNT] ?: 4,
            resolution  = prefs[PrefKeys.RESOLUTION]   ?: "native",
            vsync       = prefs[PrefKeys.VSYNC]        ?: true,
            lowLatency  = prefs[PrefKeys.LOW_LATENCY]  ?: false,
            shadow      = prefs[PrefKeys.SHADOW]       ?: "medium",
            texQuality  = prefs[PrefKeys.TEX_QUALITY]  ?: "medium",
            particles   = prefs[PrefKeys.PARTICLES]    ?: true,
            dynLighting = prefs[PrefKeys.DYN_LIGHTING] ?: true,
            msaa        = prefs[PrefKeys.MSAA]         ?: false,
            goServer    = prefs[PrefKeys.GO_SERVER]    ?: "https://api.generalsonline.net",
            stunServer  = prefs[PrefKeys.STUN_SERVER]  ?: "",
            panSpeed    = prefs[PrefKeys.PAN_SPEED]    ?: 5,
            zoomSpeed   = prefs[PrefKeys.ZOOM_SPEED]   ?: 5,
            uiScale     = prefs[PrefKeys.UI_SCALE]     ?: 100,
            hudOpacity  = prefs[PrefKeys.HUD_OPACITY]  ?: 80
        )
    }

    suspend fun save(s: AppSettings) {
        context.dataStore.edit { prefs ->
            prefs[PrefKeys.GAME_DIR]     = s.gameDir
            prefs[PrefKeys.GAME_EXE]     = s.gameExe
            prefs[PrefKeys.RENDERER]     = s.renderer
            prefs[PrefKeys.MODS_JSON]    = gson.toJson(s.mods)
            prefs[PrefKeys.FPS_CAP]      = s.fpsCap
            prefs[PrefKeys.HEAP_LIMIT]   = s.heapLimit
            prefs[PrefKeys.CPU_GOV]      = s.cpuGov
            prefs[PrefKeys.THREAD_COUNT] = s.threadCount
            prefs[PrefKeys.RESOLUTION]   = s.resolution
            prefs[PrefKeys.VSYNC]        = s.vsync
            prefs[PrefKeys.LOW_LATENCY]  = s.lowLatency
            prefs[PrefKeys.SHADOW]       = s.shadow
            prefs[PrefKeys.TEX_QUALITY]  = s.texQuality
            prefs[PrefKeys.PARTICLES]    = s.particles
            prefs[PrefKeys.DYN_LIGHTING] = s.dynLighting
            prefs[PrefKeys.MSAA]         = s.msaa
            prefs[PrefKeys.GO_SERVER]    = s.goServer
            prefs[PrefKeys.STUN_SERVER]  = s.stunServer
            prefs[PrefKeys.PAN_SPEED]    = s.panSpeed
            prefs[PrefKeys.ZOOM_SPEED]   = s.zoomSpeed
            prefs[PrefKeys.UI_SCALE]     = s.uiScale
            prefs[PrefKeys.HUD_OPACITY]  = s.hudOpacity
        }
    }

    private fun defaultMods() = listOf(
        ModInfo(name="Zero Hour Reborn",  author="Reborn Team",   version="2.1.4", folderPath="Mods/ZHReborn",  priority=0, enabled=true,  description="Total overhaul — new factions, maps, AI."),
        ModInfo(name="ShockWave",         author="ShockWave Team",version="1.1",   folderPath="Mods/ShockWave", priority=1, enabled=true,  description="Enhanced generals, new units."),
        ModInfo(name="Contra",            author="Contra Team",   version="009",   folderPath="Mods/Contra009", priority=2, enabled=false, description="Massive balance overhaul.", conflicts=listOf("ShockWave"))
    )
}
