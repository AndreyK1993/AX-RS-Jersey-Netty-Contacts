package app.service.impl;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import app.domain.user.User;
import app.domain.user.UserResponse;
import app.domain.user.UsersResponse;
import app.repository.impl.UserRepository;
import app.service.AppService;
import app.utils.ResponseMessage;

import java.util.List;
import java.util.Optional;

// Клас RESTful веб-сервісу,
// який надає REST API для клієнтів.
@Path("/api/v1/users")
@Produces({MediaType.APPLICATION_JSON})
public class UserService implements AppService<User> {

    UserRepository repository = new UserRepository();

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(User user) {
        repository.create(user);
        Optional<User> optional = repository.getLastUser();
        UserResponse response;
        if (optional.isPresent()) {
            response =
                    new UserResponse(optional.get());
            return Response.ok(response.toString())
                    .status(Response.Status.CREATED).build();
        } else {
            return Response.ok(null)
                    .status(Response.Status.NO_CONTENT).build();
        }
    }

    @GET
    public Response fetchAll() {
        Optional<List<User>> optional = repository.fetchAll();
        UsersResponse response;
        if (optional.isEmpty()) {
            return Response.noContent().build();
        } else {
            response = new UsersResponse(optional.get());
            return Response.ok(response.toString()).build();
        }
    }


    // ---- Path Param ----------------------

    @GET
    @Path("{id: [0-9]+}")
    public Response fetchById(@PathParam("id") Long id) {
        Optional<User> optional = repository.fetchById(id);
        if (optional.isPresent()) {
            UserResponse response =
                    new UserResponse(optional.get());
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
    public Response update(@PathParam("id") Long id, User user) {
        Optional<User> optional = repository.fetchById(id);
        if (optional.isPresent()) {
            repository.update(id, user);
            UserResponse response =
                    new UserResponse(optional.get());
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
    public Response getUsersByFistName(@QueryParam("firstName") String firstName) {
        Optional<List<User>> optional = repository.fetchByFirstName(firstName);
        if (optional.isEmpty()) {
            return Response.noContent().build();
        }
        UsersResponse response = new UsersResponse(optional.get());
        return Response.ok(response.toString()).build();
    }

    /*
        http://localhost:8081/api/v1/users/query-by-lastname?lastName=Bright
        http://localhost:8081/api/v1/users/query-by-lastname?lastName=Terra
    */
    @GET
    @Path("/query-by-lastname")
    public Response getUsersByLastName(@QueryParam("lastName") String lastName) {
        Optional<List<User>> optional = repository.fetchByLastName(lastName);
        if (optional.isEmpty()) {
            return Response.noContent().build();
        }
        UsersResponse response = new UsersResponse(optional.get());
        return Response.ok(response.toString()).build();
    }

    /*
        http://localhost:8081/api/v1/users/query-order-by?orderBy=firstName
        http://localhost:8081/api/v1/users/query-order-by?orderBy=lastName
    */
    @GET
    @Path("/query-order-by")
    public Response getUsersOrderBy(
            @QueryParam("orderBy") String orderBy
    ) {
        Optional<List<User>> optional = repository.fetchAllOrderBy(orderBy);
        if (optional.isEmpty()) {
            return Response.noContent().build();
        }
        UsersResponse response = new UsersResponse(optional.get());
        return Response.ok(response.toString()).build();
    }


    /*
        http://localhost:8081/api/v1/users/query-by-lastname-order-by-firstname?lastName=Bright&orderBy=firstName
    */
    @GET
    @Path("/query-by-lastname-order-by-firstname")
    public Response getUsersByLastNameOrderByFirstName(
            @QueryParam("lastName") String lastName,
            @QueryParam("orderBy") String orderBy
    ) {
        Optional<List<User>> optional =
                repository.fetchByLastNameOrderBy(lastName, orderBy);
        if (optional.isEmpty()) {
            return Response.noContent().build();
        }
        UsersResponse response = new UsersResponse(optional.get());
        return Response.ok(response.toString()).build();
    }

    /*
        http://localhost:8081/api/v1/users/query-between-ids?from=3&to=6
    */
    @GET
    @Path("/query-between-ids")
    public Response getUsersBetweenIds(
            @QueryParam("from") int from,
            @QueryParam("to") int to
    ) {
        Optional<List<User>> optional = repository.fetchBetweenIds(from, to);
        if (optional.isEmpty()) {
            return Response.noContent().build();
        }
        UsersResponse response = new UsersResponse(optional.get());
        return Response.ok(response.toString()).build();
    }

    /*
        http://localhost:8081/api/v1/users/query-lastname-in?lastName1=Terra&lastName2=Bright
    */
    @GET
    @Path("/query-lastname-in")
    public Response getUsersLastNameIn(
            @QueryParam("lastName1") String lastName1,
            @QueryParam("lastName2") String lastName2
    ) {
        Optional<List<User>> optional =
                repository.fetchLastNameIn(lastName1, lastName2);
        if (optional.isEmpty()) {
            return Response.noContent().build();
        }
        UsersResponse response = new UsersResponse(optional.get());
        return Response.ok(response.toString()).build();
    }
}
