package sample;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * Created by pwilkin on 30-Nov-17.
 */
@Entity
@Table(name = "STUDENTS")
public class Student implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    protected Integer id;

    @Column(name = "NAME")
    protected String name;

    @Column(name = "SURNAME")
    protected String surname;

    @Column(name = "PESEL")
    protected String pesel;

    @Column(name = "IDX")
    protected String idx;

    @ManyToMany
    @JoinTable(name = "GROUPS_FOR_STUDENTS", joinColumns = @JoinColumn(name = "STUDENT_ID"), inverseJoinColumns = @JoinColumn(name = "GROUP_ID"))
    protected List<Grupa> groups;

    public List<Grupa> getGroups() {
        return groups;
    }

    public void setGroups(List<Grupa> groups) {
        this.groups = groups;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }
}
