package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.acme.model.Book;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class BookRepository implements PanacheRepository<Book> {
    public List<Book> findByName(String name) {
        return list("select b from Book b where b.name=?1 order by id desc", name);
    }
}
