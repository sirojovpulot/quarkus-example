package org.acme.controller;

import org.acme.model.Book;
import org.acme.repository.BookRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("/book")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookController {

    @Inject
    BookRepository bookRepository;

    @GET
    public Response getBooks() {
        List<Book> books = bookRepository.listAll();
        return Response.ok(books).build();
    }

    @GET
    @Path("{id}")
    public Response getId(@PathParam("id") Long id) {
        return bookRepository.findByIdOptional(id)
                .map(b -> Response.ok(b).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Path("author/{author}")
    public Response getByAuthor(@PathParam("author") String author) {
        List<Book> books = bookRepository.find("author", author).list();
        return Response.ok(books).build();
    }

    @GET
    @Path("name/{name}")
    public Response getByName(@PathParam("name") String name) {
        List<Book> books = bookRepository.findByName(name);
        return Response.ok(books).build();
    }

    @POST
    @Transactional
    public Response create(Book book) {
        bookRepository.persist(book);
        if (bookRepository.isPersistent(book)) {
            return Response.created(URI.create("/book/" + book.getId())).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteById(@PathParam("id") Long id) {
        boolean deleted = bookRepository.deleteById(id);
        if (deleted) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
