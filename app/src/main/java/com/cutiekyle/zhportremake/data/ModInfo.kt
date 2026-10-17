package com.cutiekyle.zhportremake.data

import java.util.UUID

data class ModInfo(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val author: String = "",
    val version: String = "",
    val description: String = "",
    val folderPath: String,
    val exeOverride: String = "",
    val priority: Int = 0,
    val enabled: Boolean = false,
    val requires: List<String> = emptyList(),
    val conflicts: List<String> = emptyList(),
    val notes: String = ""
)

data class CompatResult(
    val conflicts: List<Pair<String, String>> = emptyList(),
    val missingReqs: List<Pair<String, String>> = emptyList()
) {
    val isClean get() = conflicts.isEmpty() && missingReqs.isEmpty()
}

fun checkCompat(mods: List<ModInfo>): CompatResult {
    val enabled = mods.filter { it.enabled }
    val names = enabled.map { it.name.lowercase() }.toSet()
    val folders = enabled.map { it.folderPath.lowercase() }.toSet()
    val nameMap = enabled.associateBy { it.name.lowercase() }
    val inSet = { r: String -> r.lowercase() in names || r.lowercase() in folders }
    val conflicts = mutableListOf<Pair<String, String>>()
    val missing = mutableListOf<Pair<String, String>>()
    val seen = mutableSetOf<String>()
    for (m in enabled) {
        for (c in m.conflicts) {
            val other = nameMap[c.lowercase()]
            if (other != null) {
                val key = listOf(m.id, other.id).sorted().joinToString("|")
                if (key !in seen) { seen += key; conflicts += m.name to other.name }
            }
        }
        for (req in m.requires) {
            if (!inSet(req)) missing += m.name to req
        }
    }
    return CompatResult(conflicts, missing)
}

fun topoSort(mods: List<ModInfo>): List<ModInfo>? {
    val enabled = mods.filter { it.enabled }.sortedBy { it.priority }
    val idMap = enabled.associateBy { it.id }
    val nameMap = enabled.associateBy { it.name.lowercase() }
    val resolve = { r: String -> nameMap[r.lowercase()]?.id }
    val visited = mutableSetOf<String>()
    val stack = mutableSetOf<String>()
    val order = mutableListOf<String>()
    fun visit(id: String): Boolean {
        if (id in stack) return false
        if (id in visited) return true
        stack += id
        val m = idMap[id] ?: return true
        for (req in m.requires) {
            val rid = resolve(req) ?: continue
            if (!visit(rid)) return false
        }
        stack -= id; visited += id; order += id
        return true
    }
    for (m in enabled) if (!visit(m.id)) return null
    return order.mapNotNull { idMap[it] }
}
