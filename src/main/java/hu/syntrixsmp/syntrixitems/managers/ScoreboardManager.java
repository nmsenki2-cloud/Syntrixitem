package hu.syntrixsmp.syntrixitems.managers;

import hu.syntrixsmp.syntrixitems.SyntrixItemsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreboardManager {
    private final SyntrixItemsPlugin plugin;
    private final Map<UUID, Scoreboard> scoreboards = new HashMap<>();
    private BukkitTask updateTask;

    public ScoreboardManager(SyntrixItemsPlugin plugin) {
        this.plugin = plugin;
        startUpdateTask();
    }

    private void startUpdateTask() {
        updateTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                updateScoreboard(player);
            }
        }, 20L, 20L); // másodpercenként frissül
    }

    public void updateScoreboard(Player player) {
        BoosterManager boosterManager = plugin.getBoosterManager();

        Scoreboard scoreboard = scoreboards.computeIfAbsent(player.getUniqueId(), uuid -> {
            Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
            return sb;
        });

        Objective objective = scoreboard.getObjective("syntrix");
        if (objective == null) {
            objective = scoreboard.registerNewObjective("syntrix", Criteria.DUMMY,
                    c("&b&lSyntrixSMP"));
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        } else {
            objective.displayName(c("&b&lSyntrixSMP"));
        }

        // Töröljük a régi sorokat
        for (String entry : scoreboard.getEntries()) {
            scoreboard.resetScores(entry);
        }

        int line = 10;

        // Üres sor
        setLine(scoreboard, objective, " ", line--);

        // Booster sor — csak ha aktív
        if (boosterManager.hasActiveBooster(player)) {
            int pps = plugin.getConfig().getInt("shard-booster.points-per-second", 4);
            setLine(scoreboard, objective, c("&b⚡ Booster: &f" + pps + "/mp"), line--);
            setLine(scoreboard, objective, c("&7Hátralévő: &e" + boosterManager.getFormattedRemaining(player)), line--);
            setLine(scoreboard, objective, "  ", line--);
        }

        // Üres sor alul
        setLine(scoreboard, objective, "   ", line--);
        setLine(scoreboard, objective, c("&7syntrixsmp.hu"), line--);

        player.setScoreboard(scoreboard);
    }

    private void setLine(Scoreboard scoreboard, Objective objective, String text, int score) {
        Score s = objective.getScore(text);
        s.setScore(score);
    }

    // Adventure Component helyett sima legacy String — scoreboard csak Stringet fogad el
    private String c(String text) {
        return text.replace("&", "§");
    }

    public void removeScoreboard(Player player) {
        scoreboards.remove(player.getUniqueId());
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
    }

    public void stop() {
        if (updateTask != null) updateTask.cancel();
    }
}
