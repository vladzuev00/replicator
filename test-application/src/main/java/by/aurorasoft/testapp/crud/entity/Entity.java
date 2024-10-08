package by.aurorasoft.testapp.crud.entity;

import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
import org.hibernate.Hibernate;

import java.util.Objects;

import static java.util.Objects.hash;

public abstract class Entity implements AbstractEntity<Long> {

    public abstract Long getId();

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
