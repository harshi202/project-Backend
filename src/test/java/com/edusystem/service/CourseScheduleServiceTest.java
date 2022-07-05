package com.edusystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.edusystem.entity.Course;
import com.edusystem.entity.CourseSchedule;
import com.edusystem.entity.Trainer;
import com.edusystem.exception.CourseScheduleNotFoundException;
import com.edusystem.repository.CourseRepository;
import com.edusystem.repository.CourseScheduleRepository;
import com.edusystem.repository.TrainerRepository;

@SpringBootTest
public class CourseScheduleServiceTest {

	@InjectMocks
	private CourseScheduleService courseScheduleService = new CourseScheduleServiceImpl();

	@Mock
	private CourseScheduleRepository courseScheduleRepository;

	@Mock
	private TrainerRepository trainerRepository;

	@Mock
	private CourseRepository courseRepository;

	@Test
	public void testCourseScheduleById() {

		CourseSchedule courseSchedule = new CourseSchedule();
		courseSchedule.setCourseScheduleId(1);
		courseSchedule.setTiming("9am - 12pm");

		Optional<CourseSchedule> optionalCourseSchedule = Optional.of(courseSchedule);

		when(courseScheduleRepository.findById(1)).thenReturn(optionalCourseSchedule);

	}

	@Test
	public void testGetCourseScheduleByIdWithException() {

		when(courseScheduleRepository.findById(100)).thenThrow(CourseScheduleNotFoundException.class);

		assertThrows(CourseScheduleNotFoundException.class, () -> courseScheduleService.getCourseScheduleById(100));
	}

	@Test
	public void testGetAllCourseSchedules() {

		CourseSchedule courseSchedule = new CourseSchedule();
		courseSchedule.setCourseScheduleId(1);
		courseSchedule.setTiming("9am - 12pm");

		CourseSchedule courseSchedule1 = new CourseSchedule();
		courseSchedule.setCourseScheduleId(2);
		courseSchedule.setTiming("9am - 12pm");

		List<CourseSchedule> courseScheduleList = new ArrayList<>();
		courseScheduleList.add(courseSchedule);
		courseScheduleList.add(courseSchedule1);

		when(courseScheduleRepository.findAll()).thenReturn(courseScheduleList);

		List<CourseSchedule> courseSchedules = courseScheduleService.getAllCourseSchedule();

		assertEquals(2, courseSchedules.size());

	}

	@Test
	public void testSaveCourseSchedule() {

		Course newCourse = new Course();
		Trainer newTrainer = new Trainer();

		newCourse.setCourseId(1);
		newCourse.setCourseFee(1600);
		newCourse.setCourseDuration(3);
		newCourse.setCourseName("java");
		newTrainer.setTrainerId(1);

		CourseSchedule courseSchedule = new CourseSchedule();
		courseSchedule.setCourseScheduleId(1);
		courseSchedule.setTiming("9am - 12pm");
		courseSchedule.setCourse(newCourse);
		courseSchedule.setTrainerId(1);

		when(courseRepository.findById(1)).thenReturn(Optional.of(newCourse));

		when(trainerRepository.findById(1)).thenReturn(Optional.of(newTrainer));

		when(courseScheduleRepository.save(courseSchedule)).thenReturn(courseSchedule);

		CourseSchedule newCourseSchedule = courseScheduleService.saveCourseSchedule(courseSchedule, 1);

		assertEquals(1, newCourseSchedule.getCourseScheduleId());

		verify(courseScheduleRepository, times(1)).save(courseSchedule);

	}

	@Test
	public void testDeleteCourseSchedule() {

		Course  course = new Course();
		Trainer trainer = new Trainer();
		CourseSchedule courseSchedule = new CourseSchedule();
		courseSchedule.setCourseScheduleId(1);
		courseSchedule.setTiming("9am - 12pm");
		courseSchedule.setTrainerId(1);
		course.setCourseSchedule(null);

		Optional<CourseSchedule> optionalCourseSchedule = Optional.of(courseSchedule);

		when(courseScheduleRepository.findById(1)).thenReturn(optionalCourseSchedule);

		Optional<Trainer> optionalTrainer = Optional.of(trainer);

		when(trainerRepository.findById(1)).thenReturn(optionalTrainer);

		courseScheduleService.deleteCourseSchedule(1);

		verify(courseScheduleRepository, times(1)).findById(1);
		verify(trainerRepository, times(1)).findById(1);
		verify(courseScheduleRepository, times(1)).deleteById(1);

	}

	@Test
	void testUpdateCourseSchedule() {

		Trainer trainer = new Trainer();
		CourseSchedule courseSchedule = new CourseSchedule();
		courseSchedule.setCourseScheduleId(1);
		courseSchedule.setTiming("9am - 12pm");
		courseSchedule.setTrainerId(1);

		Optional<CourseSchedule> optionalCourseSchedule = Optional.of(courseSchedule);

		when(courseScheduleRepository.findById(1)).thenReturn(optionalCourseSchedule);

		Optional<Trainer> optionalTrainer = Optional.of(trainer);

		when(trainerRepository.findById(1)).thenReturn(optionalTrainer);

		courseScheduleService.updateCourseSchedule(courseSchedule);

		verify(courseScheduleRepository, times(1)).findById(1);
		verify(trainerRepository, times(1)).findById(1);
		verify(courseScheduleRepository, times(1)).save(courseSchedule);
		

	}

}
