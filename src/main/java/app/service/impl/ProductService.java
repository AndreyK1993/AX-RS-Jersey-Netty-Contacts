package app.service.impl;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import app.domain.product.Product;
import app.domain.product.ProductResponse;
import app.domain.product.ProductsResponse;
import app.repository.impl.ProductRepository;
import app.service.AppService;
import app.utils.ResponseMessage;

import java.util.List;
import java.util.Optional;

// Клас RESTful веб-сервісу,
// який надає REST API для клієнтів.
@Path("/api/v1/products")
@Produces({MediaType.APPLICATION_JSON})
public class ProductService implements AppService<Product> {

    ProductRepository repository = new ProductRepository();

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(Product product) {
        repository.create(product);
        Optional<Product> optional = repository.getLastProduct();
        ProductResponse response;
        if (optional.isPresent()) {
            response =
                    new ProductResponse(optional.get());
            return Response.ok(response.toString())
                    .status(Response.Status.CREATED).build();
        } else {
            return Response.ok(null)
                    .status(Response.Status.NO_CONTENT).build();
        }
    }

    @GET
    public Response fetchAll() {
        Optional<List<Product>> optional = repository.fetchAll();
        ProductsResponse response;
        if (optional.isEmpty()) {
            return Response.noContent().build();
        } else {
            response = new ProductsResponse(optional.get());
            return Response.ok(response.toString()).build();
        }
    }

    // ---- Path Param ----------------------

    @GET
    @Path("{id: [0-9]+}")
    public Response fetchById(@PathParam("id") Long id) {
        Optional<Product> optional = repository.fetchById(id);
        if (optional.isPresent()) {
            ProductResponse response =
                    new ProductResponse(optional.get());
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
    public Response update(@PathParam("id") Long id, Product product) {
        Optional<Product> optional = repository.fetchById(id);
        if (optional.isPresent()) {
            repository.update(id, product);
            ProductResponse response =
                    new ProductResponse(optional.get());
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
}
