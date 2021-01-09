package com.pro_crafting.mc.wg.kit;

import com.pro_crafting.mc.wg.kit.plugins.EssentialsProvider;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;

import java.util.ArrayList;
import java.util.List;

public class KitAPI
{

	private static KitAPI instance;
	private List<KitProvider> kitProviders;
	private ServicesManager sm;

	public KitAPI() {
		instance = this;
	}

	public static KitAPI getInstance() {
		if (instance == null) {
			instance = new KitAPI();
		}
		return instance;
	}

	public void load() {
		this.kitProviders = new ArrayList<KitProvider>();
		this.sm = Bukkit.getServicesManager();
		this.loadKitPlugins();
	}

	private void loadKitPlugins()
	{
		hookKitPlugin("Essentials", EssentialsProvider.class);
	}
	
	private void hookKitPlugin(String name, Class<? extends KitProvider> hookClass)
	{
		Plugin plugin = Bukkit.getPluginManager().getPlugin(name);
		if (Bukkit.getPluginManager().getPlugin(name) != null)
		{
			try {
				KitProvider instance = hookClass.getConstructor().newInstance();
				this.kitProviders.add(instance);
				this.sm.register(KitProvider.class, instance, plugin, ServicePriority.Normal);
			} catch (Exception ex) {
				
			}
		}
	}
}
