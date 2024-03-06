package app.domain.product;

import java.util.List;

public class ProductsResponse {

    private final List<Product> data;

    public ProductsResponse(List<Product> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return " \"data\" : " + data;
    }
}
