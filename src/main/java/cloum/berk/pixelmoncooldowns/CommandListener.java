package cloum.berk.pixelmoncooldowns;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CommandListener implements Listener {

    final private ConcurrentHashMap<Data, Long> cooldownMap;

    public CommandListener() {
        cooldownMap = new ConcurrentHashMap<>();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent event) {

        Player player = event.getPlayer();
        String commandName = event.getMessage().split(" ")[0];
        Data data = new Data(player.getUniqueId(), commandName);

        if (event.getMessage().contains("breed") || event.getMessage().contains("hatch")) {

            if (!cooldownMap.containsKey(data) || System.currentTimeMillis() - cooldownMap.get(data) > PixelmonCooldowns.cooldown) {
                // off cooldown
                cooldownMap.put(data, System.currentTimeMillis());
            } else {
                // on cooldown
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + PixelmonCooldowns.message);
            }
        }

    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        cooldownMap.forEach((data, timeout) -> {
            if (data.getUuid().equals(event.getPlayer().getUniqueId())) {
                cooldownMap.remove(data);
            }
        });
    }

    public static class Data {

        private final UUID uuid;
        private final String command;

        public Data(UUID uuid, String command) {
            this.uuid = uuid;
            this.command = command;
        }

        public UUID getUuid() {
            return uuid;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Data data = (Data) o;
            if (!uuid.equals(data.uuid)) return false;
            return command.equals(data.command);
        }

        @Override
        public int hashCode() {
            int result = uuid.hashCode();
            result = 31 * result + command.hashCode();
            return result;
        }

    }

}
