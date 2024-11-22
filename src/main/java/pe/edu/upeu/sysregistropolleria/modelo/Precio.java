package pe.edu.upeu.sysregistropolleria.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


//UnidadMedida
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "upeu_precio")  // Asegúrate de que esta tabla exista en la base de datos
public class Precio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_precio")  // id_precio
    private Long idPrecio;

    @Column(name = "nombre_precio", nullable = false, length = 60)  // nombre_precio
    private String nombrePrecio;  // nombre_precio (representa el nombre del precio o la unidad)

    @Column(name = "precio", nullable = false)  // Precio en formato numérico
    private double precio;  // precio unitario
}