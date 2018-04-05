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
@Table(name = "GROUPS")
public class Grupa implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    protected Integer id;

    @Column(name = "NAME")
    protected String name;

    @Column(name = "DESCRIPTION")
    protected String description;

    @ManyToMany
    @JoinTable(name = "GROUPS_FOR_STUDENTS", joinColumns = @JoinColumn(name = "GROUP_ID"), inverseJoinColumns = @JoinColumn(name = "STUDENT_ID"))
    protected List<Student> students;

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name + " - " + description;
    }
}
