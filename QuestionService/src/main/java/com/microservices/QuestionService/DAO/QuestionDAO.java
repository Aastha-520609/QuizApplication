package com.microservices.QuestionService.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.microservices.QuestionService.Model.Question;

@Repository
public interface QuestionDAO extends JpaRepository<Question, Integer>{
	
	List<Question> findByCategory(String category);

	@Query(value = "SELECT q.id FROM question q WHERE q.category= :category ORDER BY RAND() LIMIT :numQ", nativeQuery = true)
	List<Integer> findRandomQuestionsByCategory(@Param("category") String category, @Param("numQ") int numQ);

}
