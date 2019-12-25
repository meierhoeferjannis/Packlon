package de.oth.Packlon.entity;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Role extends SingelIdEntity<Integer> {


    private String role;
    @OneToMany(mappedBy = "role", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    private Set<AccountRole> accountRoles = new HashSet<>();


    public Set<AccountRole> getAccountRoles() {
        return Collections.unmodifiableSet(accountRoles);

    }

    public void setAccountRoles(Set<AccountRole> accountRoles) {
        this.accountRoles = accountRoles;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
