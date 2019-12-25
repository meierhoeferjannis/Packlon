package de.oth.Packlon.entity;

import javax.persistence.*;

@Entity
public class AccountRole extends SingelIdEntity<Integer> {


    @ManyToOne(fetch = FetchType.EAGER)
    private Account account;
    @ManyToOne(fetch = FetchType.EAGER)
    private Role role;
    public Account getAccount() {

        return account;
    }
    public void setAccount(Account account) {
        this.account = account;
    }
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }
}
