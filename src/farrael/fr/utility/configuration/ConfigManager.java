package farrael.fr.utility.configuration;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import farrael.fr.utility.Utility;
import farrael.fr.utility.configuration.FileManager.FileType;
import farrael.fr.utility.utils.ChatUtils;

public class ConfigManager {
	private boolean 		loaded 			= false;
	private boolean			debug			= false;
	private FileManager 	fileManager		= null;
	private Utility 			plugin			= null;

	public static String 	permission 		= ChatColor.RED + "Vous n'avez pas la permission pour effectuer cela.";

	public ConfigManager(Utility plugin, FileManager fileManager){
		this.plugin 		= plugin;
		this.fileManager 	= fileManager;
	}

	/**
	 * Return FileManager used.
	 */
	public FileManager getFileManager(){
		return this.fileManager;
	}

	/**
	 * Is config load successful.
	 * @return True if finished without error.
	 */
	public boolean isLoaded(){
		return this.loaded;
	}

	/**
	 * Load config from FileManager
	 */
	public boolean load(boolean reload){
		for(FileType type : this.fileManager.getFileList())
			this.loadType(type, reload);
		this.loaded = true;
		return true;
	}

	/**
	 * Load file by Type
	 * @param type
	 */
	private void loadType(FileType type, boolean reload){
		long startTime = System.currentTimeMillis();
		switch(type){
		case CONFIG:
			this.loadConfig(type);
			break;
		default:
			break;
		}
		long endTime = System.currentTimeMillis();
		debug("Loading " + type.toString().toLowerCase() + " took " + (endTime-startTime) + "ms.");
	}

	/**
	 * Reload configuration
	 */
	public boolean reload(FileType file, boolean verbose){
		if(!this.isLoaded()) return false;

		try{
			if(file == null){
				this.loaded = false;
				this.load(true);
			} else {
				this.loadType(file, true);
			}
		} catch(Exception e){
			Bukkit.getConsoleSender().sendMessage("[" + plugin.getName() + "] " + ChatColor.RED + e);
			if(verbose)
				e.printStackTrace();
			return false;
		}

		return true;
	}

	/*****************************************************************/
	/*                  Private loading function                     */ 
	/*****************************************************************/
	private void loadConfig(FileType type){
		// Get value.
		Configuration.HEADER = (String) this.fileManager.getData(type, "header", "&6~ Bienvenue sur &c%server%&6 ~");
		Configuration.FOOTER = (String) this.fileManager.getData(type, "footer", "&6En ligne : &b%online%");
		Configuration.MOTD = ChatUtils.parseMotdColor('&', (String) this.fileManager.getData(type, "motd", ""));
		Configuration.MOTD_ENABLE = (boolean) this.fileManager.getData(type, "motd-enable", false);
		Configuration.ENABLE = (boolean) this.fileManager.getData(type, "enable", true);
		Configuration.SOIL_PROTECT = (boolean) this.fileManager.getData(type, "soil-protect", false);

		this.debug = (boolean) this.fileManager.getData(type, "console-Debug", false);

		// Set commentaries.
		this.fileManager.setComment(type, "header", false, "En-tete de liste.");
		this.fileManager.setComment(type, "footer", true, "Pied de liste.");
		this.fileManager.setComment(type, "motd", true, "Message dans la liste des serveurs.");
		this.fileManager.setComment(type, "motd-enable", true, "Activer ou desactiver le message dans la liste des serveurs.");
		this.fileManager.setComment(type, "enable", true, "Active ou desactive le plugin.");
		this.fileManager.setComment(type, "console-Debug", true, "Affiche les messages de debug dans la console.");
		this.fileManager.setComment(type, "soil-protect", "Protection des champs contre les sauts.");

		// Save file.
		this.fileManager.saveFile(type);
	}

	/**
	 * Print debug message to console
	 * @param string - Debug message
	 */
	public void debug(String string){
		if(this.debug)
			Bukkit.getConsoleSender().sendMessage("[" + plugin.getName() + "] " + ChatColor.BLUE + string);
	}
}
