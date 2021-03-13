package me.zeuss.permissionsplus.managers;

import me.zeuss.permissionsplus.Main;
import me.zeuss.permissionsplus.models.Group;
import me.zeuss.permissionsplus.utilities.ConfigFile;
import me.zeuss.permissionsplus.utilities.task.APIScheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

public class GroupManager implements Manager {

    private ConfigFile file;
    private Main plugin;
    private List<Group> groups;

    public GroupManager(Main plugin, ConfigFile file) {
        this.plugin = plugin;
        this.file = file;
        this.groups = new ArrayList<>();
        load();
    }

    public List<Group> getGroups() {
        return groups;
    }

    public boolean hasGroup(String name) {
        return this.groups.stream().anyMatch(g -> g.getName().equalsIgnoreCase(name));
    }

    public Group getGroup(String name) {
        return this.groups.stream().filter(g -> g.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    @Override
    public Manager load() {
        APIScheduler.startTask(new APIScheduler.TaskRunnable() {
            @Override
            public void run() {
                if (file.exists()) {
                    if (file.hasConfigurationSection("groups")) {
                        HashMap<String, List<String>> gpp = new HashMap<>();
                        file.getConfigurationSection("groups").getKeys(false).forEach(name -> {
                            String path = "groups.".concat(name).concat(".");
                            String prefix = file.getString(path.concat("prefix"));
                            String suffix = file.getString(path.concat("suffix"));
                            List<String> extensions = file.getStringList(path.concat("extensions"));
                            List<String> permissions = file.getStringList(path.concat("permissions"));
                            if (!gpp.containsKey(name)) {
                                gpp.put(name, permissions);
                            }
                            Group g = new Group(name, prefix, suffix, extensions, permissions);
                            extensions.forEach(e -> {
                                if (gpp.containsKey(e)) {
                                    g.addPermissions(gpp.get(e));
                                }
                            });
                            groups.add(g);
                        });
                        gpp.clear();
                        plugin.log(Level.INFO, "[Grupos]", groups.size() + " grupos carregados.");
                    }
                }
                cancel();
            }
        }, 1, APIScheduler.SchedulerUnit.TICK);
        return this;
    }

    @Override
    public void save() {
        try {
            if (file == null || !file.exists()) {
                plugin.log(Level.WARNING, "[Grupos]", "Nenhum arquivo encontrado para salvar.");
                return;
            }
            file.save();
        } catch (Exception ex) {
            plugin.log(Level.SEVERE, "[Grupos]", "Erro ao tentar salvar.\n".concat(ex.getMessage()));
        }
    }
}
