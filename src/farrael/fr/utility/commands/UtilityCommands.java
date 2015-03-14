package farrael.fr.utility.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import farrael.fr.utility.Utility;
import farrael.fr.utility.configuration.ConfigManager;
import farrael.fr.utility.configuration.Configuration;
import farrael.fr.utility.configuration.FileManager.FileType;
import farrael.fr.utility.utils.ChatUtils;

public class UtilityCommands implements CommandExecutor {
	Utility plugin = Utility.getInstance();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if((sender instanceof Player) && 
				!plugin.hasPermission((Player)sender, "utility.admin"))
			return plugin.sendPluginMessage(sender, ConfigManager.permission, true);

		if(args.length < 1) {
			StringBuilder test = new StringBuilder();
			test.append(ChatColor.GOLD + "# " + ChatColor.AQUA + "Description : " + ChatColor.GREEN + plugin.getDescription().getDescription() + "\n");
			test.append(ChatColor.GOLD + "# " + ChatColor.AQUA + "Version :     " + ChatColor.GREEN + plugin.getDescription().getVersion() + "\n");
			test.append(ChatColor.GOLD + "# \n");
			test.append(ChatColor.GOLD + "# " + ChatColor.AQUA + "Contributor : "  + ChatColor.GREEN + "Farrael");

			sender.sendMessage(ChatColor.GOLD + "\n#--------[" + ChatColor.BLUE + plugin.getName() + ChatColor.GOLD + "]--------#");
			sender.sendMessage(test.toString());
			sender.sendMessage(ChatColor.GOLD + "#--------------------------#\n");

			return true;
		}

		String arg = args[0].toString();
		if(arg.equalsIgnoreCase("help")){			
			sender.sendMessage(ChatColor.GOLD + "\n#----- " + ChatColor.GREEN + "Aide [1/1]" + ChatColor.GOLD + " -----#");
			sender.sendMessage(plugin.getUsage("/utility reload : Reload la configuration."));
			sender.sendMessage(plugin.getUsage("/utility [on/off] : Active ou désactive le plugin."));
			sender.sendMessage(plugin.getUsage("/utility header [header] : Modifie le header."));
			sender.sendMessage(plugin.getUsage("/utility footer [footer] : Modifie le footer."));
			sender.sendMessage(plugin.getUsage("/utility motd [on/off] : Activer ou désactiver le motd."));
			sender.sendMessage(plugin.getUsage("/utility motd [message] : Modifier le motd."));

		} else if(arg.equalsIgnoreCase("reload")){			
			plugin.fileManager.getFile(FileType.CONFIG).update();
			plugin.configManager.reload(FileType.CONFIG, true);
			plugin.sendPluginMessage(sender, "Config reloaded.", false);
			ChatUtils.sendTabListToServer();

		} else if(arg.equalsIgnoreCase("header") || arg.equalsIgnoreCase("footer")){
			if(args.length < 2){
				sender.sendMessage(ChatColor.RED + "Il manque des arguments...\n" + plugin.getUsage("/utility " + arg.toLowerCase() + " [value]"));
				return true;
			}

			boolean isHeader = arg.equalsIgnoreCase("header");
			String value = plugin.getArguments(args, 1);

			if(isHeader) {
				Configuration.HEADER = value;
				plugin.fileManager.setData(FileType.CONFIG, "header", value);
			} else {
				Configuration.FOOTER = value;
				plugin.fileManager.setData(FileType.CONFIG, "footer", value);
			}

			plugin.sendPluginMessage(sender, "Vous venez de modifier le " + (isHeader ? "header" : "footer") + " de la liste des connectés.", false);
			ChatUtils.sendTabListToServer();

		} else if(arg.equalsIgnoreCase("motd")){
			if(args.length < 2){
				sender.sendMessage(ChatColor.RED + "Il manque des arguments...\n" + plugin.getUsage("/utility " + arg.toLowerCase() + " [value]"));
				return true;
			}

			if(args[1].equalsIgnoreCase("on") || args[1].equalsIgnoreCase("off")) {
				boolean stat = false;
				if(args[1].equalsIgnoreCase("on"))
					stat = true;

				Configuration.MOTD_ENABLE = stat;
				plugin.fileManager.setData(FileType.CONFIG, "motd-enable", stat);
				plugin.sendPluginMessage(sender, "Vous venez " + (stat ? "d'activer" : "de désactiver") + " le message du jour.", false);
			} else {
				String value = plugin.getArguments(args, 1).replace("\\n", "\n");
				Configuration.MOTD = ChatUtils.parseMotdColor('&', value);
				plugin.fileManager.setData(FileType.CONFIG, "motd", value);
				plugin.sendPluginMessage(sender, "Vous venez de modifier le message du jour.", false);
			}

		} else if(arg.equalsIgnoreCase("on") || arg.equalsIgnoreCase("off")){
			Boolean bol = false;
			if(arg.equalsIgnoreCase("on"))
				bol = true;

			Configuration.ENABLE = bol;
			plugin.fileManager.setData(FileType.CONFIG, "enable", bol);
			plugin.sendPluginMessage(sender, "Vous venez " + (bol ? "d'activer" : "de désactiver") + " le plugin.", false);

		} else {
			plugin.sendPluginMessage(sender, "Tapez /utility help pour les informations sur les commandes.", false);
		}

		return true;
	}

}
