package pe.edu.upeu.sysregistropolleria.servicio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.sysregistropolleria.dto.ModeloDataAutocomplet;
import pe.edu.upeu.sysregistropolleria.modelo.Producto;
import pe.edu.upeu.sysregistropolleria.repositorio.ProductoRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductoService {

    @Autowired
    ProductoRepository repo;
    Logger logger = LoggerFactory.getLogger(ProductoService.class);

    public Producto save(Producto to) {
        return repo.save(to);
    }

    public List<Producto> list() {
        return repo.findAll();
    }

    public Producto update(Producto to, Long id) {
        try {
            Producto toe = repo.findById(id).orElse(null); // Cambiado a orElse(null)
            if (toe != null) {
                toe.setNombre(to.getNombre());
                toe.setMenu(to.getMenu()); // Actualiza el menú si es necesario
                toe.setReserva(to.getReserva()); // Actualiza la reserva si es necesario
                toe.setPrecio(to.getPrecio()); // Actualiza el precio si es necesario
                return repo.save(toe);
            }
        } catch (Exception e) {
            logger.error("Error al actualizar el producto: {}", e.getMessage());
        }
        return null;
    }

    public Producto update(Producto to) {
        return repo.save(to);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public Producto searchById(Long id) {
        return repo.findById(id).orElse(null); // Cambiado a orElse(null)
    }

    public List<ModeloDataAutocomplet> listAutoCompletProducto() {
        List<ModeloDataAutocomplet> listarProducto = new ArrayList<>();
        try {
            for (Producto producto : repo.findAll()) {
                ModeloDataAutocomplet data = new ModeloDataAutocomplet();
                data.setIdx(String.valueOf(producto.getIdProducto()));
                data.setNameDysplay(producto.getNombre());
                // Aquí hemos eliminado las referencias a 'pu' y 'stock' ya que ya no existen en Producto.
                listarProducto.add(data);
            }
        } catch (Exception e) {
            logger.error("Error al realizar la búsqueda", e);
        }
        return listarProducto;
    }
}

