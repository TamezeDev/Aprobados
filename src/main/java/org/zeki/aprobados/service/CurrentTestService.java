package org.zeki.aprobados.service;

import org.zeki.aprobados.app.AppContext;
import org.zeki.aprobados.app.SessionManager;
import org.zeki.aprobados.controller.scene.CurrentTestController;
import org.zeki.aprobados.model.test.Question;
import org.zeki.aprobados.model.test.Test;
import org.zeki.aprobados.model.user.AnswerTest;
import org.zeki.aprobados.model.user.StudentTest;

import java.util.List;

public class CurrentTestService {

    private final CurrentTestController testController;

    public CurrentTestService() {
        testController = new CurrentTestController();
    }

    public void setSelectedTest(Test test) {
        testController.setCurrentTest(test);
        testController.setAnswersTestsId();
        testController.setSelectedQuestion(test.getQuestions().getFirst().getIdQuestion());
        testController.setSelectedIndexQuestion(0);
    }

    public Question getQuestionByIndex(int index) {
        return testController.getCurrentTest().getQuestions().get(index);
    }

    public int getSelectedIndexQuestion() {
        return testController.getSelectedIndexQuestion();
    }

    public boolean nextFirstQuestion() {
        return testController.getSelectedIndexQuestion() == 1;
    }

    public boolean isFirstQuestion() {
        return testController.getSelectedIndexQuestion() == 0;
    }

    public boolean selectedFirstQuestion(int selectedIndex) {
        return selectedIndex == 0;
    }

    public boolean selectedLastQuestion(int selectedIndex) {
        return selectedIndex == (getQuestionsLength() - 1);
    }

    public boolean checkSingleQuestion() {
        return getQuestionsLength() == 1;
    }

    public boolean isDualQuestion() {
        return getQuestionsLength() == 2;
    }

    public boolean isLastQuestion() {
        int sizeTest = getQuestionsLength();
        return testController.getSelectedIndexQuestion() == (sizeTest - 1);
    }

    public boolean nextLastQuestion() {
        int sizeTest = getQuestionsLength();
        return (testController.getSelectedIndexQuestion() + 1) == (sizeTest - 1);
    }

    public Question getNextQuestion() {
        return testController.nextQuestion();
    }

    public Question getLastQuestion() {
        return testController.lastQuestion();
    }

    public void setSelectedQuestion(int idQuestion, int indexQuestion) {
        testController.setSelectedQuestion(idQuestion);
        testController.setSelectedIndexQuestion(indexQuestion);
    }

    public void setAsSelectedAnswer(int answerIndex) {
        testController.getStudentAnswerById(testController.getSelectedQuestion()).setSelectedAnswer(answerIndex);
    }

    public boolean anyAnswerEmpty() {
        return testController.getAnswerTests().stream().anyMatch(answerTest -> answerTest.getSelectedAnswer() == -1);
    }

    public int getQuestionsLength() {
        return testController.getAnswerTests().size();
    }

    public ResultService answerSelected(int idQuestion) {

        AnswerTest selectedAnswer = testController.getStudentAnswerById(idQuestion);

        if (selectedAnswer.getSelectedAnswer() != -1) {
            return new ResultService(true, selectedAnswer.getSelectedAnswer());
        }
        return new ResultService(false, -1);
    }

    public List<AnswerTest> getAnswerTest() {
        return testController.getAnswerTests();
    }

    public int getTotalWrongAnswers() {
        return testController.getTotalWrong();
    }

    public List<Question> getAllQuestions() {
        return testController.getCurrentTest().getQuestions();
    }

    public void reviewTest() {
        // REVIEW FULL TEST
        testController.reviewTest();
    }

    public ResultService sendTestDataToDB() {
        if (isReviewedTest()) return sendReviewedTest();
        return sendResultTest();
    }

    // --------------PRIVATE METHODS ----------

    private boolean isReviewedTest() {
        return testController.getCurrentTest().isReviewed();
    }

    private ResultService sendReviewedTest() {
        // GET RIGHT QUESTIONS AND SEND TO DB
        List<Integer> rightsID = testController.getRightIDQuestions();
        if (rightsID.isEmpty()) return new ResultService("No hay preguntas correctas", false);
        return AppContext.getInstance().getServerManager().getTestService().sendReviewedQuestions(rightsID, SessionManager.getInstance().getCurrentUser().getJwt());
    }

    private ResultService sendResultTest() {
        // GET TEST DATA AND WRONG ANSWERS
        int testID = testController.getCurrentTest().getIdTest();
        int sizeTest = getQuestionsLength();
        int wrong = getTotalWrongAnswers();
        int right = sizeTest - wrong;
        double note = ((double) right / sizeTest) * 10;
        List<AnswerTest> wrongTest = testController.getWrongAnswerTest();

        StudentTest studentTest = new StudentTest(testID, wrong, right, note, wrongTest);
        return AppContext.getInstance().getServerManager().getTestService().sendTestToDB(studentTest, SessionManager.getInstance().getCurrentUser().getJwt());
    }


}
