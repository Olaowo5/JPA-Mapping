/***************************************************************************
 * File:  MembershipCard.java Course materials (23W) CST 8277
 * 
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @date August 28, 2022
 * 
 */
package acmecollege.entity;

import java.io.Serializable;


import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
@SuppressWarnings("unused")

/**
 * The persistent class for the membership_card database table.
 */
//TODO MC01 - Add the missing annotations.
//TODO MC02 - Do we need a mapped super class?  If so, which one?
@Entity
@Table(name = "membership_card")
@NamedQuery(name = MembershipCard.ALL_CARDS_QUERY_NAME, query = "SELECT mc FROM MembershipCard mc left join fetch mc.clubMembership")
@NamedQuery(name = MembershipCard.ID_CARD_QUERY_NAME, query = "SELECT mc FROM MembershipCard mc left join fetch mc.clubMembership where mc.id = :param1")
@AttributeOverride(name = "id", column = @Column(name = "card_id"))
public class MembershipCard extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String ALL_CARDS_QUERY_NAME = "MembershipCard.findAll";
	public static final String ID_CARD_QUERY_NAME = "MembershipCard.findById";
	
	// TODO MC03 - Add annotations for 1:1 mapping.  Changes here should cascade.
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="membership_id", referencedColumnName = "membership_id")
	private ClubMembership clubMembership;

	// TODO MC04 - Add annotations for M:1 mapping.  Changes here should not cascade.
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "student_id", referencedColumnName = "id", nullable =false)
	private Student owner;

	// TODO MC05 - Add annotations.
	@Column(name="signed", columnDefinition = "BIT(1)", nullable = false)
	@Basic(optional = false)
	private byte signed;

	public MembershipCard() {
		super();
	}
	
	public MembershipCard(ClubMembership clubMembership, Student owner, byte signed) {
		this();
		this.clubMembership = clubMembership;
		this.owner = owner;
		this.signed = signed;
	}

	public ClubMembership getClubMembership() {
		return clubMembership;
	}

	public void setClubMembership(ClubMembership clubMembership) {
		this.clubMembership = clubMembership;
		//We must manually set the 'other' side of the relationship (JPA does not 'do' auto-management of relationships)
		if (clubMembership != null) {
			clubMembership.setCard(this);
		}
	}

	public Student getOwner() {
		return owner;
	}

	public void setOwner(Student owner) {
		this.owner = owner;
		//We must manually set the 'other' side of the relationship (JPA does not 'do' auto-management of relationships)
		if (owner != null) {
			owner.getMembershipCards().add(this);
		}
	}

	public byte getSigned() {
		return signed;
	}

	public void setSigned(byte signed) {
		this.signed = signed;
	}

	public void setSigned(boolean signed) {
		this.signed = (byte) (signed ? 0b0001 : 0b0000);
	}
	
	//Inherited hashCode/equals is sufficient for this entity class

}