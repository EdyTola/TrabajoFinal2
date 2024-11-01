package pe.edu.upeu.sysregistropolleria.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.sysregistropolleria.dto.ComboBoxOption;
import pe.edu.upeu.sysregistropolleria.modelo.Precio;
import pe.edu.upeu.sysregistropolleria.repositorio.PrecioRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class PrecioService {

    @Autowired
    PrecioRepository repo;

    //C
    public Precio ave(Precio to){
        return repo.save(to);
    }

    //R
    public List<Precio> list(){
        return repo.findAll();
    }
    //U
    public Precio update(Precio to, Long id){
        try {
            Precio toe=repo.findById(id).get();
            if(toe!=null){
                toe.setNombrePrecio(to.getNombrePrecio());
            }
            return repo.save(toe);
        }catch (Exception e){
            System.out.println("Error: "+ e.getMessage());
        }
        return null;
    }

    public Precio update(Precio to){
        return repo.save(to);
    }

    //D
    public void delete(Long id){
        repo.deleteById(id);
    }
    //B
    public Precio searchById(Long id){
        return repo.findById(id).orElse(null);
    }


    public List<ComboBoxOption> listarCombobox(){
        List<ComboBoxOption> listar=new ArrayList<>();
        ComboBoxOption cb;
        for(Precio cate : repo.findAll()) {
            cb=new ComboBoxOption();
            cb.setKey(String.valueOf(cate.getIdPrecio()));
            cb.setValue(cate.getNombrePrecio());
            listar.add(cb);
        }
        return listar;
    }
}

