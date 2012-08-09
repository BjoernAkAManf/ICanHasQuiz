package de.javakara.manf.ichq;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

public final class Config {
	private static FileConfiguration config;
	private static File configFile;
	
	public static boolean initialise(FileConfiguration config,File dataFolder) throws IOException{
		Config.config = config;
		Config.configFile = new File(dataFolder + File.separator + "config.yml");
		if (!config.isSet("ts3c.version")){
			config.set("questionformat", "[Quiz] Frage <[#]>: <[Q]>");
			config.set("answerformat","[Quiz]" + "Antwort a: <[0]> | Antwort b: <[1]>");
			config.set("wrong-command","ban <[p]> Wrong Answer!");
			config.set("lang.perm","&1No Permissions");
			config.set("lang.startmsg","[Quiz]Du hast das Quiz begonnen! beantworte immer mit /a oder /b!");
			config.set("lang.inquizalready", "Du bist gerade in einem Quiz drin!");
			config.set("lang.alreadyfinished", "Du hast bereits das Quiz abgeschlossen!");
			config.set("lang.init","Wird gerade initialisiert!");
			config.set("lang.correct","[Quiz]Richtige Antwort!");
			config.set("lang.wrong", "[Quiz]Falsche Antwort! Fang nocheinmal von vorne an!");
			config.set("lang.congratulations","[Quiz]Glückwunsch, du hast alle Fragen Richtig!");
			config.set("lang.yes", "[Quiz] User hat bestanden!");
			config.set("lang.no", "[Quiz] User hat noch nicht bestanden!");
			config.set("lang.notfound", "[Quiz] User not found!");
			List<String> questions = new ArrayList<String>();
			questions.add("Was ist der höchste Rang?-Richtig-Falsch-0");
			questions.add("Was ist die höchste Rang?-Falsch-Richtig-1");
			questions.add("Was ist das höchste Rang?-Owner-Admin-0");
			config.set("questions",questions);
			save();
		}
		return true;		
	}
	
	public static int getInt(String node){
		return config.getInt(node);
	}
	
	public static String getString(String node){
		if(!config.isSet(node)){
			config.set(node, "asdf");
			try {
				save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return config.getString(node);
	}

	public static boolean getBoolean(String node) {
		return config.getBoolean(node);
	}
	
	public static List<String> getStringList(String node){
		return config.getStringList(node);
	}
			
	public static void load() throws FileNotFoundException, IOException, InvalidConfigurationException {
		config.load(configFile);
	}
	
	public static void save() throws IOException {
		config.set("ts3c.version", "1");
		config.save(configFile);
	}
	
	public static void saveObjectToHarddisk(Object o, String filename) throws IOException{
		File f = new File(configFile.getParent() + File.separator + filename + ".dat");
		ObjectOutputStream ooout = new ObjectOutputStream(new FileOutputStream(f));
		ooout.writeObject(o);
	}
	
	public static Object loadObjectFromHarddisk(String filename) throws FileNotFoundException, IOException, ClassNotFoundException{
		File f = new File(configFile.getParent() + File.separator + filename + ".dat");
		if(f.exists()){
			ObjectInputStream oin = new ObjectInputStream(new FileInputStream(f));
			return oin.readObject();
		}
		return null;
	}
}
