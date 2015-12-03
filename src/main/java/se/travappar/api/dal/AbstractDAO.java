package se.travappar.api.dal;

import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;
import se.travappar.api.model.CommonEntity;

import java.util.Collection;
import java.util.List;

public class AbstractDAO<T extends CommonEntity> extends HibernateDaoSupport {

    Class<T> aClass;

    public AbstractDAO(Class<T> aClass) {
        this.aClass = aClass;
    }

    @Transactional
    public List<T> getList() {
        List<?> list = getHibernateTemplate().find("from " + aClass.getSimpleName() + "");
        return (List<T>) list;
    }

    @Transactional
    public void saveList(Collection<T> list) {
        for(T t : list) {
            getHibernateTemplate().merge(t);
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
        List<?> list = getHibernateTemplate().find("from " + aClass.getSimpleName() + " where id=?", id);
        if(list.size() > 0) {
            return (T) list.get(0);
        }
        return null;
    }
}
