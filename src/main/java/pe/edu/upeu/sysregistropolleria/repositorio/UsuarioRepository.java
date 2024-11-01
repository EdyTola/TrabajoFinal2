package pe.edu.upeu.sysregistropolleria.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.sysregistropolleria.modelo.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository <Usuario, Long> {
}
