package acmecollege.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-07-22T15:53:55.294+0100")
@StaticMetamodel(Course.class)
public class Course_ extends PojoBase_ {
	public static volatile SingularAttribute<Course, String> courseCode;
	public static volatile SingularAttribute<Course, String> courseTitle;
	public static volatile SingularAttribute<Course, Integer> year;
	public static volatile SingularAttribute<Course, String> semester;
	public static volatile SingularAttribute<Course, Integer> creditUnits;
	public static volatile SingularAttribute<Course, Byte> online;
	public static volatile SetAttribute<Course, CourseRegistration> courseRegistrations;
}
