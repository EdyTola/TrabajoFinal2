package pe.edu.upeu.sysregistropolleria.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.sysregistropolleria.dto.ComboBoxOption;
import pe.edu.upeu.sysregistropolleria.modelo.Reserva;
import pe.edu.upeu.sysregistropolleria.repositorio.ReservaRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReservaService {

    @Autowired
    ReservaRepository repo;
    public Reserva save(Reserva to){return repo.save(to);
    }
    public List<Reserva> list() {return repo.findAll();
    }
    public Reserva update(Reserva to, Long id){
        try {
            Reserva toe=repo.findById(id).get();
            if(toe!=null){
                toe.setNombre(to.getNombre());
            }
            return repo.save(toe);
        }catch (Exception e){
            System.out.println("Error: "+ e.getMessage());
        }
        return null;
    }

    public Reserva update(Reserva to){return repo.save(to);
    }
    public void delete(Long id){repo.deleteById(id);
    }
    public Reserva searchById(Long id){return repo.findById(id).orElse(null);
    }

    public List<ComboBoxOption> listarCombobox(){
        List<ComboBoxOption> listar=new ArrayList<>();
        ComboBoxOption cb;
        for(Reserva cate : repo.findAll()) {
            cb=new ComboBoxOption();
            cb.setKey(String.valueOf(cate.getIdReserva()));
            cb.setValue(cate.getNombre());
            listar.add(cb);
        }
        return listar;
    }
}
