package de.cubepixels.chatgpt.bot.question;

import de.cubepixels.chatgpt.bot.ChatGPTPlugin;
import java.util.ArrayList;

/**
 * The type QuestionCollection. It contains all QA-Models and generates the specific question for
 * the ai to respond which QA correspond
 *
 * @author Nikolas Rummel
 * @since 23.12.22
 */
public class QuestionCollection {

    private final ChatGPTPlugin plugin;
    private final ArrayList<QuestionModel> questions;

    /**
     * Instantiates a new QuestionCollection.
     *
     * @param plugin the plugin
     */
    public QuestionCollection(ChatGPTPlugin plugin) {
        this.plugin = plugin;
        this.questions = new ArrayList<>();

        initDefault();
    }

    /**
     * TODO: Add a command to add QA's
     */
    private void initDefault() {
        this.questions.add(new QuestionModel("What is the discord ip",
            "§7The ip of this discord server is §bdc.server.eu"));
        this.questions.add(new QuestionModel("Who is the owner of this server?",
            "§7The owner of this server is §bYOURNAME"));
        this.questions.add(new QuestionModel("Where can I apply?",
            "§You can apply at §bapply.server.eu"));
    }

    /**
     * Generates the specific Question.
     *
     * @param sentence the sentence a user wrote
     * @return the specific question for the ai
     */
    public String generateRequestString(String sentence) {
        StringBuilder sentencesToCompare = new StringBuilder();

        int i = 1;
        for (QuestionModel question : questions) {
            sentencesToCompare.append(i).append(". ").append(question.getSimilarQuestion());
            i++;
        }

        return "Here is a list of sentences: "
            + sentencesToCompare.toString() + "    "
            + "To which sentence is this sentence similar: '" + sentence
            + "' Give only the number, no other characters, no points etc! If there is no match, say 'no match found'";
    }

    /**
     * Sends and answer of an QA in the chat
     *
     * @param response the response which serves as index
     */
    public void sendAnswer(String response) {
        try {
            response = response.replaceAll("\n", "");
            int index = Integer.parseInt(response) - 1;
            plugin.sendBotMessage("\n" + questions.get(index).getAnswer());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Getter for the List of QA's.
     *
     * @return the questions
     */
    public ArrayList<QuestionModel> getQuestions() {
        return questions;
    }
}
