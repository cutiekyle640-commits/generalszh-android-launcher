package com.cutiekyle.zhportremake.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.cutiekyle.zhportremake.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class LogEntry(val ts: String, val msg: String, val level: String)

class MainViewModel(private val repo: PrefsRepository, private val ctx: Context) : ViewModel() {

    val settings: StateFlow<AppSettings> = repo.settingsFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, AppSettings())

    private val _logs = MutableStateFlow<List<LogEntry>>(emptyList())
    val logs: StateFlow<List<LogEntry>> = _logs.asStateFlow()

    init { log("ZH Port Remake initialized", "ok") }

    fun log(msg: String, level: String = "info") {
        val ts = SimpleDateFormat("HH:mm:ss", Locale.US).format(Date())
        _logs.update { (listOf(LogEntry(ts, msg, level)) + it).take(500) }
    }

    fun updateMods(mods: List<ModInfo>) {
        viewModelScope.launch {
            repo.save(settings.value.copy(mods = mods))
            log("Mods updated: ${mods.count { it.enabled }} active", "info")
        }
    }

    fun saveSettings(s: AppSettings) {
        viewModelScope.launch {
            repo.save(s)
            log("Settings saved", "ok")
        }
    }

    fun clearLogs() { _logs.value = emptyList() }

    fun buildLaunchCmd(): String {
        val s = settings.value
        val lo = topoSort(s.mods) ?: return ""
        if (lo.isEmpty()) return ""
        val mod = lo.first()
        val exe = (mod.exeOverride.ifBlank { s.gameExe }).ifBlank { "generals.exe" }
        val dir = s.gameDir.ifBlank { "C:\\ZH" }
        val folder = mod.folderPath
        return "\"$dir\\$exe\" -mod \"$folder\""
    }

    fun copyLaunchCmd() {
        val cmd = buildLaunchCmd()
        if (cmd.isBlank()) return
        val cm = ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        cm.setPrimaryClip(ClipData.newPlainText("ZH Launch Command", cmd))
        log("Launch command copied", "ok")
    }

    companion object {
        fun Factory(ctx: Context) = viewModelFactory {
            initializer {
                MainViewModel(PrefsRepository(ctx.applicationContext), ctx.applicationContext)
            }
        }
    }
}
