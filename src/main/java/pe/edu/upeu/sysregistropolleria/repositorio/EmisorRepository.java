package pe.edu.upeu.sysregistropolleria.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.sysregistropolleria.modelo.Emisor;

@Repository
public interface EmisorRepository extends JpaRepository <Emisor, Long> {
}
