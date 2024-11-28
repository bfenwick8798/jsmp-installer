package io.github.amturtle.tppinstaller

import com.google.gson.JsonObject
import java.nio.file.Path

class PackVersion(val modpack: Modpack, val data: JsonObject) {
    val packVersion: String
    val gameVersion: String
    val loader: Loader

    init {
        packVersion = data["version_number"].asString
        
        // Get game version from game_versions array, using the first one
        gameVersion = data["game_versions"].asJsonArray.first().asString
        
        // Get loader from loaders array, defaulting to FABRIC if not found
        loader = try {
            val loaderStr = data["loaders"].asJsonArray.first().asString
            Loader.valueOf(loaderStr.uppercase())
        } catch (e: Exception) {
            Loader.FABRIC // Default to FABRIC if loader is not found or not recognized
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
