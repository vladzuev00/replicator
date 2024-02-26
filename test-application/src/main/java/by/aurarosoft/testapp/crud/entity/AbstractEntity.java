package by.aurarosoft.testapp.crud.entity;

import org.hibernate.Hibernate;

import java.util.Objects;

import static java.util.Objects.hash;

public abstract class AbstractEntity<ID> implements by.nhorushko.crudgeneric.v2.domain.AbstractEntity<ID> {

    @Override
    @SuppressWarnings({"unchecked", "EqualsWhichDoesntCheckParameterClass"})
    public final boolean equals(final Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null) {
            return false;
        }
        if (Hibernate.getClass(this) != Hibernate.getClass(otherObject)) {
            return false;
        }
        final AbstractEntity<ID> other = (AbstractEntity<ID>) otherObject;
        return Objects.equals(getId(), other.getId());
    }

    @Override
    public final int hashCode() {
        return hash(getId());
    }
}
