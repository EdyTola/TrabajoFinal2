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
@Table(name = "upeu_precio")  // Asegúrate que esta tabla exista en tu base de datos, upeu_unid_medida
public class Precio  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_precio")//id_unidad
    private Long idPrecio;//idUnidad

    @Column(name = "nombre_precio", nullable = false, length = 60)//nombre_medida
    private String nombrePrecio;//nombreMedida
}