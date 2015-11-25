package se.travappar.api.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

public class HibernateObjectMapper extends ObjectMapper {

    public HibernateObjectMapper() {
        registerModule(new Hibernate5Module());
    }
}
