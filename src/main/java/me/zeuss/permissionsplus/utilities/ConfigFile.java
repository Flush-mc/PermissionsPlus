package me.zeuss.permissionsplus.utilities;

import org.bukkit.*;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConfigFile extends YamlConfiguration {

    private File datafolder;
    private File folder;
    private File file;
    private Plugin plugin;
    private String charset = "UTF-8";
    private YamlConfiguration yml;

    public ConfigFile(String filename, Plugin Plugin) {
        this(filename, Plugin, null);
    }

    public ConfigFile(String filename, Plugin Plugin, String Folder) {
        plugin = Plugin;
        datafolder = plugin.getDataFolder();
        folder = Folder != null ? new File(datafolder, Folder) : null;
        if (!datafolder.exists()) {
            datafolder.mkdir();
        }
        if (folder != null && (!folder.exists())) {
            folder.mkdir();
        }
        filename = !filename.contains(".") ? filename.concat(".yml") : filename;
        file = new File(folder != null ? folder.getPath() : datafolder.getPath(), filename);
        saveDefault(false);
        reload();
        save();
    }

    public boolean exists() {
        return file != null && file.exists();
    }

    public boolean save() {
        try {
            yml.save(file);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean reload() {
        try {
            file = new File(folder != null ? folder : datafolder, file.getName());
            InputStreamReader input = new InputStreamReader(new FileInputStream(file), charset);
            yml = YamlConfiguration.loadConfiguration(input);
            return true;
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void saveDefault(boolean replace) {
        if (!file.exists() || replace) {
            String name = folder != null ? folder.getName() + File.separator + file.getName() : file.getName();
            plugin.saveResource(name, true);
        }
    }

    public Set<String> getKeys(boolean deep) {
        return yml.getKeys(deep);
    }

    public Map<String, Object> getValues(boolean deep) {
        return yml.getValues(deep);
    }

    public boolean contains(String path) {
        return yml.contains(path);
    }

    public boolean contains(String path, boolean ignoreDefault) {
        return yml.contains(path, ignoreDefault);
    }

    public boolean isSet(String path) {
        return yml.isSet(path);
    }

    public String getCurrentPath() {
        return yml.getCurrentPath();
    }

    public String getName() {
        return file.getName();
    }

    public Configuration getRoot() {
        return yml.getRoot();
    }

    public ConfigurationSection getParent() {
        return yml.getParent();
    }

    public void addDefault(String path, Object value) {
        yml.addDefault(path, value);
    }

    public ConfigurationSection getDefaultSection() {
        return yml.getDefaultSection();
    }

    public void set(String path, Object value) {
        yml.set(path, value);
    }

    public Object get(String path) {
        return yml.get(path);
    }

    public Object get(String path, Object def) {
        return yml.get(path, def);
    }

    public ConfigurationSection createSection(String path) {
        return yml.createSection(path);
    }

    public ConfigurationSection createSection(String path, Map<?, ?> map) {
        return yml.createSection(path, map);
    }

    public String getString(String path) {
        return yml.getString(path);
    }

    public String getString(String path, String def) {
        return yml.getString(path, def);
    }

    public boolean isString(String path) {
        return yml.isString(path);
    }

    public int getInt(String path) {
        return yml.getInt(path);
    }

    public int getInt(String path, int def) {
        return yml.getInt(path, def);
    }

    public boolean isInt(String path) {
        return yml.isInt(path);
    }

    public boolean getBoolean(String path) {
        return yml.getBoolean(path);
    }

    public boolean getBoolean(String path, boolean def) {
        return yml.getBoolean(path, def);
    }

    public boolean isBoolean(String path) {
        return yml.isBoolean(path);
    }

    public double getDouble(String path) {
        return yml.getDouble(path);
    }

    public double getDouble(String path, double def) {
        return yml.getDouble(path, def);
    }

    public boolean isDouble(String path) {
        return yml.isDouble(path);
    }

    public long getLong(String path) {
        return yml.getLong(path);
    }

    public long getLong(String path, long def) {
        return yml.getLong(path, def);
    }

    public boolean isLong(String path) {
        return yml.isLong(path);
    }

    public List<?> getList(String path) {
        return yml.getList(path);
    }

    public List<?> getList(String path, List<?> def) {
        return yml.getList(path, def);
    }

    public boolean isList(String path) {
        return yml.isList(path);
    }

    public List<String> getStringList(String path) {
        return yml.getStringList(path);
    }

    public List<Integer> getIntegerList(String path) {
        return yml.getIntegerList(path);
    }

    public List<Boolean> getBooleanList(String path) {
        return yml.getBooleanList(path);
    }

    public List<Double> getDoubleList(String path) {
        return yml.getDoubleList(path);
    }

    public List<Float> getFloatList(String path) {
        return yml.getFloatList(path);
    }

    public List<Long> getLongList(String path) {
        return yml.getLongList(path);
    }

    public List<Byte> getByteList(String path) {
        return yml.getByteList(path);
    }

    public List<Character> getCharacterList(String path) {
        return yml.getCharacterList(path);
    }

    public List<Short> getShortList(String path) {
        return yml.getShortList(path);
    }

    public List<Map<?, ?>> getMapList(String path) {
        return yml.getMapList(path);
    }

    public <T extends ConfigurationSerializable> T getSerializable(String path, Class<T> clazz) {
        return yml.getSerializable(path, clazz);
    }

    public <T extends ConfigurationSerializable> T getSerializable(String path, Class<T> clazz, T def) {
        return yml.getSerializable(path, clazz, def);
    }

    public org.bukkit.util.Vector getVector(String path) {
        return yml.getVector(path);
    }

    public org.bukkit.util.Vector getVector(String path, org.bukkit.util.Vector def) {
        return yml.getVector(path, def);
    }

    public boolean isVector(String path) {
        return yml.isVector(path);
    }

    public OfflinePlayer getOfflinePlayer(String path) {
        return yml.getOfflinePlayer(path);
    }

    public OfflinePlayer getOfflinePlayer(String path, OfflinePlayer def) {
        return getOfflinePlayer(path, def);
    }

    public boolean isOfflinePlayer(String path) {
        return yml.isOfflinePlayer(path);
    }

    public ItemStack getItemStack(String path) {
        return yml.getItemStack(path);
    }

    public ItemStack getItemStack(String path, ItemStack def) {
        return yml.getItemStack(path, def);
    }

    public boolean isItemStack(String path) {
        return yml.isItemStack(path);
    }

    public Color getColor(String path) {
        return yml.getColor(path);
    }

    public Color getColor(String path, Color def) {
        return yml.getColor(path, def);
    }

    public boolean isColor(String path) {
        return yml.isColor(path);
    }

    public ConfigurationSection getConfigurationSection(String path) {
        return yml.getConfigurationSection(path);
    }

    public boolean hasConfigurationSection(String path) {
        return getConfigurationSection(path) != null;
    }

    public boolean isConfigurationSection(String path) {
        return yml.isConfigurationSection(path);
    }

    public String toString() {
        return getName();
    }

}
