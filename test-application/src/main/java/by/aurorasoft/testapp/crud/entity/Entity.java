package by.aurorasoft.testapp.crud.entity;

import org.hibernate.Hibernate;

import java.util.Objects;

import static java.util.Objects.hash;

@SuppressWarnings("deprecation")
public abstract class Entity implements by.nhorushko.crudgeneric.v2.domain.AbstractEntity<Long>, by.nhorushko.crudgeneric.domain.AbstractEntity {

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public final boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null) {
            return false;
        }
        if (Hibernate.getClass(this) != Hibernate.getClass(otherObject)) {
            return false;
        }
        Entity other = (Entity) otherObject;
        return Objects.equals(getId(), other.getId());
    }

    @Override
    public final int hashCode() {
        return hash(getId());
    }
}
