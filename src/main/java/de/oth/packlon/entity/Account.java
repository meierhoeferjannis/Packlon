package de.oth.packlon.entity;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;


@Entity
public class Account extends SingelIdEntity<Long> implements UserDetails {

    private String email;
    private String phone;
    private String password;
    @OneToOne
    private Customer owner;
    @ManyToMany
    private List<Delivery> deliveryList;

    @OneToOne
    private Address homeAddress;
    public Account(){

        deliveryList = new ArrayList<Delivery>();
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.EAGER)

    private Set<AccountRole> roles = new HashSet<>();


    public void setRoles(Set<AccountRole> roles) {
        this.roles = roles;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorites = new HashSet<>();
        for (AccountRole accountRole : this.roles) {
            authorites.add(new SimpleGrantedAuthority(accountRole.getRole().getRole()));
        }
        return authorites;
    }


    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Delivery> getDeliveryList() {
        return Collections.unmodifiableList(deliveryList);
    }

    public void setDeliveryList(List<Delivery> deliveryList) {
        this.deliveryList = deliveryList;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Customer getOwner() {
        return owner;
    }

    public void setOwner(Customer owner) {
        this.owner = owner;
    }

    public void addAccountRole(AccountRole accountRole) {
        if (!roles.contains(accountRole))
            this.roles.add(accountRole);
    }

    public void addDelivery(Delivery delivery) {
        if (!deliveryList.contains(delivery)) {
            deliveryList.add(delivery);
        }
    }


    public void removeDelivery(Delivery delivery)
    {
        if(deliveryList.contains(delivery))
            deliveryList.remove(delivery);
    }


}
