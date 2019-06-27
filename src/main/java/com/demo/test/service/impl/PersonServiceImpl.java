package com.demo.test.service.impl;

import com.demo.test.dao.StudentRepository;
import com.demo.test.domain.Student;
import com.demo.test.service.PersonService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Service class
 *
 * @author Jack
 * @date 2019/05/30 14:36 PM
 */
@Service("studentService")
@Transactional(rollbackOn = Exception.class)
public class PersonServiceImpl implements PersonService {

    static Logger logger = LogManager.getLogger(PersonServiceImpl.class);

    @Autowired
    private final StudentRepository studentRepository;

    @Autowired
    public PersonServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Page<Student> listByPage(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    @Override
    public Page<Student> findByName(String name, Pageable pageable) {
        return studentRepository.findByName(name, pageable);
    }

    @Override
    public List<Student> findByNameAndPassword(String name, String password) {
        return studentRepository.findByNameAndPassword(name, password);
    }

    @Override
    public void addStudent(Student student) {
        studentRepository.save(student);
    }

    @Override
    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }

}
