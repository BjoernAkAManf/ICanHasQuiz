package de.javakara.manf.ichq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class QuestionChecklist{
	private final HashMap<Integer,Question> checklist;
	private int currentQuestion;
	private int answeredQuestions;
	/**
	 * Initialise a Quiz
	 * @param questions
	 * @param subject
	 */
	public QuestionChecklist(final ArrayList<Question> questions){
		checklist = new HashMap<Integer,Question>();
		answeredQuestions = 0;
		int i = 0;
		for(Question q:questions){
			checklist.put(i, q);
			i++;
		}
	}
	
	public Question getQuestion(int id){
		currentQuestion = id;
		return checklist.get(id);
	}
	
	public int getRandomQuestion(){
		Random r = new Random();
		int x = 0;
		do{
			int i = r.nextInt(checklist.size());
			if(checklist.containsKey(i)){
				return i;
			}
			x++;
		}while(x <=5);
		
		for(int i:checklist.keySet()){
			return i;
		}
		return 0;
	}
	
	public boolean solveQuestion(int answer){
		if(checklist.get(currentQuestion).getAnswer() == answer){
			checklist.remove(currentQuestion);
			answeredQuestions++;
			return true;
		}
		return false;
	}
	
	public boolean isQuizComplete(){
		return checklist.size() == 0;
	}

	public int getAnsweredQuestions() {
		return answeredQuestions;
	}
}
