package src.com.pattplayz.otc;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class OTCListener implements Listener
{
	private OTC plugin;

	public OTCListener(OTC instance)
	{
		plugin = instance;
		OTC.console.log(Level.INFO, "[OTC] Player join listener registered.");
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) throws IOException,
			InvalidConfigurationException
	{
		Player player = e.getPlayer();
		if (player.hasPermission("otc.clock"))
		{
			File f = new File(plugin.clockDir, player.getUniqueId() + ".clock");
			if (!f.exists()){
				OTCHelper.makeNewClockFile(player);
			}
			
			OTCHelper.clock(player, true);
	
			if (player.hasPermission("otc.announce.mod")
					&& !player.hasPermission("otc.announce.custom"))
			{
				e.setJoinMessage(OTCHelper.broadcastJoin(player, true));
			}
			else if (player.hasPermission("otc.announce.custom"))
			{
				e.setJoinMessage(OTCHelper.broadcastJoin(player, true));
			}
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) throws IOException,
			InvalidConfigurationException
	{
		Player player = e.getPlayer();
		if (player.hasPermission("otc.clock"))
		{
			OTCHelper.clock(player, false);
		}
	}

}
