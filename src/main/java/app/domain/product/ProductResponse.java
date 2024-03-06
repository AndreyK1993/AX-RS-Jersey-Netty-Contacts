package app.domain.product;

public class ProductResponse {

    private final Product data;

    public ProductResponse(Product data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return " \"data\" : " + data;
    }
}
