package ml.jadss.jadgens;

import ml.jadss.jadgens.commands.JadGensCommand;
import ml.jadss.jadgens.commands.TabCompleter;
import ml.jadss.jadgens.listeners.*;
import ml.jadss.jadgens.management.DataFile;
import ml.jadss.jadgens.management.MetricsLite;
import ml.jadss.jadgens.tasks.ProduceMachineDelayTask;
import ml.jadss.jadgens.utils.PlaceHolders;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class JadGens extends JavaPlugin {

    private DataFile dataFile;
    public DataFile getDataFile() {
        return dataFile;
    }

    //hook booleans.
    private boolean hookedVault = false;
    public boolean isHookedVault() {
        return hookedVault;
    }

    private boolean hookedPlaceHolderAPI = false;
    public boolean isHookedPlaceHolderAPI() {
        return hookedPlaceHolderAPI;
    }

    private boolean hookedPlayerPoints = false;
    public boolean isHookedPlayerPoints() {
        return hookedPlayerPoints;
    }

    //hooks
    private Economy eco;
    public Economy getEco() {
        return eco;
    }

    private PlayerPointsAPI pointsAPI;
    public PlayerPointsAPI getPointsAPI() {
        return pointsAPI;
    }

    private MetricsLite metrics;

    //tasks
    private BukkitTask task;
    public BukkitTask getTask() {
        return task;
    }
    public void setTask(BukkitTask t) {
        task = t;
    }

    private static JadGens instance;
    public static JadGens getInstance() {
        return instance;
    }

    //Compatibility Mode
    public boolean compatibilityMode = false;
    public boolean getCompMode() { return compatibilityMode; }

    @Override
    public void onEnable() {
        if (getServer().getBukkitVersion().equalsIgnoreCase("1.7.10-R0.1-SNAPSHOT")) {
            compatibilityMode = true;
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &bEnabling &3&lCompatibility Mode&e..."));
        }

        getConfig().options().copyDefaults();
        saveDefaultConfig();
        reloadConfig();


        hookVault();
        hookPlaceHolderAPI();
        hookPlayerPoints();

        if (hookedVault) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &eHooked into &bVault&e!"));
        }
        if (hookedPlaceHolderAPI) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &eHooked into &bPlaceHolderAPI&e!"));
        }
        if (hookedPlayerPoints) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &eHooked into &bPlayerPoints&e!"));
        }

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &eHooked into &bXP&e!"));

        instance = this;

        if (!setupShop()) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        dataFile = new DataFile();
        dataFile.setupDataFile();

        task = new ProduceMachineDelayTask().runTaskTimer(this, 0L, getConfig().getLong("machinesConfig.machinesDelay") * 20);

        setupAPIDebug();
        registerStuff();

        if (!getCompMode()) { metrics = new MetricsLite(this, 8789); }

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &3Plugin &bEnabled&7!"));
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &bDisabling &3Plugin&e!"));
    }


    //API STUFF
    private boolean APIDebug;

    public void setupAPIDebug() {
        APIDebug = getConfig().getBoolean("messages.debugAPI");
    }

    public boolean isAPIDebugEnabled() {
        if (this.getConfig().getBoolean("messages.debugAPI") != APIDebug) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &eA plugin tried to disable API Debug, but it was reverted."));
            APIDebug = getConfig().getBoolean("messages.debugAPI");
            return APIDebug;
        } else {
            return APIDebug;
        }
    }
    //END OF API STUFF


    //Other stuff
    private void registerStuff() {
        //Register Commands
        getCommand("JadGens").setExecutor(new JadGensCommand());
        getCommand("JadGens").setTabCompleter(new TabCompleter());

        //Register Listeners
        getServer().getPluginManager().registerEvents(new PlayerBuildListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerBreakListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        getServer().getPluginManager().registerEvents(new OpenGuiListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new BlockExplodeListener(), this);
        getServer().getPluginManager().registerEvents(new EntityExplodeEvent(), this);
        getServer().getPluginManager().registerEvents(new PistonMoveListener(), this);
        getServer().getPluginManager().registerEvents(new ShopListeners(), this);
    }

    //SETUP SHOP STUFF
    private boolean setupShop() {
        if (getConfig().getBoolean("shop.enabled")) {
            for (String key : getConfig().getConfigurationSection("machines").getKeys(false)) {
                if (getConfig().getString("machines." + key + ".shop.currency").equalsIgnoreCase("ECO")) {
                    if (!hookedVault) {
                        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &eCouldn't &b&lstartup &ebecause, a &3machine currency &eis &a\"ECO\"&e, but &3&lVault &cwasn't &afound&e!"));
                        return false;
                    }
                }
                if (getConfig().getString("machines." + key + ".shop.currency").equalsIgnoreCase("POINTS")) {
                    if (!hookedPlayerPoints) {
                        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &eCouldn't &b&lstartup &ebecause, a &3machine currency &eis &a\"POINTS\"&e, but &3&lPlayerPoints &cwasn't &afound&e!"));
                        return false;
                    }
                }
            }
            for (String key : getConfig().getConfigurationSection("fuels").getKeys(false)) {
                if (getConfig().getString("fuels." + key + ".shop.currency").equalsIgnoreCase("ECO")) {
                    if (!hookedVault) {
                        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &eCouldn't &b&lstartup &ebecause, a &3machine currency &eis &a\"ECO\"&e, but &3&lVault &cwasn't &afound&e!"));
                        return false;
                    }
                }
                if (getConfig().getString("machines." + key + ".shop.currency").equalsIgnoreCase("POINTS")) {
                    if (!hookedPlayerPoints) {
                        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &eCouldn't &b&lstartup &ebecause, a &3machine currency &eis &a\"POINTS\"&e, but &3&lPlayerPoints &cwasn't &afound&e!"));
                        return false;
                    }
                }
            }
        } else if (!getConfig().getBoolean("shop.enabled")) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JadGens &7>> &3Shop &eis &cDisabled&e!"));
            return true;
        }
        return true;
    }


    //Hooks
    private void hookVault() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            hookedVault = false;
            return;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            hookedVault = false;
            return;
        }
        eco = rsp.getProvider();
        hookedVault = true;
    }

    private void hookPlayerPoints() {
        if (getServer().getPluginManager().getPlugin("PlayerPoints") == null) {
            hookedPlayerPoints = false;
            return;
        }
        pointsAPI = new PlayerPointsAPI((PlayerPoints) getServer().getPluginManager().getPlugin("PlayerPoints"));
        hookedPlayerPoints = true;
    }

    private void hookPlaceHolderAPI() {
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") == null) {
            return;
        }
        hookedPlaceHolderAPI = true;
        new PlaceHolders().register();
    }
}