package acmecollege.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-07-21T01:31:05.274+0100")
@StaticMetamodel(Professor.class)
public class Professor_ extends PojoBase_ {
	public static volatile SingularAttribute<Professor, String> firstName;
	public static volatile SingularAttribute<Professor, String> lastName;
	public static volatile SingularAttribute<Professor, String> department;
	public static volatile SetAttribute<Professor, CourseRegistration> courseRegistrations;
}
