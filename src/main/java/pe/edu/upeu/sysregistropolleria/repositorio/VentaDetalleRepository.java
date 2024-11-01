package pe.edu.upeu.sysregistropolleria.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.sysregistropolleria.modelo.VentaDetalle;

@Repository
public interface VentaDetalleRepository extends JpaRepository <VentaDetalle, Long> {
}
