package de.javakara.manf.ichq;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

public class IchQ extends JavaPlugin {
	private IchQCommands IchQCMD;
	
	@Override
	public void onEnable() {
		if (getServer().getPluginManager().getPlugin("Vault") != null) {
		}
		try {
			Config.initialise(getConfig(), getDataFolder());
			Config.load();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
		registerCommands();
	}

	private void registerCommands() {
		IchQCMD = new IchQCommands();
		getCommand("quiz").setExecutor(IchQCMD);	
		getCommand("a").setExecutor(IchQCMD);
		getCommand("b").setExecutor(IchQCMD);
	}
}
