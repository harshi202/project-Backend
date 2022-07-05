package com.edusystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.edusystem.entity.StudentDetails;
import com.edusystem.exception.AuthenticationFailedException;
import com.edusystem.exception.PasswordMisMatchException;
import com.edusystem.exception.StudentNotFoundException;
import com.edusystem.repository.StudentDetailsRepository;

@Service
public class StudentDetailsServiceImpl implements StudentDetailsService {

	@Autowired
	private StudentDetailsRepository studentDetailsRepository;

	@Override
	public StudentDetails saveStudentDetails(StudentDetails studentDetails) {
		StudentDetails newStudentDetails = studentDetailsRepository.save(studentDetails);
		return studentDetails;
	}

	@Override
	public StudentDetails getStudentDetailsById(int studentId) {
		Optional<StudentDetails> optionalStudentDetails = studentDetailsRepository.findById(studentId);

		if (optionalStudentDetails.isEmpty()) {
			throw new StudentNotFoundException("Student Not existing with id: " + studentId);
		}

		StudentDetails studentDetails = optionalStudentDetails.get();

		return studentDetails;
	}

	@Override
	public List<StudentDetails> getAllStudentDetails() {
		List<StudentDetails> studentDetails = studentDetailsRepository.findAll();

		return studentDetails;
	}

	@Override
	public StudentDetails updateStudentDetails(StudentDetails studentDetails) {

		Optional<StudentDetails> optionalStudentDetails = studentDetailsRepository
				.findById(studentDetails.getStudentId());

		if (optionalStudentDetails.isEmpty()) {
			throw new StudentNotFoundException("Student Not found with id: " + studentDetails.getStudentId());
		}

		StudentDetails updatedStudentDetails = studentDetailsRepository.save(studentDetails);

		return updatedStudentDetails;

	}

	@Override
	public void deleteStudentDetails(int studentDetailsId) {

		Optional<StudentDetails> optionalStudentDetails = studentDetailsRepository.findById(studentDetailsId);
		if (optionalStudentDetails.isEmpty()) {
			throw new StudentNotFoundException("Student Not found with id: " + studentDetailsId);
		}

		studentDetailsRepository.deleteById(studentDetailsId);

	}

	@Override
	public StudentDetails doLogin(int studentId, String studentPassword) {
		StudentDetails studentDetails = studentDetailsRepository.login(studentId, studentPassword);
		if (studentDetails == null) {
			throw new AuthenticationFailedException("Username or Password Invalid");
		}

		return studentDetails;

	}

	@Override
	public String recoverPassword(int studentId) {
		@SuppressWarnings("deprecation")
		StudentDetails studentDetails = studentDetailsRepository.getById(studentId);
		String password = studentDetails.getStudentPassword();
		return password;
	}

	@Override
	public String changePassword(int studentId, String oldPassword, String newPassword) {
		Optional<StudentDetails> studentDetailsById = studentDetailsRepository.findById(studentId);
		if (studentDetailsById.isEmpty()) {
			throw new StudentNotFoundException(studentId + " student not found");
		}
		
		StudentDetails studentDetails = studentDetailsById.get();
		if(!studentDetails.getStudentPassword().equals(oldPassword)) {
			throw new PasswordMisMatchException("old password not matching with new password");
		}
		
		StudentDetails newStudentDetails=new StudentDetails();
		newStudentDetails.setStudentId(studentDetails.getStudentId());
		newStudentDetails.setStudentName(studentDetails.getStudentName());
		newStudentDetails.setStudentContact(studentDetails.getStudentContact());
		newStudentDetails.setStudentAddress(studentDetails.getStudentAddress());
		newStudentDetails.setStudentEmail(studentDetails.getStudentEmail());
		newStudentDetails.setGender(studentDetails.getGender());
		newStudentDetails.setStudentPassword(newPassword);
		
		studentDetailsRepository.save(newStudentDetails);
		
		return "Password Updated Successfully.";
	}
}
