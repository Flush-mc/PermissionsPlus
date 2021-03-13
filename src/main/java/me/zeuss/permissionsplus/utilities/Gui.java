package me.zeuss.permissionsplus.utilities;

import me.zeuss.permissionsplus.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Gui {

    private String title;
    private int rows;
    private Player player;
    private Inventory inv;
    private HashMap<Integer, ItemStack> itens = new HashMap<>();
    private HashMap<Integer, onAction> actions = new HashMap<>();
    private HashMap<Integer, ActionRules> actionRules = new HashMap<>();
    private HashSet<Integer> blockedSlots = new HashSet<>();
    private HashSet<Navigation> navigations = new HashSet<>();
    private Listener listener;
    private boolean blockInterract = true;

    public Gui(Player player, GuiTemplate template) {
        this.player = player;
        this.rows = template.rows;
        this.title = template.title;
        this.itens = template.itens;
        this.actions = template.actions;
        this.blockedSlots = template.blockedSlots;
        this.navigations = template.navigations;
        this.blockInterract = template.blockInterract;
        this.inv = Bukkit.createInventory(player, 9 * rows, title);
    }

    public Gui(Player player, int rows, String title) {
        this.player = player;
        this.rows = rows;
        this.title = TextUtils.formatText(title);
        this.inv = Bukkit.createInventory(player, 9 * rows, this.title);
    }

    public Gui blockInterract(boolean blockInterract) {
        this.blockInterract = blockInterract;
        return this;
    }

    public Gui setItem(int slot, ItemStack item) {
        this.itens.put(slot, item);
        return this;
    }

    public Gui setItem(int slot, ItemStack item, onAction action) {
        this.itens.put(slot, item);
        setAction(slot, action);
        return this;
    }

    public Gui setItem(int slot, ItemStack item, onAction action, ClickType... types) {
        this.itens.put(slot, item);
        ActionRules ar = new ActionRules(slot, action, types);
        setActionRules(slot, ar);
        return this;
    }

    public Gui setItem(int linha, int coluna, ItemStack item) {
        int slot = ((linha - 1) * 9) + coluna - 1;
        this.itens.put(slot, item);
        return this;
    }

    public Gui setItem(int linha, int coluna, ItemStack item, onAction action) {
        int slot = ((linha - 1) * 9) + coluna - 1;
        setAction(slot, action);
        this.itens.put(slot, item);
        return this;
    }

    public Gui setItem(int linha, int coluna, ItemStack item, onAction action, ClickType... types) {
        int slot = ((linha - 1) * 9) + coluna - 1;
        ActionRules ar = new ActionRules(slot, action, types);
        setActionRules(slot, ar);
        this.itens.put(slot, item);
        return this;
    }

    public Gui fillAllSlotWith(ItemStack item) {
        for (int i = 0; i < rows * 9; i++) {
            setItem(i, item);
        }
        return this;
    }

    public Gui fillAllSlotWith(ItemStack item, onAction action) {
        for (int i = 0; i < rows * 9; i++) {
            setItem(i, item);
            setAction(i, action);
        }
        return this;
    }

    private void setActionRules(int slot, ActionRules ar) {
        actionRules.put(slot, ar);
    }

    public Gui setAction(int slot, onAction action) {
        this.actions.put(slot, action);
        return this;
    }

    public Gui setAction(int linha, int coluna, onAction action) {
        int slot = ((linha - 1) * 9) + coluna - 1;
        this.actions.put(slot, action);
        return this;
    }

    public Gui blockSlotInterract(int slot) {
        this.blockedSlots.add(slot);
        return this;
    }

    public Gui blockSlotInterract(int linha, int coluna) {
        int slot = ((linha - 1) * 9) + coluna - 1;
        this.blockedSlots.add(slot);
        return this;
    }

    public Gui blockSlotInterract(Slot... slots) {
        if (slots != null) {
            Arrays.stream(slots).forEach(s -> this.blockedSlots.add(s.getSlot()));
        }
        return this;
    }

    public Gui blockSlotInterract(int... slots) {
        if (slots != null) {
            Arrays.stream(slots).forEach(s -> this.blockedSlots.add(s));
        }
        return this;
    }

    public Gui setGuiNavigation(int linha, int coluna, Gui gui, ClickType... types) {
        int slot = ((linha - 1) * 9) + coluna - 1;
        this.navigations.add(new Navigation(slot, gui, types));
        return this;
    }

    public Gui setGuiNavigation(int linha, int coluna, Gui gui, boolean condition, ClickType... types) {
        int slot = ((linha - 1) * 9) + coluna - 1;
        this.navigations.add(new Navigation(slot, gui, condition, types));
        return this;
    }

    public void openGui() {
        build();
        this.player.openInventory(this.inv);
        startListener();
    }

    public void openGui(Sound sound) {
        this.player.playSound(this.player.getLocation(), sound, 1f, 1f);
        openGui();
    }

    public Gui updateSlot(int linha, int coluna, ItemStack item) {
        int slot = ((linha - 1) * 9) + coluna - 1;
        if (itens.containsKey(slot)) {
            if (itens.get(slot).getType() == item.getType()) {
                setItem(linha, coluna, item);
            }
        }
        build();
        this.player.updateInventory();
        return this;
    }

    public Gui update() {
        build();
        this.player.updateInventory();
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    protected Inventory build() {
        if (itens.size() > 0) {
            itens.forEach((slot, item) -> inv.setItem(slot, item));
        }
        return inv;
    }

    protected void startListener() {
        this.listener = new Listener() {
            @EventHandler(ignoreCancelled = true)
            public void onClose(InventoryCloseEvent e) {
                if (e.getPlayer() != null) {
                    Player p = (Player) e.getPlayer();
                    if (p.getUniqueId().equals(player.getUniqueId())) {
                        HandlerList.unregisterAll(listener);
                    }
                }
            }
            @EventHandler(ignoreCancelled = true)
            public void onClick(InventoryClickEvent e) {
                if (!(e.getWhoClicked() instanceof Player)) return;
                Player p = (Player) e.getWhoClicked();
                if (!p.getUniqueId().equals(player.getUniqueId())) return;
                if (e.getRawSlot() < 0) return;
                Inventory inv = p.getOpenInventory().getTopInventory();
                if (inv == null) return;
                if ((e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) && e.getClick() != ClickType.NUMBER_KEY) return;

                if (blockInterract) {
                    e.setCancelled(true);
                    e.setResult(Event.Result.DENY);
                    p.updateInventory();
                } else {
                    if (blockedSlots.size() > 0) {
                        if (blockedSlots.contains(e.getRawSlot())) {
                            e.setCancelled(true);
                            e.setResult(Event.Result.DENY);
                            p.updateInventory();
                        }
                    }
                }

                AtomicBoolean ab = new AtomicBoolean();

                if (navigations.size() > 0) {
                    navigations.forEach(n -> {
                        if (n.getSlot() == e.getRawSlot()) {
                            Arrays.stream(n.getClickType()).forEach(clickType -> {
                                if (clickType == e.getClick()) {
                                    if (n.hasCondition()) {
                                        n.getGUI().openGui(Sound.BLOCK_COMPARATOR_CLICK);
                                        ab.set(true);
                                    }
                                }
                            });
                        }
                    });
                }

                if (ab.get()) {
                    ab.set(false);
                    return;
                }

                if (actionRules.size() > 0 && actionRules.containsKey(e.getRawSlot())) {
                    ActionRules ar = actionRules.get(e.getRawSlot());
                    Optional<ClickType> result = Arrays.stream(ar.getClickType()).filter(ct -> ct == e.getClick()).findAny();
                    if (result.isPresent()) {
                        ar.getEvent().onAction(new ClickActionEvent(p, inv, e.getCurrentItem(), e.getRawSlot(), e.getClick()));
                        return;
                    }
                }

                if (actions.size() > 0 && actions.containsKey(e.getRawSlot())) {
                    onAction action = actions.get(e.getRawSlot());
                    action.onAction(new ClickActionEvent(p, inv, e.getCurrentItem(), e.getRawSlot(), e.getClick()));
                }
            }
        };
        Bukkit.getPluginManager().registerEvents(this.listener, Main.getInstance());
    }

    public static class PageableGui extends Gui {

        private double itensPerPage;
        private ArrayList<ItemStack> itens = new ArrayList<>();
        private ArrayList<Integer> availableSlots = new ArrayList<>();
        private HashMap<Player, Integer> playerPage = new HashMap<>();
        private Player player;
        private boolean vanishButton = true;
        private int previousSlot, nextSlot;

        public PageableGui(Player player, GuiTemplate template) {
            super(player, template);
            this.player = player;
            this.playerPage.put(player, 0);
        }

        public PageableGui(Player player, int rows, String title) {
            super(player, rows, title);
            this.player = player;
            this.playerPage.put(player, 0);
        }

        public PageableGui setItens(List<ItemStack> itens) {
            this.itens.addAll(itens);
            return this;
        }

        public PageableGui setAvailableSlots(Integer... slots) {
            if (this.availableSlots.size() > 0) {
                this.availableSlots.clear();
            }
            this.itensPerPage = slots.length;
            this.availableSlots.addAll(Arrays.asList(slots));
            return this;
        }

        public PageableGui setInteraction(onAction action) {
            this.availableSlots.forEach(s -> {
                setAction(s, action);
            });
            return this;
        }

        public PageableGui setPreviousPageSlot(int slot, ItemStack item) {
            this.previousSlot = slot;
            setItem(slot, item, e -> update(false));
            return this;
        }

        public PageableGui setPreviousPageSlot(int linha, int coluna, ItemStack item) {
            int slot = ((linha - 1) * 9) + coluna - 1;
            return setPreviousPageSlot(slot, item);
        }

        public PageableGui setNextPageSlot(int slot, ItemStack item) {
            this.nextSlot = slot;
            setItem(slot, item, e -> update(true));
            return this;
        }

        public PageableGui setNextPageSlot(int linha, int coluna, ItemStack item) {
            int slot = ((linha - 1) * 9) + coluna - 1;
            return setNextPageSlot(slot, item);
        }

        private void update(boolean nextPage) {
            int atual = playerPage.get(player);
            double totalPages = (itens.size() / itensPerPage);
            if ((atual - 1) < 0 && !nextPage) return;
            if (nextPage && (atual + 1) >= totalPages) return;
            if (nextPage) {
                playerPage.put(player, atual + 1);
            } else {
                playerPage.put(player, atual - 1);
            }
            openGui();
            player.updateInventory();
        }

        public PageableGui showPageButtons() {
            this.vanishButton = false;
            return this;
        }

        protected Inventory pageBuild() {
            Inventory inv = build();

            int init = (int)itensPerPage * (playerPage.get(player) + 1) - (int)itensPerPage;
            int end = (int)itensPerPage * (playerPage.get(player) + 1);

            int count = 0;

            for (int i = init; i < end; i++) {
                int slot = availableSlots.get(count++);
                inv.setItem(slot, null);
                if (i < itens.size()) {
                    ItemStack item = itens.get(i);
                    inv.setItem(slot, item);
                }
            }

            if (vanishButton) {
                double totalPages = (itens.size() / itensPerPage);
                if (playerPage.get(player) - 1 < 0) {
                    inv.setItem(this.previousSlot, null);
                }
                if ((playerPage.get(player) + 1) >= totalPages) {
                    inv.setItem(this.nextSlot, null);
                }
            }
            return inv;
        }

        public void openGui() {
            Inventory inv = pageBuild();
            this.player.openInventory(inv);
            super.startListener();
        }

        public void openGui(Sound sound) {
            this.player.playSound(this.player.getLocation(), sound, 1f, 1f);
            openGui();
        }

    }

    public static class GuiTemplate {

        private String title;
        private int rows;
        private Inventory inventory;
        private HashMap<Integer, ItemStack> itens = new HashMap<>();
        private HashMap<Integer, onAction> actions = new HashMap<>();
        private HashSet<Integer> blockedSlots = new HashSet<>();
        private HashSet<Navigation> navigations = new HashSet<>();
        private HashMap<Integer, ActionRules> actionRules = new HashMap<>();
        private boolean blockInterract = true;

        public GuiTemplate(int rows, String title) {
            this.rows = rows;
            this.title = TextUtils.formatText(title);
        }

        public GuiTemplate blockInterract(boolean blockInterract) {
            this.blockInterract = blockInterract;
            return this;
        }

        public GuiTemplate setItem(int slot, ItemStack item) {
            this.itens.put(slot, item);
            return this;
        }

        public GuiTemplate setItem(int slot, ItemStack item, onAction action) {
            this.itens.put(slot, item);
            setAction(slot, action);
            return this;
        }

        public GuiTemplate setItem(int slot, ItemStack item, onAction action, ClickType... types) {
            this.itens.put(slot, item);
            ActionRules ar = new ActionRules(slot, action, types);
            setActionRules(slot, ar);
            return this;
        }

        public GuiTemplate setItem(int linha, int coluna, ItemStack item) {
            int slot = ((linha - 1) * 9) + coluna - 1;
            this.itens.put(slot, item);
            return this;
        }

        public GuiTemplate setItem(int linha, int coluna, ItemStack item, onAction action) {
            int slot = ((linha - 1) * 9) + coluna - 1;
            setAction(slot, action);
            this.itens.put(slot, item);
            return this;
        }

        public GuiTemplate setItem(int linha, int coluna, ItemStack item, onAction action, ClickType... types) {
            int slot = ((linha - 1) * 9) + coluna - 1;
            ActionRules ar = new ActionRules(slot, action, types);
            setActionRules(slot, ar);
            this.itens.put(slot, item);
            return this;
        }

        public GuiTemplate fillAllSlotWith(ItemStack item) {
            for (int i = 0; i < rows * 9; i++) {
                setItem(i, item);
            }
            return this;
        }

        public GuiTemplate fillAllSlotWith(ItemStack item, onAction action) {
            for (int i = 0; i < rows * 9; i++) {
                setItem(i, item);
                setAction(i, action);
            }
            return this;
        }

        private void setActionRules(int slot, ActionRules ar) {
            actionRules.put(slot, ar);
        }

        public GuiTemplate setAction(int slot, onAction action) {
            this.actions.put(slot, action);
            return this;
        }

        public GuiTemplate setAction(int linha, int coluna, onAction action) {
            int slot = ((linha - 1) * 9) + coluna - 1;
            this.actions.put(slot, action);
            return this;
        }

        public GuiTemplate blockSlotInterract(int slot) {
            this.blockedSlots.add(slot);
            return this;
        }

        public GuiTemplate blockSlotInterract(int linha, int coluna) {
            int slot = ((linha - 1) * 9) + coluna - 1;
            this.blockedSlots.add(slot);
            return this;
        }

        public GuiTemplate blockSlotInterract(Slot... slots) {
            if (slots != null) {
                Arrays.stream(slots).forEach(s -> this.blockedSlots.add(s.getSlot()));
            }
            return this;
        }

        public GuiTemplate blockSlotInterract(int... slots) {
            if (slots != null) {
                Arrays.stream(slots).forEach(s -> this.blockedSlots.add(s));
            }
            return this;
        }

        public GuiTemplate setGuiNavigation(int linha, int coluna, Gui gui, ClickType... types) {
            int slot = ((linha - 1) * 9) + coluna - 1;
            this.navigations.add(new Navigation(slot, gui, types));
            return this;
        }

        public GuiTemplate setGuiNavigation(int linha, int coluna, Gui gui, boolean condition, ClickType... types) {
            int slot = ((linha - 1) * 9) + coluna - 1;
            this.navigations.add(new Navigation(slot, gui, condition, types));
            return this;
        }

    }

    public static class ActionRules {

        private int slot;
        private onAction event;
        private ClickType[] clickType;
        private boolean condition;
        private boolean hasCondition = false;

        public ActionRules(int slot, onAction event, ClickType... type) {
            this.slot = slot;
            this.event = event;
            this.clickType = type;
        }

        public ActionRules(int slot, onAction event, boolean condition, ClickType... type) {
            this.slot = slot;
            this.clickType = type;
            this.event = event;
            this.condition = condition;
            this.hasCondition = true;
        }

        public onAction getEvent() {
            return event;
        }

        public boolean hasCondition() {
            return !this.hasCondition || this.condition;
        }

        public ClickType[] getClickType() {
            return this.clickType;
        }

        public int getSlot() {
            return this.slot;
        }

    }

    public static class Navigation {

        private int slot;
        private ClickType[] clickType;
        private Gui gui;
        private boolean condition;
        private boolean hasCondition = false;

        public Navigation(int slot, Gui gui, ClickType... type) {
            this.slot = slot;
            this.clickType = type;
            this.gui = gui;
        }

        public Navigation(int slot, Gui gui, boolean condition, ClickType... type) {
            this.slot = slot;
            this.clickType = type;
            this.gui = gui;
            this.condition = condition;
            this.hasCondition = true;
        }

        public boolean hasCondition() {
            return !this.hasCondition || this.condition;
        }

        public Gui getGUI() {
            return this.gui;
        }

        public ClickType[] getClickType() {
            return this.clickType;
        }

        public int getSlot() {
            return this.slot;
        }

    }

    public static class Slot {

        private int linha;
        private int coluna;
        private int line_size = 9;
        private int slot;

        public Slot(int slot) {
            this.slot = slot;
        }

        public Slot(int linha, int coluna) {
            this.linha = linha;
            this.coluna = coluna;
            slot = (((linha - 1) * line_size) + coluna - 1);
        }

        public int getSlot() {
            return this.slot;
        }

    }

    public interface onAction {
        void onAction(ClickActionEvent e);
    }

    public static class ClickActionEvent {

        private Player player;
        private Inventory inventory;
        private ItemStack item;
        private ClickType clickType;
        private int slot;

        public ClickActionEvent(Player player, Inventory inventory, ItemStack item, int slot, ClickType clickType) {
            this.player = player;
            this.inventory = inventory;
            this.item = item;
            this.slot = slot;
            this.clickType = clickType;
        }

        public ClickType getClickType() {
            return this.clickType;
        }

        public int getSlot() {
            return this.slot;
        }

        public ItemStack getItem() {
            return this.item;
        }

        public Inventory getInventory() {
            return this.inventory;
        }

        public Player getPlayer() {
            return this.player;
        }

    }

}
