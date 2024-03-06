package app.domain.contact;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contacts")
public class Contact {

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
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "Phone")
    private String phone;

    @Override
    public String toString() {
        return "{" +
                "\"id\" : " + id +
                ", \"firstName\" : \"" + firstName + "\"" +
                ",  \"phone\" : \"" + phone + "\"" +
                "}";
    }
}
