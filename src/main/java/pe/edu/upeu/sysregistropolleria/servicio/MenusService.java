package pe.edu.upeu.sysregistropolleria.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.sysregistropolleria.dto.ComboBoxOption;
import pe.edu.upeu.sysregistropolleria.modelo.Menus;
import pe.edu.upeu.sysregistropolleria.repositorio.MenusRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenusService {

    @Autowired
    MenusRepository repo;

    //C
    public Menus save(Menus to){
        return repo.save(to);
    }

    //R
    public List<Menus> list(){
        return repo.findAll();
    }
    //U
    public Menus update(Menus to, Long id){
        try {
            Menus toe=repo.findById(id).get();
            if(toe!=null){
                toe.setNombre(to.getNombre());
            }
            return repo.save(toe);
        }catch (Exception e){
            System.out.println("Error: "+ e.getMessage());
        }
        return null;
    }

    public Menus update(Menus to){
        return repo.save(to);
    }

    //D
    public void delete(Long id){
        repo.deleteById(id);
    }
    //B
    public Menus searchById(Long id){
        return repo.findById(id).orElse(null);
    }


    public List<ComboBoxOption> listarCombobox(){
        List<ComboBoxOption> listar=new ArrayList<>();
        ComboBoxOption cb;
        for(Menus cate : repo.findAll()) {
            cb=new ComboBoxOption();
            cb.setKey(String.valueOf(cate.getIdMenu()));
            cb.setValue(cate.getNombre());
            listar.add(cb);
        }
        return listar;
    }
}
