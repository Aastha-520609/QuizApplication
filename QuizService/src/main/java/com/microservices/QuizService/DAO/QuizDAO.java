package com.microservices.QuizService.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import com.microservices.QuizService.Model.Quiz;

public interface QuizDAO extends JpaRepository<Quiz, Integer>{

}
