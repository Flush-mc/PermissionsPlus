package me.zeuss.permissionsplus.utilities;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class IBuilder {

    private ItemStack item;
    private Material mat;
    private String name;
    private List<String> lore = new ArrayList<>();
    private Map<Enchantment, Integer> enchants = new HashMap<>();
    private Integer amount = 1;
    private short damage = 0;
    private String p;

    public IBuilder(ItemStack item) {
        if (item != null) {
            this.item = item;
            this.mat = item.getType();
            this.amount = item.getAmount();
            this.damage = item.getDurability();
            if (item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                if (meta.hasDisplayName()) {
                    this.name = meta.getDisplayName();
                }
                if (meta.hasLore()) {
                    this.lore.addAll(meta.getLore());
                }
                if (meta.hasEnchants()) {
                    this.enchants = meta.getEnchants();
                }
                if (item.getType() == Material.PLAYER_HEAD) {
                    this.p = ((SkullMeta) meta).getOwner();
                }
            }
        }
    }

    //   minecraft:diamond!&!1!&!-n;batata teste!&!-l;teasdas asdasd!&!-l;asdas!&!-en;minecraft:sharpness@1!&!-f:hide_enchants

    public IBuilder(String input) {
        if (input == null) return;
        ArrayList<String> args = new ArrayList<>(Arrays.asList(input.split("!&!")));
        String item_name = args.get(0);
        amount = TextUtils.isInt(args.get(1)) ? Integer.parseInt(args.get(1)) : 1;
        args.remove(0);
        args.remove(0);
        if (Material.matchMaterial(item_name) != null || Material.matchMaterial("minecraft:".concat(item_name)) != null) {
            item = new ItemStack(Objects.requireNonNull(Material.matchMaterial(item_name) != null ? Material.matchMaterial(item_name) : Material.matchMaterial("minecraft:".concat(item_name))));
            mat = item.getType();
            args.forEach(arg -> {
                if (arg.contains(";")) {
                    String key = arg.split(";")[0];
                    String value = arg.split(";")[1];
                    switch (key.toLowerCase()) {
                        case "-n":
                            name = TextUtils.formatText(value);
                            break;
                        case "-l":
                            add(value);
                            break;
                        case "-e":
                            if (value.contains(":")) {
                                String enchant_key = value.split(":")[0];
                                String enchant_value = value.split(":")[0];
                                if (enchant_value.contains("@")) {
                                    String enchant_name = enchant_value.split("@")[0];
                                    String enchant_lvl = enchant_value.split("@")[1];
                                    if (TextUtils.isInt(enchant_lvl)) {
                                        addEnchant(Enchantment.getByKey(new NamespacedKey(enchant_key, enchant_name)), Integer.parseInt(enchant_lvl));
                                    } else {
                                        addEnchant(Enchantment.getByKey(new NamespacedKey(enchant_key, enchant_name)), 1);
                                    }
                                } else {
                                    addEnchant(Enchantment.getByKey(new NamespacedKey(enchant_key, enchant_value)), 1);
                                }
                            } else {
                                addEnchant(Enchantment.getByKey(NamespacedKey.minecraft(value)), 1);
                            }
                            break;
//                    case "-f":
//                        switch (value.toLowerCase()) {
//                            case "hide_flags":
//                            case "hideflags":
//                            case "hideflag":
//                                item.getItemMeta().addItemFlags(ItemFlag.);
//                        }
                    }
                }
            });
            this.item = this.build();
        }
    }

    public IBuilder(Material mat) {
        this.mat = mat;
    }

    public IBuilder(Material mat, short damage) {
        this.mat = mat;
        this.damage = damage;
    }

    public IBuilder(Material mat, int damage, String name) {
        this.mat = mat;
        this.damage = (short) damage;
        this.name(name);
    }

    public IBuilder(Material mat, int damage, int amount, String name) {
        this.mat = mat;
        this.damage = (short) damage;
        this.amount = amount;
        this.name(name);
    }

    public IBuilder amount(Integer amount) {
        this.amount = amount;
        return this;
    }

    public IBuilder name(String name) {
        this.name = ChatColor.translateAlternateColorCodes('&', name);
        return this;
    }

    public IBuilder add(String lore) {
        this.lore.add(ChatColor.translateAlternateColorCodes('&', lore));
        return this;
    }

    public IBuilder lore(String[] lore) {
        this.lore.clear();
        for (String l : lore) {
            this.lore.add(ChatColor.translateAlternateColorCodes('&', l));
        }
        return this;
    }

    public IBuilder lore(ArrayList<String> lore) {
        this.lore.clear();
        for (String l : lore) {
            this.lore.add(ChatColor.translateAlternateColorCodes('&', l));
        }
        return this;
    }

    public IBuilder resetLore() {
        this.lore.clear();
        return this;
    }

    public IBuilder damage(int damage) {
        this.damage = (short) damage;
        return this;
    }

    public IBuilder owner(String owner) {
        this.p = owner;
        this.damage = 3;
        return this;
    }

    public IBuilder addEnchant(Enchantment enchant, int lvl) {
        this.enchants.put(enchant, lvl);
        return this;
    }

    public boolean isSimilarType(ItemStack item_2) {
        if (item == null || item_2 == null) return false;
        if (item.getType() == item_2.getType()) {
            if (item.getDurability() == item_2.getDurability()) {
                return true;
            }
        }
        return false;
    }

    public boolean isSimilar(ItemStack item_2) {
        if (item == null || item_2 == null) return false;
        item = item.clone();
        item_2 = item_2.clone();
        item.setAmount(1);
        item_2.setAmount(1);

        if (item.getType() != item_2.getType()) return false;
        if (item.getDurability() != item_2.getDurability()) return false;
        if ((item.hasItemMeta() && !item_2.hasItemMeta()) || (!item.hasItemMeta() && item_2.hasItemMeta()))
            return false;

        if (item.getItemMeta().hasEnchants() && !item_2.getItemMeta().hasLore()) return false;
        if (!item.getItemMeta().hasEnchants() && item_2.getItemMeta().hasLore()) return false;

        if (item.getItemMeta().hasLore() && !item_2.getItemMeta().hasLore()) return false;
        if (!item.getItemMeta().hasLore() && item_2.getItemMeta().hasLore()) return false;

        if (item.getItemMeta().hasDisplayName() && !item_2.getItemMeta().hasDisplayName()) return false;
        if (!item.getItemMeta().hasDisplayName() && item_2.getItemMeta().hasDisplayName()) return false;

        if (item.hasItemMeta()) {
            ItemMeta meta_1 = item.getItemMeta(), meta_2 = item_2.getItemMeta();

            if (meta_1.hasDisplayName()) {
                if (!meta_1.getDisplayName().equals(meta_2.getDisplayName())) return false;
            }

            if (meta_1.hasLore()) {
                if (meta_1.getLore().size() != meta_2.getLore().size()) return false;
                if (!meta_1.getLore().equals(meta_2.getLore())) return false;
            }

            if (meta_1.hasEnchants()) {
                if (meta_1.getEnchants().size() != meta_2.getEnchants().size()) return false;
                if (!meta_1.getEnchants().equals(meta_2.getEnchants())) return false;
            }
        }

        return true;
    }

    public ItemStack build() {
        ItemStack item = new ItemStack(mat);
        item.setAmount(amount);
        item.setDurability(damage);
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        if (name != null) {
            meta.setDisplayName(name);
        }

        if (lore != null && !lore.isEmpty()) {
            meta.setLore(lore);
        }

        if (p != null && item.getType() == Material.PLAYER_HEAD) {
            ((SkullMeta) meta).setOwner(p);
        }

        item.setItemMeta(meta);

        if (this.enchants.size() > 0) {
            for (Enchantment e : enchants.keySet()) {
                item.addUnsafeEnchantment(e, enchants.get(e));
            }
        }
        return item;
    }

    public IBuilder clone() {
        return new IBuilder(build().clone());
    }

    public String toString() {
        if (item == null) {
            item = build();
        }
        if (item != null) {
            StringBuilder serializedItem = new StringBuilder();
            serializedItem.append(item.getType().getKey()).append("!&!");
            serializedItem.append(item.getAmount()).append("!&!");
            if (item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                if (meta.hasDisplayName()) {
                    serializedItem.append("-n;").append(meta.getDisplayName().replace("§", "&")).append("!&!");
                }
                if (meta.hasLore()) {
                    meta.getLore().forEach(line -> {
                        serializedItem.append("-l;").append(line.replace("§", "&")).append("!&!");
                    });
                }
            }
            return serializedItem.toString();
        } else {
            return null;
        }
    }

}
