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
public class TestCRUDMembershipcard extends JUnitBase{
	private EntityManager em;
	private EntityTransaction et;

	private static ClubMembership clubship;
	private static StudentClub studclub;
	private static Student student;
	private static MembershipCard mcard;
	private static final byte MyByte = 1;
	private static DurationAndStatus Durand;
	

	
	@BeforeAll
	static void setupAllInit() {
		
		student = new Student();
		student.setFullName("John", "Smith");
		
		 studclub = new AcademicStudentClub();
		studclub.setName("Marian");
		 clubship  = new ClubMembership();
		 Durand = new DurationAndStatus();
		 Durand.setStartDate(LocalDateTime.of(2023, 7, 28, 3, 20, 0));
		 Durand.setEndDate(LocalDateTime.of(2024,5, 15, 3, 20));
		clubship.setStudentClub(studclub);
		clubship.setDurationAndStatus(Durand);
		
		
		 mcard = new MembershipCard();
		 mcard.setOwner(student);
		 mcard.setSigned(MyByte);
		// clubship.setCard(mcard);
		mcard.setClubMembership(clubship);
		clubship.setCard(mcard);
		//mcard.setSigned(true);
		
		
		
	}

	@BeforeEach
	void setup() {
		em = getEntityManager();
		et = em.getTransaction();
	}

	@AfterEach
	void tearDown() {
		em.close();
	}

	@Test
	@Order(1)
	void test01_Empty() {

		CriteriaBuilder builder = em.getCriteriaBuilder();
		// Create query for long as we need the number of found rows
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		// Select count(cr) from CourseRegistration cr
		Root<MembershipCard> root = query.from(MembershipCard.class);
		query.select(builder.count(root));
		// Create query and set the parameter
		TypedQuery<Long> tq = em.createQuery(query);
		// Get the result as row count
		long result = tq.getSingleResult();

		assertThat(result, is(comparesEqualTo(0L)));

	}

	@Test
	@Order(2)
	void test02_Create() {
			
		et.begin();
			
		
		
		

		// Persist the MembershipCard
		em.persist(student);
		em.persist(mcard);
		et.commit();

		CriteriaBuilder builder = em.getCriteriaBuilder();
		// Create query for long as we need the number of found rows
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		// Select count(cr) from CourseRegistration cr where cr.id = :id
		Root<MembershipCard> root = query.from(MembershipCard.class);
		query.select(builder.count(root));
		query.where(builder.equal(root.get(MembershipCard_.id), builder.parameter(Integer.class, "id")));
		// Create query and set the parameter
		TypedQuery<Long> tq = em.createQuery(query);
		tq.setParameter("id", mcard.getId());
		// Get the result as row count
		long result = tq.getSingleResult();

		// There should only be one row in the DB
		assertThat(result, is(greaterThanOrEqualTo(1L)));
//		assertEquals(result, 1);
	}

	@Test
	@Order(3)
	void test03_CreateInvalid() {
		et.begin();
		MembershipCard mcard2 = new MembershipCard();
		mcard2.setOwner(student);
		mcard2.setClubMembership(clubship);
		
		//mcard.setSigned(true);
		mcard2.setSigned(MyByte);
//		courseRegistration2.setCourse(course);
		
		// We expect a failure because course is part of the composite key
		assertThrows(PersistenceException.class, () -> em.persist(mcard2));
		et.commit();
	}

	@Test
	@Order(4)
	void test04_Read() {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		// Create query for CourseRegistration
		CriteriaQuery<MembershipCard> query = builder.createQuery(MembershipCard.class);
		// Select cr from CourseRegistration cr
		Root<MembershipCard> root = query.from(MembershipCard.class);
		query.select(root);
		// Create query and set the parameter
		TypedQuery<MembershipCard> tq = em.createQuery(query);
		// Get the result as row count
		List<MembershipCard> mcardss = tq.getResultList();

		assertThat(mcardss, contains(equalTo(mcard)));
	}

	@Test
	@Order(5)
	void test05_ReadDependencies() {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		// Create query for CourseRegistration
		CriteriaQuery<MembershipCard> query = builder.createQuery(MembershipCard.class);
		// Select cr from CourseRegistration cr
		Root<MembershipCard> root = query.from(MembershipCard.class);
		query.select(root);
		query.where(builder.equal(root.get(MembershipCard_.id), builder.parameter(Integer.class, "id")));
		
		// Create query and set the parameter
		TypedQuery<MembershipCard> tq = em.createQuery(query);
		tq.setParameter("id", mcard.getId());
		// Get the result as row count
		MembershipCard returnedMcard = tq.getSingleResult();

		assertThat(returnedMcard.getOwner(), equalTo(student));
		assertThat(returnedMcard.getSigned(), equalTo(MyByte));
		assertThat(returnedMcard.getClubMembership(), equalTo(clubship));
		
	}

	@Test
	@Order(6)
	void test06_Update() {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		// Create query for Contact
		CriteriaQuery<MembershipCard> query = builder.createQuery(MembershipCard.class);
		// Select cr from Contact cr
		Root<MembershipCard> root = query.from(MembershipCard.class);
		query.select(root);
		query.where(builder.equal(root.get(MembershipCard_.id), builder.parameter(Integer.class, "id")));
		// Create query and set the parameter
		TypedQuery<MembershipCard> tq = em.createQuery(query);
		tq.setParameter("id", mcard.getId());
		// Get the result as row count
		MembershipCard returnedmcard = tq.getSingleResult();

		Byte newByte = 0;
		

		et.begin();
		returnedmcard.setSigned(newByte);
		
		em.merge(returnedmcard);
		et.commit();

		returnedmcard = tq.getSingleResult();

		assertThat(returnedmcard.getSigned(), equalTo(newByte));
		
	}

