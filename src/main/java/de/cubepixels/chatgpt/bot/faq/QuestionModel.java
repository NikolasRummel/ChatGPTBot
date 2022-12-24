package de.cubepixels.chatgpt.bot.faq;

/**
 * @author Nikolas Rummel
 * @since 23.12.22
 */
public class QuestionModel {

    private String similarQuestion;
    private String answer;

    public QuestionModel(String similarQuestion, String answer) {
        this.similarQuestion = similarQuestion;
        this.answer = answer;
    }

    public String getSimilarQuestion() {
        return similarQuestion;
    }

    public void setSimilarQuestion(String similarQuestion) {
        this.similarQuestion = similarQuestion;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
