package org.idk;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main extends JavaPlugin implements CommandExecutor, TabCompleter {
    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "IDK has been enabled.");

        getCommand("resetfield").setExecutor(new Main());
        getCommand("resetfield").setTabCompleter(new Main());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (args[0]) {
            case "on" -> runSchematic(args[1]);
            case "off" -> Schematic.onFalse();
            case "runonce" -> Schematic.runOnce(args[1]);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        ArrayList<String> strings = new ArrayList<>();

        switch (args.length) {
            case 1 -> strings.addAll(Arrays.asList("on", "off", "runonce"));
            case 2 -> strings.addAll(Arrays.asList(fileList()));
        }
        return strings;
    }

    public String[] fileList() {
        File file = new File("plugins/WorldEdit/schematics/");

        return file.list();
    }

    public void runSchematic(String name) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                if (!Schematic.paste(name)) {
                    Bukkit.broadcastMessage(ChatColor.RED + "Error while pasting blocks. Please check the server logs. 건축물 복구 오류. 서버장의 서버 기록을 확인해주세요.");
                }
                else Bukkit.broadcastMessage("§d" + "Schematic " + name + "pasted. 건축물 복구됨.");
            }
        }, 0L, 60L);
        Bukkit.broadcastMessage("§d" + "Schematic " + name + "is going to be pasted every 15 minutes. 건축물 15분마다 자동 복구 시작.");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "IDK has been disabled.");
    }
}
