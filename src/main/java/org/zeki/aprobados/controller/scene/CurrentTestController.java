package org.zeki.aprobados.controller.scene;

import lombok.Getter;
import lombok.Setter;
import org.zeki.aprobados.model.test.Answer;
import org.zeki.aprobados.model.test.Question;
import org.zeki.aprobados.model.test.Test;
import org.zeki.aprobados.model.user.AnswerTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Getter
@Setter
public class CurrentTestController {

    private Test currentTest;
    private List<AnswerTest> answerTests;
    private int selectedQuestion;
    private int selectedIndexQuestion;

    public CurrentTestController() {
        answerTests = new ArrayList<>();
    }

    public void setAnswersTestsId() {
        currentTest.getQuestions().forEach(question -> answerTests.add(new AnswerTest(question.getIdQuestion())));
    }

    public AnswerTest getStudentAnswerById(int id) {
        return answerTests.stream().filter(answerTest -> answerTest.getIdQuestion() == id).findFirst().orElse(null);
    }

    public Question nextQuestion() {
        selectedIndexQuestion++;
        Question question = currentTest.getQuestions().get(selectedIndexQuestion);
        selectedQuestion = question.getIdQuestion();
        return question;
    }

    public Question lastQuestion() {
        selectedIndexQuestion--;
        Question question = currentTest.getQuestions().get(selectedIndexQuestion);
        selectedQuestion = question.getIdQuestion();
        return question;
    }



    public int getTotalWrong() {
        return Math.toIntExact(answerTests.stream().filter(AnswerTest::isWrong).count());
    }

    public void reviewTest() {
        List<Question> questions = currentTest.getQuestions();
        questions.forEach(question -> {
            AnswerTest answerTest = answerTests.stream().filter(item -> item.getIdQuestion() == question.getIdQuestion()).findFirst().orElse(null);
            int rightIndex = IntStream.range(0, question.getAnswers().size()).filter(i -> question.getAnswers().get(i).isRight()).findFirst().orElse(-1);

            if (answerTest != null && answerTest.getSelectedAnswer() != rightIndex) answerTest.setWrong(true);
        });
    }

    public List<AnswerTest> getWrongAnswerTest() {
        return answerTests.stream().filter(AnswerTest::isWrong).toList();
    }
}

