package pe.edu.upeu.sysregistropolleria.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "upeu_ncliente")  // Asegúrate que esta tabla exista en tu base de datos
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto") // Verifica que esta columna exista
    private Long idProducto;

    @NotNull(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 120, message = "El nombre debe tener entre 2 y 120 caracteres")
    @Column(name = "nombre", nullable = false, length = 120)
    private String nombre;

    @NotNull(message = "El menú no puede estar vacío")
    @ManyToOne
    @JoinColumn(name = "id_menu", referencedColumnName = "id_menu",
            nullable = false, foreignKey = @ForeignKey(name = "FK_CATEGORIA_PRODUCTO"))
    private Menus menu; // CATEGORÍA

    @NotNull(message = "La reserva no puede estar vacía")
    @ManyToOne
    @JoinColumn(name = "id_reserva", referencedColumnName = "id_reserva", // Cambiado a 'id_reserva'
            nullable = false, foreignKey = @ForeignKey(name = "FK_MARCA_PRODUCTO"))
    private Reserva reserva; // MARCA

    @NotNull(message = "El precio no puede estar vacía")
    @ManyToOne
    @JoinColumn(name = "id_precio", referencedColumnName = "id_precio",
            nullable = false, foreignKey = @ForeignKey(name = "FK_UNIDADMEDIDA_PRODUCTO"))
    private Precio precio; //unidadMedida
}