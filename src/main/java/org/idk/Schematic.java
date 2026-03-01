package org.idk;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.File;
import java.io.FileInputStream;

public final class Schematic {

    public static void onFalse() {
        Bukkit.broadcastMessage("§d" + "Schematic automatic pasting canceled. 건축물 자동 복구 취소됨.");
    }

    public static void runOnce(String name) {
        if (!paste(name)) {
            Bukkit.broadcastMessage(ChatColor.RED + "Error while pasting blocks. Please check the server logs. 건축물 복구 오류. 서버장의 서버 기록을 확인해주세요.");
        }
        else Bukkit.broadcastMessage("§d" + "Schematic " + name + "pasted. 건축물 복구됨.");
    }

    public static boolean paste(String name)
    {
        File file = new File("plugins/WorldEdit/schematics/" + name);


        ClipboardFormat format = ClipboardFormats.findByFile(file);

        try(ClipboardReader reader = format.getReader(new FileInputStream(file))) {

            Clipboard clipboard = reader.read();

            BukkitWorld world = new BukkitWorld(Bukkit.getWorld("world"));

            try (EditSession editSession = WorldEdit.getInstance().newEditSession(world)) {
                Operation operation = new ClipboardHolder(clipboard)
                        .createPaste(editSession)
                        .to(clipboard.getOrigin())
                        .build();
                Operations.complete(operation);
            }

            return true;

        } catch (Exception e){
            e.printStackTrace();
            return false;
    }
    }
}
