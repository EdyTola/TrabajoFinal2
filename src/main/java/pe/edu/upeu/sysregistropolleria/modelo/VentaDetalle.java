package pe.edu.upeu.sysregistropolleria.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "upeu_venta_detalle")
public class VentaDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta_detalle")
    private Long idVentaDetalle;
    @Column(name = "pu", nullable = false)//id_reserva
    private Double pu;
    @Column(name = "cantidad", nullable = false)
    private Double cantidad;
    @Column(name = "descuento", nullable = false)
    private Double descuento;
    @Column(name = "subtotal", nullable = false)
    private Double subtotal;
    @ManyToOne
    @JoinColumn(name = "id_venta", referencedColumnName =
            "id_venta",
            nullable = false, foreignKey = @ForeignKey(name =
            "FK_VENTA_VENTADETALLE"))
    private Venta venta;
    @ManyToOne
    @JoinColumn(name = "id_producto", referencedColumnName =
            "id_producto",
            nullable = false, foreignKey = @ForeignKey(name =
            "FK_PRODUCTO_VENTADETALLE"))
    private Producto producto;

    //@Table(name = "upeu_reserva")  // Asegúrate que esta tabla exista en tu base de datos
    //public class Reserva {
    //    @Id
    //    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //    @Column(name = "id_reserva")
    //    private Long idReserva;
    //
    //    @Column(name = "nombre", nullable = false, length = 100)
    //    private String nombre;
}