package acmecollege.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-07-22T04:09:56.475+0100")
@StaticMetamodel(CourseRegistration.class)
public class CourseRegistration_ extends PojoBaseCompositeKey_ {
	public static volatile SingularAttribute<CourseRegistration, CourseRegistrationPK> id;
	public static volatile SingularAttribute<CourseRegistration, Student> student;
	public static volatile SingularAttribute<CourseRegistration, Course> course;
	public static volatile SingularAttribute<CourseRegistration, Professor> professor;
	public static volatile SingularAttribute<CourseRegistration, Integer> numericGrade;
	public static volatile SingularAttribute<CourseRegistration, String> letterGrade;
}
