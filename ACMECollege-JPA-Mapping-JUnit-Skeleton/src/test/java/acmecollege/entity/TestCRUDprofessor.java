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
public class TestCRUDprofessor extends JUnitBase {
	
	private EntityManager em;
	private EntityTransaction et;
	
	private static Professor prof;
	private static final String FirstName = "Mosun";
	private static final String LastName = "Owola";
	private static final String Department = "Mathematics";
	
	
	@BeforeAll
	static void setupAllInit()
	{
		prof = new Professor();
		prof.setProfessor(FirstName, LastName, Department);
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

		
		long result = getTotalCount(em, Professor.class);
		assertThat( result, is( comparesEqualTo( 0L)));
		//assertThat( result, is( comparesEqualTo( 1L)));

	}

	@Test
	@Order(2)
	void test02_Create() {
		et.begin();
		prof = new Professor();
		prof.setFirstName(FirstName);
		prof.setLastName(LastName);
		prof.setDepartment(Department);
		prof.setCreated(LocalDateTime.now());
		
		em.persist(prof);
		et.commit();

		//Long result = getCountWithId(em,Professor.class,Integer.class,Professor_.id, prof.getId());

		long result = getTotalCount(em, Professor.class);
		
		// There should only be one row in the DB
		assertThat(result, is(greaterThanOrEqualTo(1L)));
//		assertEquals(result, 1);
	}

	@Test
	@Order(3)
	void test03_CreateInvalid() {
		et.begin();
		
		Professor testProf = new Professor();
		testProf.setDepartment("Arts & Crafts");
		assertThrows(PersistenceException.class, () -> em.persist(testProf));
		et.commit();
	}

	@Test
	@Order(4)
	void test04_Read() {
		
	
		
		List<Professor> profsAll = getAll(em,Professor.class);
		assertThat(profsAll, contains(equalTo(prof)));
	}

	@Test
	@Order(5)
	void test05_ReadAllFields() {
		//Professor returnProf = getWithId(em, Professor.class, Integer.class, Professor_.id,prof.getId());
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		// create query for Professor
		CriteriaQuery< Professor> query = builder.createQuery( Professor.class);
		// select p from Professor p
		Root< Professor> root = query.from( Professor.class);
		query.select( root);
		//query.where( builder.equal( root.get( Professor_.id), builder.parameter( Integer.class, "id")));
		// create query and set the parameter
		TypedQuery< Professor> tq = em.createQuery( query);
		//tq.setParameter( "id", prof.getId());
		// get the result as row count
		Professor returnProf= tq.getSingleResult();

		assertThat(returnProf.getFirstName(), equalTo(FirstName));
		assertThat(returnProf.getLastName(), equalTo(LastName));
		assertThat(returnProf.getDepartment(), equalTo(Department));
		
	}

	@Test
	@Order(6)
	void test06_Update() {
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		// create query for Professor
		CriteriaQuery< Professor> query = builder.createQuery( Professor.class);
		// select p from Professor p
		Root< Professor> root = query.from( Professor.class);
		query.select( root);
		//query.where( builder.equal( root.get( Professor_.id), builder.parameter( Integer.class, "id")));
		// create query and set the parameter
		TypedQuery< Professor> tq = em.createQuery( query);
		//tq.setParameter( "id", prof.getId());
		// get the result as row count
		Professor returnProf= tq.getSingleResult();
		
		String newFirstName = "Toyi";
		String newLastName = "Segun";
		String newDepartment = "Biology";
		
		et.begin();
		returnProf.setFirstName(newFirstName);
		returnProf.setLastName(newLastName);
		returnProf.setDepartment(newDepartment);
		em.merge(returnProf);
		
		et.commit();
		//returnProf = getWithId(em,Professor.class, Integer.class, Professor_.id,prof.getId());

		 returnProf= tq.getSingleResult();

		assertThat(returnProf.getFirstName(), equalTo(newFirstName));
		assertThat(returnProf.getLastName(), equalTo(newLastName));
		assertThat(returnProf.getDepartment(), equalTo(newDepartment));
	}

	
	@Test
	@Order(7)
	void test07_Delete() {
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		// create query for Professor
		CriteriaQuery< Professor> query = builder.createQuery( Professor.class);
		// select p from Professor p
		Root< Professor> root = query.from( Professor.class);
		query.select( root);
		query.where( builder.equal( root.get( Professor_.id), builder.parameter( Integer.class, "id")));
		// create query and set the parameter
		TypedQuery< Professor> tq = em.createQuery( query);
		tq.setParameter( "id", prof.getId());
		// get the result as row count
		Professor returnProf= tq.getSingleResult();
		
		et.begin();
		Professor proftest = new Professor();

		String newFirstName = "Jojo";
		String newLastName = "Asun";
		String newDepartment = "Chemistry";
		
		
		proftest.setFirstName(newFirstName);
		proftest.setLastName(newLastName);
		proftest.setDepartment(newDepartment);
		
		em.persist(proftest);
		
		et.commit();
		
		et.begin();
		em.remove(returnProf);
		et.commit();
		
		//long result = getCountWithId(em,Professor.class,Integer.class, Professor_.id, returnProf.getId());
		long result = getTotalCount(em, Professor.class);
		assertThat(result, is(equalTo(1L)));
		
		//result = getCountWithId(em, Professor.class, Integer.class, Professor_.id, proftest.getId());
		result = getTotalCount(em, Professor.class);
				assertThat(result, is(equalTo(1L)));
	}
	
	

}
