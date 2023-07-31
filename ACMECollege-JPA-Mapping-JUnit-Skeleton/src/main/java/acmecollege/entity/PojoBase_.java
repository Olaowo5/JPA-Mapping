package acmecollege.entity;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2023-07-21T01:31:05.264+0100")
@StaticMetamodel(PojoBase.class)
public class PojoBase_ {
	public static volatile SingularAttribute<PojoBase, Integer> id;
	public static volatile SingularAttribute<PojoBase, Integer> version;
	public static volatile SingularAttribute<PojoBase, LocalDateTime> created;
	public static volatile SingularAttribute<PojoBase, LocalDateTime> updated;
}
