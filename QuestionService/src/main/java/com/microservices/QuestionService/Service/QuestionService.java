package com.microservices.QuestionService.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.microservices.QuestionService.Model.QuestionWrapper;
import com.microservices.QuestionService.Model.Response;
import com.microservices.QuestionService.DAO.QuestionDAO;
import com.microservices.QuestionService.Model.Question;

@Service
public class QuestionService {
	
	@Autowired
	QuestionDAO questionDAO;

	public ResponseEntity<List<Question>> getAllQuestions() {
		try {
			return new ResponseEntity<>(questionDAO.findAll(), HttpStatus.OK);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<List<Question>> getQuestionsByCategory(String category) {
		try {
			return new ResponseEntity<>(questionDAO.findByCategory(category), HttpStatus.OK);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<String> addQuestion(Question question) {
		try {
			questionDAO.save(question);
			return new ResponseEntity<>("success", HttpStatus.CREATED);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return new ResponseEntity<>("Question not added", HttpStatus.BAD_REQUEST);
	}
	
	public ResponseEntity<String> deleteQuestion(Integer id) {
		
		try {
			if(questionDAO.existsById(id)) {
				questionDAO.deleteById(id);
				return new ResponseEntity<>("Question Deleted Successfully", HttpStatus.CREATED);
	        }
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return new ResponseEntity<>("Question not found", HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<List<Integer>> getQuestionsForQuiz(String categoryName, Integer numQuestions) {
	    List<Integer> questions = questionDAO.findRandomQuestionsByCategory(categoryName, numQuestions);
		return new ResponseEntity<>(questions, HttpStatus.OK);
	}

	public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(List<Integer> questionIds) {
		List<QuestionWrapper> wrappers = new ArrayList<>();
		List<Question> questions = new ArrayList<>();
		
		for(Integer id : questionIds) {
			questions.add(questionDAO.findById(id).get());
		}
		
		for(Question question : questions) {
			com.microservices.QuestionService.Model.QuestionWrapper wrapper = new QuestionWrapper();
			wrapper.setId(question.getId());
			wrapper.setQuestionTitle(question.getQuestionTitle());
			wrapper.setOption1(question.getOption1());
			wrapper.setOption2(question.getOption2());
			wrapper.setOption3(question.getOption3());
			wrapper.setOption4(question.getOption4());
			wrappers.add(wrapper);
		}
		return new ResponseEntity<>(wrappers, HttpStatus.OK);
	}

	public ResponseEntity<Integer> getScore(List<Response> responses) {
		int right = 0;
	   
		for(Response response : responses) {
			Question question = questionDAO.findById(response.getId()).get();
			if(response.getResponse().equals(question.getRightAnswer())) {
				right++;
			}
		}
		return new ResponseEntity<>(right, HttpStatus.OK);
	}

}
