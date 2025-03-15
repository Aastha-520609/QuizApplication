package com.microservices.QuizService.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.microservices.QuizService.DAO.QuizDAO;
import com.microservices.QuizService.Feign.QuizInterface;
import com.microservices.QuizService.Model.Question;
import com.microservices.QuizService.Model.QuestionWrapper;
import com.microservices.QuizService.Model.Quiz;
import com.microservices.QuizService.Model.Response;

@Service
public class QuizService {
	
	@Autowired
	QuizDAO quizDAO;
	
	@Autowired
    QuizInterface quizInterface;

	public ResponseEntity<String> createQuiz(String category, int numQ, String title) {
		List<Integer> questions = quizInterface.getQuestionsForQuiz(category, numQ).getBody();
		Quiz quiz = new Quiz();
		quiz.setTitle(title);
		quiz.setQuestionIds(questions);
		quizDAO.save(quiz);
		return new ResponseEntity<>("Success", HttpStatus.CREATED);
	}

	public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
        Quiz quiz = quizDAO.findById(id).get();
        List<Integer> questionIds = quiz.getQuestionIds();
        ResponseEntity<List<QuestionWrapper>> questions = quizInterface.getQuestionsFromId(questionIds);
		return questions;
	}

	public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {
        ResponseEntity<Integer> score = quizInterface.getScore(responses);
        return score;
	}

}
