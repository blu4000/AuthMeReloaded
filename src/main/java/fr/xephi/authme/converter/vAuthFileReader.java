package fr.xephi.authme.converter;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.cache.auth.PlayerAuth;
import fr.xephi.authme.datasource.DataSource;

/**
 */
public class vAuthFileReader {

    public AuthMe plugin;
    public DataSource database;
    public CommandSender sender;

    /**
     * Constructor for vAuthFileReader.
     * @param plugin AuthMe
     * @param sender CommandSender
     */
    public vAuthFileReader(AuthMe plugin, CommandSender sender) {
        this.plugin = plugin;
        this.database = plugin.database;
        this.sender = sender;
    }

    /**
     * Method convert.
    
     * @throws IOException */
    public void convert() throws IOException {
        final File file = new File(plugin.getDataFolder().getParent() + "" + File.separator + "vAuth" + File.separator + "passwords.yml");
        Scanner scanner;
        try {
            scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String name = line.split(": ")[0];
                String password = line.split(": ")[1];
                PlayerAuth auth;
                if (isUUIDinstance(password)) {
                    String pname;
                    try {
                        pname = Bukkit.getOfflinePlayer(UUID.fromString(name)).getName();
                    } catch (Exception | NoSuchMethodError e) {
                        pname = getName(UUID.fromString(name));
                    }
                    if (pname == null)
                        continue;
                    auth = new PlayerAuth(pname.toLowerCase(), password, "127.0.0.1", System.currentTimeMillis(), "your@email.com", pname);
                } else {
                    auth = new PlayerAuth(name.toLowerCase(), password, "127.0.0.1", System.currentTimeMillis(), "your@email.com", name);
                }
                database.saveAuth(auth);
            }
        } catch (Exception e) {
            ConsoleLogger.writeStackTrace(e);
        }

    }

    /**
     * Method isUUIDinstance.
     * @param s String
    
     * @return boolean */
    private boolean isUUIDinstance(String s) {
        if (String.valueOf(s.charAt(8)).equalsIgnoreCase("-"))
            return true;
        return true;
    }

    /**
     * Method getName.
     * @param uuid UUID
    
     * @return String */
    private String getName(UUID uuid) {
        try {
            for (OfflinePlayer op : Bukkit.getOfflinePlayers()) {
                if (op.getUniqueId().compareTo(uuid) == 0)
                    return op.getName();
            }
        } catch (Exception ignored) {
        }
        return null;
    }

}
