package se.travappar.api.dal;

import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;
import se.travappar.api.model.CommonEntity;
import se.travappar.api.model.filter.Filtering;

import java.util.Collection;
import java.util.List;

public class AbstractDAO<T extends CommonEntity> extends HibernateDaoSupport {

    protected String id_column = "id";

    Class<T> aClass;

    public AbstractDAO(Class<T> aClass) {
        this.aClass = aClass;
    }

    @Transactional
    public List<T> getList(List<Filtering> filteringList) {
        StringBuilder stringBuilder = getWhereFiltering(filteringList);
        List<?> list = getHibernateTemplate().find("from " + aClass.getSimpleName() + stringBuilder.toString());
        return (List<T>) list;
    }

    @Transactional
    public void mergeList(Collection<T> list) {
        for (T t : list) {
            getHibernateTemplate().merge(t);
        }
    }

    @Transactional
    public void saveList(Collection<T> list) {
        for (T t : list) {
            getHibernateTemplate().save(t);
        }
    }

    @Transactional
    public T create(T entity) {
        getHibernateTemplate().persist(entity);
        return entity;
    }

    @Transactional
    public T update(T entity) {
        getHibernateTemplate().update(entity);
        return entity;
    }

    @Transactional
    public void delete(T entity) {
        getHibernateTemplate().delete(entity);
    }

    @Transactional
    public T get(Long id) {
        List<?> list = getHibernateTemplate().find("from " + aClass.getSimpleName() + " where " + id_column + "=?", id);
        if (list.size() > 0) {
            return (T) list.get(0);
        }
        return null;
    }

    protected StringBuilder getWhereFiltering(List<Filtering> filteringList) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (filteringList != null && !filteringList.isEmpty()) {
            stringBuilder.append(" WHERE ");
            for (Filtering filtering : filteringList) {
                String relation = "";
                if (filtering.getRelationWithPrevious() != null) {
                    relation = filtering.getRelationWithPrevious();
                }
                stringBuilder.append(" ").append(relation).append(" ")
                        .append(filtering.getColumnName()).append(" ")
                        .append(filtering.getOperator()).append(" ")
                        .append(filtering.getValue()).append(" ");
            }
        }
        return stringBuilder;
    }
}
