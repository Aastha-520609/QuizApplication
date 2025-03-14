package com.application.QuizApplication.DAO;

import com.application.QuizApplication.Model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizDAO extends JpaRepository<Quiz, Integer>{

}