	@Test
	@Order(7)
	void test07_UpdateDependencies() {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		// Create query for CourseRegistration
		CriteriaQuery<MembershipCard> query = builder.createQuery(MembershipCard.class);
		// Select cr from Contact cr where cr.id = :id
		Root<MembershipCard> root = query.from(MembershipCard.class);
		query.select(root);
		query.where(builder.equal(root.get(MembershipCard_.id), builder.parameter(Integer.class, "id")));
		// Create query and set the parameter
		TypedQuery<MembershipCard> tq = em.createQuery(query);
		tq.setParameter("id", mcard.getId());
		// Get the result as row count
		MembershipCard returnedmcard = tq.getSingleResult();

		student = returnedmcard.getOwner();
		student.setFullName("Bob", "Builder");

		clubship = returnedmcard.getClubMembership();
		studclub = clubship.getStudentClub();
		
		studclub.setName("boxing");
		clubship.setStudentClub(studclub);
		clubship.setUpdated(LocalDateTime.now());
		
		
		

		

		et.begin();
		returnedmcard.setOwner(student);
		returnedmcard.setSigned(MyByte);
		returnedmcard.setClubMembership(clubship);
		returnedmcard.setUpdated(LocalDateTime.now());
		//em.merge(student);
		em.merge(returnedmcard);
		et.commit();

		returnedmcard = tq.getSingleResult();

		assertThat(returnedmcard.getOwner(), equalTo(student));
		assertThat(returnedmcard.getClubMembership(), equalTo(clubship));
		
	}

	@Test
	@Order(8)
	void test08_DeleteDependecy() {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		// Create query for CourseRegistration
		CriteriaQuery<MembershipCard> query = builder.createQuery(MembershipCard.class);
		// Select cr from CourseRegistration cr
		Root<MembershipCard> root = query.from(MembershipCard.class);
		query.select(root);
		query.where(builder.equal( root.get(MembershipCard_.id), builder.parameter(Integer.class, "id")));
		// Create query and set the parameter
		TypedQuery<MembershipCard> tq = em.createQuery(query);
		tq.setParameter("id", mcard.getId());
		// Get the result as row count
		MembershipCard returnedmcard = tq.getSingleResult();

		int clubId = returnedmcard.getClubMembership().getId();

		et.begin();
		returnedmcard.setClubMembership(null);
		em.merge(returnedmcard);
		et.commit();

		returnedmcard = tq.getSingleResult();

		assertThat(returnedmcard.getClubMembership(), is(nullValue()));

		// Create query for long as we need the number of found rows
		CriteriaQuery<Long> query2 = builder.createQuery(Long.class);
		// Select count(p) from Professor p where p.id = :id
		Root<ClubMembership> root2 = query2.from(ClubMembership.class);
		query2.select(builder.count(root2));
		query2.where(builder.equal(root2.get(ClubMembership_.id), builder.parameter(Integer.class, "id")));
		// Create query and set the parameter
		TypedQuery<Long> tq2 = em.createQuery(query2);
		tq2.setParameter("id", clubId);
		// Get the result as row count
		long result = tq2.getSingleResult();
		// Because it can be null so it is not removed
		assertThat(result, is(equalTo(1L)));
	}

	@Test
	@Order(9)
	void test09_Delete() {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		// Create query for Contact
		CriteriaQuery<MembershipCard> query = builder.createQuery(MembershipCard.class);
		// Select cr from CourseRegistration cr
		Root<MembershipCard> root = query.from(MembershipCard.class);
		query.select(root);
		query.where(builder.equal(root.get(MembershipCard_.id), builder.parameter(Integer.class, "id")));
		// Create query and set the parameter
		TypedQuery<MembershipCard> tq = em.createQuery(query);
		tq.setParameter("id", mcard.getId());
		// Get the result as row count
		MembershipCard returnedmcard = tq.getSingleResult();

		et.begin();
		// Add another row to db to make sure only the correct row is deleted
		MembershipCard Mard2 = new MembershipCard();
		Student studn = new Student();
		studn.setFullName("Momo", "Madagau");
		Mard2.setOwner(studn);
		Mard2.setSigned(false);
		//ClubMembership Chip  = new ClubMembership();
		//Chip = returnedmcard.getClubMembership();
		//Chip.setCard(Mard2);
		Mard2.setClubMembership(returnedmcard.getClubMembership());
		
		em.persist(studn);
		em.persist(Mard2);
		et.commit();

		et.begin();
		em.remove(returnedmcard);
		et.commit();

		// Create query for long as we need the number of found rows
		CriteriaQuery<Long> query2 = builder.createQuery(Long.class);
		// Select count(p) from Professor p where p.id = :id
		Root<MembershipCard> root2 = query2.from(MembershipCard.class);
		query2.select(builder.count(root2));
		query2.where(builder.equal(root2.get(MembershipCard_.id), builder.parameter(Integer.class, "id")));
		// Create query and set the parameter
		TypedQuery<Long> tq2 = em.createQuery(query2);
		tq2.setParameter("id", returnedmcard.getId());
		// Get the result as row count
		long result = tq2.getSingleResult();
		assertThat(result, is(equalTo(0L)));

		// Create query and set the parameter
		TypedQuery<Long> tq3 = em.createQuery(query2);
		tq3.setParameter("id", Mard2.getId());
		// Get the result as row count
		result = tq3.getSingleResult();
		assertThat(result, is(equalTo(1L)));
	}
}
