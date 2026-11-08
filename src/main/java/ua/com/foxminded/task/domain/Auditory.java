package ua.com.foxminded.task.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "auditories")
public class Auditory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "number")
    private String auditoryNumber;
    @ManyToOne
    @JoinColumn(name = "auditory_type_id")
    private AuditoryType type;
    @Column(name = "capacity")
    private int maxCapacity;
    @Column(name = "description")
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuditoryNumber() {
        return auditoryNumber;
    }

    public void setAuditoryNumber(String auditoryNumber) {
        this.auditoryNumber = auditoryNumber;
    }

    public AuditoryType getAuditoryType() {
        return type;
    }

    public void setType(AuditoryType type) {
        this.type = type;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((auditoryNumber == null) ? 0 : auditoryNumber.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + maxCapacity;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Auditory other = (Auditory) obj;
        if (auditoryNumber == null) {
            if (other.auditoryNumber != null)
                return false;
        } else if (!auditoryNumber.equals(other.auditoryNumber))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (maxCapacity != other.maxCapacity)
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Auditory [id=" + id 
             + ", auditoryNumber=" + auditoryNumber 
             + ", type=" + type 
             + ", maxCapacity=" + maxCapacity 
             + ", description=" + description 
             + "]";
    }


}
