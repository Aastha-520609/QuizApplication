package com.application.QuizApplication.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.application.QuizApplication.DAO.QuestionDAO;
import com.application.QuizApplication.DAO.QuizDAO;
import com.application.QuizApplication.Model.Question;
import com.application.QuizApplication.Model.QuestionWrapper;
import com.application.QuizApplication.Model.Quiz;
import com.application.QuizApplication.Model.Response;

@Service
public class QuizService {
	
	@Autowired
	QuizDAO quizDAO;
	
	@Autowired
	QuestionDAO questionDAO;

	public ResponseEntity<String> createQuiz(String category, int numQ, String title) {
		
		List<Question> questions = questionDAO.findRandomQuestionsByCategory(category, numQ);
		
		Quiz quiz = new Quiz();
		quiz.setTitle(title);
		quiz.setQuestions(questions);
		quizDAO.save(quiz);
		return new ResponseEntity<>("Success", HttpStatus.CREATED);
	}

	public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
		Optional<Quiz> quiz = quizDAO.findById(id);
		List<Question> questionsFromDB= quiz.get().getQuestions();
		List<QuestionWrapper> questionsForUser = new ArrayList<>();
		for(Question q: questionsFromDB) {
			QuestionWrapper qw = new QuestionWrapper(q.getId(), q.getQuestionTitle(), q.getOption1(), q.getOption2(), q.getOption3(), q.getOption4());
			questionsForUser.add(qw);
		}
		return new ResponseEntity<>(questionsForUser, HttpStatus.OK);
	}

	public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {
	    Quiz quiz = quizDAO.findById(id).orElseThrow(() -> new RuntimeException("Quiz not found"));
	    List<Question> questions = quiz.getQuestions();

	    Map<Integer, String> correctAnswers = questions.stream()
	            .collect(Collectors.toMap(Question::getId, q -> q.getRightAnswer().trim()));

	    int correctCount = 0;

	    for (Response response : responses) {
	        if (correctAnswers.containsKey(response.getId()) && 
	            correctAnswers.get(response.getId()).equalsIgnoreCase(response.getResponse().trim())) {
	            correctCount++;
	        }
	    }

	    return new ResponseEntity<>(correctCount, HttpStatus.OK);
	}

}
