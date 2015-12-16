package se.travappar.api.utils.converter;

public interface Converter<E, D> {

    D convertToDTO(E e);

    E convertToEntity(D d);
}
