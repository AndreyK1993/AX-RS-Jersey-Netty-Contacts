package app.service.impl;

import app.domain.contact.Contact;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import app.domain.contact.ContactResponse;
import app.domain.contact.ContactsResponse;
import app.repository.impl.ContactRepository;
import app.service.AppService;
import app.utils.ResponseMessage;

import java.util.List;
import java.util.Optional;

// Клас RESTful веб-сервісу,
// який надає REST API для клієнтів.
@Path("/api/v1/contacts")
@Produces({MediaType.APPLICATION_JSON})
public class ContactService implements AppService<Contact> {

    ContactRepository repository = new ContactRepository();

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(Contact contact) {
        repository.create(contact);
        Optional<Contact> optional = repository.getLastContact();
        ContactResponse response;
        if (optional.isPresent()) {
            response =
                    new ContactResponse(optional.get());
            return Response.ok(response.toString())
                    .status(Response.Status.CREATED).build();
        } else {
            return Response.ok(null)
                    .status(Response.Status.NO_CONTENT).build();
        }
    }

    @GET
    public Response fetchAll() {
        Optional<List<Contact>> optional = repository.fetchAll();
        ContactsResponse response;
        if (optional.isEmpty()) {
            return Response.noContent().build();
        } else {
            response = new ContactsResponse(optional.get());
            return Response.ok(response.toString()).build();
        }
    }


    // ---- Path Param ----------------------

    @GET
    @Path("{id: [0-9]+}")
    public Response fetchById(@PathParam("id") Long id) {
        Optional<Contact> optional = repository.fetchById(id);
        if (optional.isPresent()) {
            ContactResponse response =
                    new ContactResponse(optional.get());
            return Response.ok(response.toString())
                    .status(Response.Status.CREATED).build();
        } else {
            ResponseMessage resMsg =
                    new ResponseMessage(404, false,
                            "Entity not found.");
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(resMsg.toString()).build();
        }
    }

    @PUT
    @Path("{id: [0-9]+}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response update(@PathParam("id") Long id, Contact contact) {
        Optional<Contact> optional = repository.fetchById(id);
        if (optional.isPresent()) {
            repository.update(id, contact);
            ContactResponse response =
                    new ContactResponse(optional.get());
            return Response.ok(response.toString())
                    .status(Response.Status.CREATED).build();
        } else {
            ResponseMessage respMsg =
                    new ResponseMessage(404, false,
                            "Entity not found.");
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(respMsg.toString()).build();
        }
    }

    @DELETE
    @Path("{id: [0-9]+}")
    public Response delete(@PathParam("id") Long id) {
        if (repository.isIdExists(id)) {
            repository.delete(id);
            ResponseMessage respMsg =
                    new ResponseMessage(200, true,
                            "Entity deleted.");
            return Response.ok(respMsg.toString()).build();
        } else {
            ResponseMessage respMsg =
                    new ResponseMessage(404, false,
                            "Entity not found.");
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(respMsg.toString()).build();
        }
    }


    // ---- Query Params ----------------------

    /*
        http://localhost:8081/api/v1/users/query-by-firstname?firstName=Tom
    */
    @GET
    @Path("/query-by-firstname")
    public Response getContactsByFirstName(@QueryParam("firstName") String firstName) {
        Optional<List<Contact>> optional = repository.fetchByFirstName(firstName);
        if (optional.isEmpty()) {
            return Response.noContent().build();
        }
        ContactsResponse response = new ContactsResponse(optional.get());
        return Response.ok(response.toString()).build();
    }

    /*
        http://localhost:8081/api/v1/users/query-order-by?orderBy=firstName

    */
    @GET
    @Path("/query-order-by")
    public Response getContactsOrderBy(
            @QueryParam("orderBy") String orderBy
    ) {
        Optional<List<Contact>> optional = repository.fetchAllOrderBy(orderBy);
        if (optional.isEmpty()) {
            return Response.noContent().build();
        }
        ContactsResponse response = new ContactsResponse(optional.get());
        return Response.ok(response.toString()).build();
    }

    /*
        http://localhost:8081/api/v1/users/query-between-ids?from=3&to=6
    */
    @GET
    @Path("/query-between-ids")
    public Response getContactsBetweenIds(
            @QueryParam("from") int from,
            @QueryParam("to") int to
    ) {
        Optional<List<Contact>> optional = repository.fetchBetweenIds(from, to);
        if (optional.isEmpty()) {
            return Response.noContent().build();
        }
        ContactsResponse response = new ContactsResponse(optional.get());
        return Response.ok(response.toString()).build();
    }

}
