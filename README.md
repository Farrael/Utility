# Utility #

Some utilities for minecraft server.

Compatible Bukkit and Spigot 1.8.1.

### 1- How the plugin work

* Place the .jar in your plugins folder.
* Start your server.
* The plugin will create a Utility folder with configurations in your plugin folder.

### 2 - Commands

* /utility [on/off] - Enable/Disable plugin
* /utility header [message] - Change tab-header message
* /utility footer [message] - Change tab-footer message
* /utility motd [on/off] - Enable/Disable motd
* /utility motd [message] - Edit motd message
* /utility reload - Reload plugin configuration

### 3 - Configuration

* header - In-game Tabulation header message (work with [format](#format))
* footer - In-game Tabulation footer message (work with [format](#format))
* motd - Message of the day (work with [color](#color))
* motd-enable - Enable/Disable motd message.
* soil-protect - Enable/Disable soil protection.
* enable - Enable/Disable plugin
* console-Debug - Debug message on console.

### 4 - Format <a id="format"></a>
You can use the following formats to add dynamisme in your messages.

<a id="color"></a>Color need to start with '&', for exemple '&9' is blue.

* %server% - Server name (server.conf)
* %online% - Online players number
* %player% - Player's name
* \n - New line with motd.

### 5 - Contributors

* Farrael
