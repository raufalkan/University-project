package ua.com.foxminded.task.dao.filter;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.data.jpa.domain.Specification;

import ua.com.foxminded.task.domain.Group;
import ua.com.foxminded.task.domain.Person_;
import ua.com.foxminded.task.domain.Student;
import ua.com.foxminded.task.domain.Student_;
import ua.com.foxminded.task.domain.Teacher;
import ua.com.foxminded.task.domain.TimetableFilters;
import ua.com.foxminded.task.domain.TimetableItem;
import ua.com.foxminded.task.domain.TimetableItem_;

public class TimetableItemSpecification implements Specification<TimetableItem>{

    private static final long serialVersionUID = 8089427019528112580L;
    
    private TimetableFilters filters; 

    public TimetableItemSpecification(TimetableFilters filters) {
        this.filters = filters;
    }

    @Override
    public Predicate toPredicate(Root<TimetableItem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<>();

        if (filters.getStartDate() != null && filters.getEndDate() != null) {
            predicates.add(cb.between(root.get(TimetableItem_.date), filters.getStartDate(), filters.getEndDate()));
        }
        if (filters.getSelectedTeacher() != 0) {
            Join<TimetableItem, Teacher> teacherJoin = root.join(TimetableItem_.teacher, JoinType.INNER);
            predicates.add(cb.equal(teacherJoin.get(Person_.id), filters.getSelectedTeacher()));
        }
        if (filters.getSelectedStudent() != 0) {
            Subquery<Group> subquery = query.subquery(Group.class);
            Root<Student> studentRoot = subquery.from(Student.class);
            subquery.select(studentRoot.get(Student_.group)).where(cb.equal(studentRoot.get(Student_.id), filters.getSelectedStudent()));

            ListJoin<TimetableItem, Group> groupsJoin = root.join(TimetableItem_.groups, JoinType.INNER);
            predicates.add(subquery.in(groupsJoin));
        }
        return cb.and(predicates.toArray(new Predicate[0]));
    }

}
