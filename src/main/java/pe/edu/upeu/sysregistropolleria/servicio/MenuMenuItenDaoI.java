package pe.edu.upeu.sysregistropolleria.servicio;

import pe.edu.upeu.sysregistropolleria.dto.MenuMenuItenTO;

import java.util.List;
import java.util.Properties;

public interface MenuMenuItenDaoI {

    public List<MenuMenuItenTO> listaAccesos(String perfil, Properties idioma);

}
