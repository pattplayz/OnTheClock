package src.com.pattplayz.otc;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class OTCCommand implements CommandExecutor
{
	OTC plugin;

	public OTCCommand(OTC instance)
	{
		plugin = instance;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("otc"))
			{
				try
				{
					if (args.length < 1)
					{
						player.sendMessage(OTCHelper.formatString("&6[OTC] &eOnTheClock is written by Ar7ific1al", ""));

						if (player.hasPermission("otc.announce.custom"))
						{
							player.sendMessage(OTCHelper.formatString("&6[OTC] &eYou have permission &cotc.announce.custom&e."
									+ " Use &c/otc cb Message&e to set your own custom join message."
									+ "You can use format codes. Here's an example:",""));
							player.sendMessage("/otc cb &e" + player.getName() + " is here to &osteal &eyour potatoes!");
						}
					}
					else
					{
						if (args[0].equalsIgnoreCase("cb")
								&& player.hasPermission("otc.announce.custom"))
						{
							File f = new File(plugin.clockDir,
									player.getUniqueId() + ".clock");
							FileConfiguration tempfc = new YamlConfiguration();
							tempfc.load(f);
							if (args.length < 2)
							{
								player.sendMessage(OTCHelper.formatString("&6[OTC] &eYour announcement is currently set to: ","")
										+ tempfc.getString("BroadcastMessage"));
							}
							else if (args.length > 1)
							{
								String message = "";
								for (int i = 1; i < args.length; i++)
								{
									message += args[i];
									if (i != args.length)
										message += " ";
								}
								tempfc.set("BroadcastMessage", message);
								tempfc.save(f);
								player.sendMessage(OTCHelper.formatString("&6[OTC] &eYour announcement was changed."
										+ " Here is a preview:\n" + message, ""));
							}
						}
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				return true;
			}
		}
		return false;
	}
}
