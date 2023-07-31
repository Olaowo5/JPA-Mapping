package acmecollege.entity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import common.JUnitBase;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestCRUDcourse extends JUnitBase {

	private EntityManager em;
	private EntityTransaction et;
	
	private static Course course;
	private static final String CourseCode = "COMP020";
	private static final String CourseTitle = "Computer programming";
	private static final int Year = 2023;
	private static final String Semester = "Summer";
	private static final int CreditUnit = 200;
	private static final byte online = 0;
	
	
	@BeforeAll
	static void setupAllInit()
	{
		course = new Course();
		course.setCourse(CourseCode, CourseTitle, Year, Semester, CreditUnit, online);
		
		
	}
	
	@BeforeEach
	void setup()
	{
		em = getEntityManager();
		et = em.getTransaction();
	}
	@AfterEach
	void tearDown()
	{
		em.close();
	}
	
	@Order(1)
	@Test
	void test01_Empty() {

		
		long result = getTotalCount(em, Course.class);
		assertThat( result, is( comparesEqualTo( 0L)));
		//assertThat( result, is( comparesEqualTo( 1L)));

	}

	@Test
	@Order(2)
	void test02_Create() {
		et.begin();
		//course = new Student();
		//stud.setFirstName(FirstName);
		//stud.setLastName(LastName);
		//prof.setDepartment(Department);
		course = new Course();
		course.setCourse(CourseCode, CourseTitle, Year, Semester, CreditUnit, online);
		course.setCreated(LocalDateTime.now());
		
		em.persist(course);
		et.commit();

		//Long result = getCountWithId(em,Professor.class,Integer.class,Professor_.id, prof.getId());

		long result = getTotalCount(em, Course.class);
		
		// There should only be one row in the DB
		assertThat(result, is(greaterThanOrEqualTo(1L)));
//		assertEquals(result, 1);
	}

	@Test
	@Order(3)
	void test03_CreateInvalid() {
		et.begin();
		
		Course tesTtCourse = new Course();
		tesTtCourse.setCourseTitle("Mathematics");
		assertThrows(PersistenceException.class, () -> em.persist(tesTtCourse));
		et.commit();
	}

	@Test
	@Order(4)
	void test04_Read() {
		
	
		
		List<Course> coursAll = getAll(em,Course.class);
		assertThat(coursAll, contains(equalTo(course)));
	}

	@Test
	@Order(5)
	void test05_ReadAllFields() {
		//Professor returnProf = getWithId(em, Professor.class, Integer.class, Professor_.id,prof.getId());
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		// create query for Professor
		CriteriaQuery< Course> query = builder.createQuery( Course.class);
		// select p from Professor p
		Root< Course> root = query.from( Course.class);
		query.select( root);
		query.where( builder.equal( root.get( Course_.id), builder.parameter( Integer.class, "id")));
		// create query and set the parameter
		TypedQuery< Course> tq = em.createQuery( query);
		tq.setParameter( "id", course.getId());
		// get the result as row count
		Course returnco = tq.getSingleResult();

		assertThat(returnco.getCourseCode(), equalTo(CourseCode));
		assertThat(returnco.getCourseTitle(), equalTo(CourseTitle));
		assertThat(returnco.getYear(), equalTo(Year));
		assertThat(returnco.getSemester(), equalTo(Semester));
		assertThat(returnco.getCreditUnits(), equalTo(CreditUnit));
		
	}

	@Test
	@Order(6)
	void test06_Update() {
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		// create query for Professor
		CriteriaQuery< Course> query = builder.createQuery( Course.class);
		// select p from Professor p
		Root< Course> root = query.from( Course.class);
		query.select( root);
		query.where( builder.equal( root.get( Course_.id), builder.parameter( Integer.class, "id")));
		// create query and set the parameter
		TypedQuery< Course> tq = em.createQuery( query);
		tq.setParameter( "id", course.getId());
		// get the result as row count
		Course returnco= tq.getSingleResult();
		
		
		String newcourseTitle = "A.p Biology";
		LocalDateTime NewDt = LocalDateTime.now();
		String newCourceCode = "BSCE";
		
		et.begin();
		returnco.setCourseCode(newCourceCode);
		returnco.setCourseTitle(newcourseTitle);
		returnco.setOnline((byte) 1);
		returnco.setUpdated(NewDt);
		em.merge(returnco);
		
		et.commit();
		//returnProf = getWithId(em,Professor.class, Integer.class, Professor_.id,prof.getId());

		 returnco= tq.getSingleResult();

		assertThat(returnco.getCourseCode(), equalTo(newCourceCode));
		assertThat(returnco.getCourseTitle(), equalTo(newcourseTitle));
		//assertThat(returnProf.getDepartment(), equalTo(newDepartment));
	}

	
	@Test
	@Order(7)
	void test07_Delete() {
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		// create query for Professor
		CriteriaQuery< Course> query = builder.createQuery( Course.class);
		// select p from Professor p
		Root< Course> root = query.from( Course.class);
		query.select( root);
		query.where( builder.equal( root.get( Course_.id), builder.parameter( Integer.class, "id")));
		// create query and set the parameter
		TypedQuery< Course> tq = em.createQuery( query);
		tq.setParameter( "id", course.getId());
		// get the result as row count
		Course returnCo= tq.getSingleResult();
		
		et.begin();
		Course coursetest = new Course();

		String newCourseCode = "CHO";
		String newCourseTitle = "Chemistry";
		int newYear = 2035;
		String newsemester = "Winter";
		int newcreditUni = 5;
		byte Newbyte = 1;
		
		coursetest.setCourse(newCourseCode, newCourseTitle, newYear, newsemester, newcreditUni, Newbyte);
		
		em.persist(coursetest);
		
		et.commit();
		
		et.begin();
		em.remove(returnCo);
		et.commit();
		
		//long result = getCountWithId(em,Professor.class,Integer.class, Professor_.id, returnProf.getId());
		long result = getTotalCount(em, Course.class);
		assertThat(result, is(equalTo(1L)));
		
		//result = getCountWithId(em, Professor.class, Integer.class, Professor_.id, proftest.getId());
		result = getTotalCount(em, Course.class);
				assertThat(result, is(equalTo(1L)));
	}
}
