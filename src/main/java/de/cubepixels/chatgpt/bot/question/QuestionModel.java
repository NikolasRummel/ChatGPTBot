package de.cubepixels.chatgpt.bot.question;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * The type QuestionModel.
 * It represents a QA
 *
 * @author Nikolas Rummel
 * @since 23.12.22
 */
@Data
@AllArgsConstructor
public class QuestionModel {

    private String similarQuestion;
    private String answer;

}
