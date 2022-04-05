package src.com.pattplayz.otc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class OTCHelper
{
	private static String _modBroadcastFormat;
	private static String _broadcastPrefix;
	private static FileConfiguration _settings;
	private static OTC plugin;

	public OTCHelper(OTC instance) throws FileNotFoundException, IOException, InvalidConfigurationException
	{
		plugin = instance;
		File f = new File(plugin.getDataFolder(), "settings.yml");
		_settings = new YamlConfiguration();
		_settings.load(f);
		_broadcastPrefix = _settings.getString("Prefix");
		_modBroadcastFormat = _settings.getString("DefaultFormat");
	}

	public static void clock(Player player, boolean clockingIn)
			throws IOException, InvalidConfigurationException
	{
		File clock = new File(plugin.clockDir, player.getUniqueId().toString()
				+ ".clock");
		if (!clock.exists())
		{
			clock.createNewFile();
		}

		FileConfiguration tempfc = new YamlConfiguration();
		tempfc.load(clock);

		Calendar cal = Calendar.getInstance();
		Date date = new Date();
		cal.setTime(date);

		int year = cal.get(Calendar.YEAR);
		String month = new DateFormatSymbols().getMonths()[cal
				.get(Calendar.MONTH)];
		int day = cal.get(Calendar.DAY_OF_MONTH);
		String time = cal.get(Calendar.HOUR_OF_DAY) + ":"
				+ cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);

		List<String> clocks = new ArrayList<String>();

		if (clockingIn)
		{
			if (tempfc.contains(year + "." + month + "." + day))
			{
				for (Object s : tempfc.getList(year + "." + month + "." + day))
				{
					clocks.add((String) s);
				}
				clocks.add("In: " + time);
				tempfc.set(year + "." + month + "." + day, clocks);
			}
			else
			{
				clocks.add("In: " + time);
				tempfc.addDefault(year + "." + month + "." + day, clocks);
			}

			if (tempfc.getString("LastName") != player.getName())
				tempfc.set("LastName", player.getName());
		}
		else
		{
			if (tempfc.contains(year + "." + month + "." + day))
			{
				for (Object s : tempfc.getList(year + "." + month + "." + day))
				{
					clocks.add((String) s);
				}
				clocks.add("Out: " + time);
				tempfc.set(year + "." + month + "." + day, clocks);
			}
			else
			{
				clocks.add("Out: " + time);
				tempfc.addDefault(year + "." + month + "." + day, clocks);
			}
		}
		tempfc.options().copyDefaults(true);
		tempfc.save(clock);
	}

	public static String broadcastJoin(Player player, boolean useCustom)
			throws IOException, InvalidConfigurationException
	{
		String name = player.getName();
		UUID uuid = player.getUniqueId();
		String message = "";

		if (!useCustom)
		{
			message = _broadcastPrefix + _modBroadcastFormat;
		}
		else
		{
			File f = new File("plugins/OnTheClock16/Players/", uuid + ".clock");
			FileConfiguration tempfc = new YamlConfiguration();
			tempfc.load(f);
			message = _broadcastPrefix + tempfc.getString("BroadcastMessage");
		}

		return formatString(message, name);
	}

	static String formatString(String message, String pName)
	{
		message = message.replaceAll("&([0-9a-f])", "\u00A7$1");
		message = message.replaceAll("&([k-o])", "\u00A7$1");
		message = message.replaceAll("%p", pName);
		return message;
	}

	public static void makeNewClockFile(Player player) throws IOException,
			InvalidConfigurationException
	{
		File f = new File(plugin.clockDir, player.getUniqueId() + ".clock");
		f.createNewFile();

		FileConfiguration tempfc = new YamlConfiguration();
		tempfc.load(f);
		tempfc.addDefault("BroadcastMessage",
				_settings.getString("DefaultFormat"));
		tempfc.addDefault("LastName", player.getName());
		tempfc.options().copyDefaults(true);
		tempfc.save(f);
	}

}
