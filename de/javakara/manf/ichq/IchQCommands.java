package de.javakara.manf.ichq;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IchQCommands implements CommandExecutor {
	private HashMap<String, QuestionChecklist> checklists = new HashMap<String, QuestionChecklist>();
	private static final ArrayList<Question> questions = new ArrayList<Question>();
	private static boolean questionsInitialised = false;
	private static ArrayList<String> finishedPlayers = new ArrayList<String>();
	
	static{
		loadQuestions();
		try {
			Object o = Config.loadObjectFromHarddisk("players");
			if(o != null && o instanceof ArrayList<?>){
				List<?> l = (ArrayList<?>) o;
				for(Object obj:l){
					finishedPlayers.add((String) obj);
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,String label, String[] args) {
		if (!(sender.hasPermission("ichq.use") && sender instanceof Player)) {
			sender.sendMessage(lang("perm"));
			return true;
		}
		if (label.equalsIgnoreCase("quiz")) {
			if(args.length == 0){
				if (questionsInitialised) {
					// Start new Quiz
					if (checklists.containsKey(sender.getName())) {
						sender.sendMessage(lang("inquizalready"));
						return true;
					} else if(finishedPlayers.contains(sender.getName())){
						sender.sendMessage(lang("alreadyfinished"));
					}else{
						QuestionChecklist checklist = new QuestionChecklist(questions,Config.getInt(""));
						checklists.put(sender.getName(), checklist);
						sender.sendMessage(lang("startmsg"));
						sendNewQuestion(sender,checklist);
						return true;
					}
				}else{
					sender.sendMessage(lang("init"));
					return true;
				}
			}else{
				if(sender.hasPermission("ichq.search")){
					Player p = Bukkit.getPlayer(args[0]);
					if(p == null){
						sender.sendMessage(lang("notfound"));
						return true;
					}
					if(finishedPlayers.contains(p.getName())){
						sender.sendMessage(lang("yes"));
						return true;
					}else{
						sender.sendMessage(lang("no"));
						return true;
					}
				}
			}			
		}else if(label.equalsIgnoreCase("a") || label.equalsIgnoreCase("b")){
			if(checklists.containsKey(sender.getName())){
				int answer;
				if(label.equalsIgnoreCase("a")){
					answer = 0;
				}else{
					answer = 1;
				}
				QuestionChecklist checklist = checklists.get(sender.getName());
				if(checklist.solveQuestion(answer)){
					sender.sendMessage(lang("correct"));
					if(checklist.isQuizComplete()){
						sender.sendMessage(lang("congratulations"));
						finishedPlayers.add(sender.getName());
						try {
							saveFinishedPlayers();
						} catch (IOException e) {
							e.printStackTrace();
						}
						checklists.remove(sender.getName());
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),Config.getString("correct-command").replace("<[p]>", sender.getName()));
						checklists.remove(sender.getName());
						return true;
					}else{
						sendNewQuestion(sender,checklist);
						return true;
					}
				}else{
					sender.sendMessage(lang("wrong"));
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),Config.getString("wrong-command").replace("<[p]>", sender.getName()));
					checklists.remove(sender.getName());
					return true;
				}
			}			
		}
		return false;
	}

	public void sendNewQuestion(CommandSender sender,QuestionChecklist checklist){
		Question q = checklist.getQuestion(checklist.getRandomQuestion());
		sender.sendMessage(getQuestionString(Config.getString("questionformat"),checklist,q));
		sender.sendMessage(getAnswerString(Config.getString("answerformat"), q));
	}
	
	public static void loadQuestions() {
		List<String> questionlist = Config.getStringList("questions");
		for(String s:questionlist){
			String[] args = s.split("-");
			if(args.length >= 4){
				questions.add(new Question(args[0], args[1], args[2], Integer.valueOf(args[3])));
			}else{
				System.out.println("Error for Line :" + s);
			}
		}
		
		questionsInitialised = true;
	}

	private String lang(String string) {
		return ChatColor.translateAlternateColorCodes('&',Config.getString("lang." + string));
	}

	private String getQuestionString(String msg,QuestionChecklist checklist,Question q){
		String s = msg.replace("<[#]>", (checklist.getAnsweredQuestions()+1) + "");
		s = s.replace("<[Q]>", q.getQuestion());
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	
	private String getAnswerString(String msg,Question q) {
		String s = msg.replace("<[0]>", q.getAnswers()[0]).replace("<[1]>", q.getAnswers()[1]);
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	
	private void saveFinishedPlayers() throws IOException{
		Config.saveObjectToHarddisk(finishedPlayers, "players");
	}
}