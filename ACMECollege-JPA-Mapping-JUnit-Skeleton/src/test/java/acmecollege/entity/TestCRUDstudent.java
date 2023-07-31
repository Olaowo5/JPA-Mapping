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
public class TestCRUDstudent extends JUnitBase {
	private EntityManager em;
	private EntityTransaction et;
	
	private static Student stud;
	private static final String FirstName = "Imade";
	private static final String LastName = "Osarwunese";
	
	@BeforeAll
	static void setupAllInit()
	{
		stud = new Student();
		stud.setFullName(FirstName, LastName);
		
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

		
		long result = getTotalCount(em, Student.class);
		assertThat( result, is( comparesEqualTo( 0L)));
		//assertThat( result, is( comparesEqualTo( 1L)));

	}

	@Test
	@Order(2)
	void test02_Create() {
		et.begin();
		stud = new Student();
		stud.setFirstName(FirstName);
		stud.setLastName(LastName);
		//prof.setDepartment(Department);
		stud.setCreated(LocalDateTime.now());
		
		em.persist(stud);
		et.commit();

		//Long result = getCountWithId(em,Professor.class,Integer.class,Professor_.id, prof.getId());

		long result = getTotalCount(em, Student.class);
		
		// There should only be one row in the DB
		assertThat(result, is(greaterThanOrEqualTo(1L)));
//		assertEquals(result, 1);
	}

	@Test
	@Order(3)
	void test03_CreateInvalid() {
		et.begin();
		
		Student testStud = new Student();
		testStud.setLastName("Arts & Crafts");
		assertThrows(PersistenceException.class, () -> em.persist(testStud));
		et.commit();
	}

	@Test
	@Order(4)
	void test04_Read() {
		
	
		
		List<Student> studsAll = getAll(em,Student.class);
		assertThat(studsAll, contains(equalTo(stud)));
	}

	@Test
	@Order(5)
	void test05_ReadAllFields() {
		//Professor returnProf = getWithId(em, Professor.class, Integer.class, Professor_.id,prof.getId());
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		// create query for Professor
		CriteriaQuery< Student> query = builder.createQuery( Student.class);
		// select p from Professor p
		Root< Student> root = query.from( Student.class);
		query.select( root);
		query.where( builder.equal( root.get( Student_.id), builder.parameter( Integer.class, "id")));
		// create query and set the parameter
		TypedQuery< Student> tq = em.createQuery( query);
		tq.setParameter( "id", stud.getId());
		// get the result as row count
		Student returnStud = tq.getSingleResult();

		assertThat(returnStud.getFirstName(), equalTo(FirstName));
		assertThat(returnStud.getLastName(), equalTo(LastName));
		//assertThat(returnProf.getDepartment(), equalTo(Department));
		
	}

	@Test
	@Order(6)
	void test06_Update() {
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		// create query for Professor
		CriteriaQuery< Student> query = builder.createQuery( Student.class);
		// select p from Professor p
		Root< Student> root = query.from( Student.class);
		query.select( root);
		query.where( builder.equal( root.get( Student_.id), builder.parameter( Integer.class, "id")));
		// create query and set the parameter
		TypedQuery< Student> tq = em.createQuery( query);
		tq.setParameter( "id", stud.getId());
		// get the result as row count
		Student returnStud= tq.getSingleResult();
		
		String newFirstName = "Zeme";
		String newLastName = "Greg";
		//String newDepartment = "Biology";
		
		et.begin();
		returnStud.setFirstName(newFirstName);
		returnStud.setLastName(newLastName);
		//returnProf.setDepartment(newDepartment);
		em.merge(returnStud);
		
		et.commit();
		//returnProf = getWithId(em,Professor.class, Integer.class, Professor_.id,prof.getId());

		 returnStud= tq.getSingleResult();

		assertThat(returnStud.getFirstName(), equalTo(newFirstName));
		assertThat(returnStud.getLastName(), equalTo(newLastName));
		//assertThat(returnProf.getDepartment(), equalTo(newDepartment));
	}

	
	@Test
	@Order(7)
	void test07_Delete() {
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		// create query for Professor
		CriteriaQuery< Student> query = builder.createQuery( Student.class);
		// select p from Professor p
		Root< Student> root = query.from( Student.class);
		query.select( root);
		query.where( builder.equal( root.get( Student_.id), builder.parameter( Integer.class, "id")));
		// create query and set the parameter
		TypedQuery< Student> tq = em.createQuery( query);
		tq.setParameter( "id", stud.getId());
		// get the result as row count
		Student returnStud= tq.getSingleResult();
		
		et.begin();
		Student proftest = new Student();

		String newFirstName = "Jojo";
		String newLastName = "Asun";
		String newDepartment = "Chemistry";
		
		
		proftest.setFirstName(newFirstName);
		proftest.setLastName(newLastName);
		//proftest.setDepartment(newDepartment);
		
		em.persist(proftest);
		
		et.commit();
		
		et.begin();
		em.remove(returnStud);
		et.commit();
		
		//long result = getCountWithId(em,Professor.class,Integer.class, Professor_.id, returnProf.getId());
		long result = getTotalCount(em, Student.class);
		assertThat(result, is(equalTo(1L)));
		
		//result = getCountWithId(em, Professor.class, Integer.class, Professor_.id, proftest.getId());
		result = getTotalCount(em, Student.class);
				assertThat(result, is(equalTo(1L)));
	}
}
