package de.oth.packlon.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Objects;

@MappedSuperclass
public class SingelIdEntity<K> {
    public K getId() {
        return id;
    }

    public void setId(K id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public K id;
    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        final SingelIdEntity other = (SingelIdEntity) o;
        if (!Objects.equals(getId(), other.getId()))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        if (getId() == null)
            return 0;
        else
            return id.hashCode();
// return hashCode()-Methode des @Id-Attributs
    }
}
