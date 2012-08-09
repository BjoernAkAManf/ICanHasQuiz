package de.javakara.manf.ichq;

public class Question {
	/** Question */
	private String formattedQuestion;
	/** Answers	 */
	private String[] answers;
	/** Correct Answer */
	private int answer;
	
	/**
	 * Defines a Question
	 * @param question The actual Question
	 * @param a Answer a
	 * @param b Answer b
	 * @param i Correct answer
	 */
	public Question(String question,String a,String b,int i){
		if(i > 1){
			i = 1;
		}
		formattedQuestion = question;
		answers = new String[2];
		answers[0] = a;
		answers[1] = b;
		answer = i;
	}
	
	/**
	 * Gets the actuall Question
	 * @return Question
	 */
	public String getQuestion(){
		return formattedQuestion;
	}
	
	/**
	 * Returns all possible Answers
	 * @return String Array Answers
	 */
	public String[] getAnswers(){
		return answers;
	}

	/**
	 * Returns Answer for Question
	 * @return Index of Answers
	 */
	public int getAnswer(){
		return answer;
	}
}
