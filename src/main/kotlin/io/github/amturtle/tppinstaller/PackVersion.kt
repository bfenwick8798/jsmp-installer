package io.github.amturtle.tppinstaller

import com.google.gson.JsonObject
import java.nio.file.Path

class PackVersion(val modpack: Modpack, val data: JsonObject) {
    val packVersion: String
    val gameVersion: String
    val loader: Loader

    init {
        val versionNumber = data["version_number"].asString
        if ('-' in versionNumber && '+' !in versionNumber) {
            packVersion = versionNumber.substringBefore('-')
            gameVersion = versionNumber.substringAfterLast('-')
            loader = if (versionNumber.count { it == '-' } > 1) {
                Loader.valueOf(versionNumber.substringAfter('-').substringBeforeLast('-').uppercase())
            } else {
                Loader.FABRIC
            }
        } else {
            packVersion = versionNumber.substringBefore('+')
            gameVersion = versionNumber.substringAfter('+').substringBeforeLast('.')
            loader = try {
                Loader.valueOf(versionNumber.substringAfterLast('.').uppercase())
            } catch (e: IllegalArgumentException) {
                Loader.FABRIC // Default to FABRIC if loader is not recognized
            }
        }
    }

    val launcherFolderPath = "${modpack.id}/$packVersion-$gameVersion-$loader"
    val launcherVersionId = "${modpack.id}-$packVersion-$gameVersion-$loader"
    val launcherProfileId = "${modpack.id}-$gameVersion-$loader"
    val isSupported = data["featured"].asBoolean

    fun install(destination: Path, progressHandler: ProgressHandler) =
        PackInstaller(this, destination, progressHandler)
            .use(PackInstaller::install)
}
