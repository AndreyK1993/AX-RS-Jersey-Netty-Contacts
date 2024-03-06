package app.domain.product;

import jakarta.persistence.*;

// Lombok НЕ застосовується
@Entity
@Table(name = "products")
public class Product {

    // @Id
    // Визначає первинний ключ об'єкта.
    //
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Для автоматичного генерування значення первинного ключа.
    // Визначає стратегію генерації значень первинних ключів.
    // GenerationType.IDENTITY вказує, що первинні ключі для сутності
    // повинні призначатися, використовуючи стовпець ідентифікації БД.
    // Вони автоматично збільшуються.
    //
    // @Column (name = "id")
    // Вказує зіставлення стовпців в БД.
    // Атрибут name використовується для вказівки імені стовпця таблиці.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // Тут, найменування стовпця в БД
    // не збігається із найменуванням змінної.
    // Атрибут name вирішує проблему.
    @Column(name = "name")
    private String productName;

    @Column(name = "measure")
    private String measure;

    @Column(name = "quota")
    private Double quota;

    @Column(name = "price")
    private Double price;

    public Product() {
    }

    public Product(Long id, String productName, String measure,
                   Double quota, Double price) {
        this.id = id;
        this.productName = productName;
        this.measure = measure;
        this.quota = quota;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public Double getQuota() {
        return quota;
    }

    public void setQuota(Double quota) {
        this.quota = quota;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\" : " + id +
                ", \"productName\" : \"" + productName + "\"" +
                ", \"measure\" : \"" + measure + "\"" +
                ", \"quota\" : \"" + quota + "\"" +
                ", \"price\" : \"" + price + "\"" +
                "}";
    }
}
