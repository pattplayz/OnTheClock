package src.com.pattplayz.otc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class OTC extends JavaPlugin {

	public static final Logger console = Logger.getLogger("OTC");
	public OTCHelper helper;

	File pluginDir = new File("plugins/OnTheClock/");
	File clockDir = new File("plugins/OnTheClock/Players/");

	public void onEnable()
	{
		PluginManager pm = getServer().getPluginManager();
		PluginDescriptionFile pdFile = this.getDescription();
		String ver = pdFile.getVersion();

		console.log(Level.INFO, "[OTC] Starting OTC v" + ver + "...");

		if (!pluginDir.exists())
		{
			console.log(Level.INFO,
					"[OTC] Performing first-time startup operations.");
			pluginDir.mkdir();
			clockDir.mkdir();
			console.log(Level.INFO, "[OTC] First-time startup operations complete.");
		}
		if (!clockDir.exists())
		{
			console.log(Level.INFO, "[OTC] Players directory was not found; the directory has been created.");
		}
		
		File settings = new File(getDataFolder(), "settings.yml");
		if (!settings.exists())
			saveResource("settings.yml", false);

		try
		{
			helper = new OTCHelper(this);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (InvalidConfigurationException e)
		{
			e.printStackTrace();
		}
		
		pm.registerEvents(new OTCListener(this), this);

		getCommand("otc").setExecutor(new OTCCommand(this));
	}
	
	@Override
	public void onDisable()
	{
		console.log(Level.INFO, "[OTC] Clocking out players...");
		for (Player p : Bukkit.getServer().getOnlinePlayers())
		{
			if (p.hasPermission("otc.clock")){
				try
				{
					OTCHelper.clock(p, false);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				catch (InvalidConfigurationException e)
				{
					e.printStackTrace();
				}
			}
		}
		console.log(Level.INFO, "[OTC] OTC disabled.");
	}
}
