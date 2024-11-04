package pe.edu.upeu.sysregistropolleria.pruebas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.edu.upeu.sysregistropolleria.modelo.Menus;
import pe.edu.upeu.sysregistropolleria.servicio.MenusService;

import java.util.List;
import java.util.Scanner;

@Component
public class MainY {
    @Autowired
    MenusService service;
    public void registro(){
        System.out.println("MAIN CATEGORIA");
        Menus ca=new Menus();
        ca.setNombre("Celulares");
        Menus dd=service.save(ca);
        System.out.println("Reporte:");
        System.out.println(dd.getIdMenu() + "  "+ dd.getNombre());
    }

    public void listar(){
        List<Menus> datos=service.list();
        System.out.println("ID\tNombre");
        for (Menus ca: datos) {
            System.out.println(ca.getIdMenu()+"\t"+ca.getNombre());
        }
    }

    public void menu(){
        int opc=0;
        Scanner cs=new Scanner(System.in);
        String mensaje="Seleccione una opcion: \n 1=Crear\n2=Listar\n0=Salir";
        System.out.println(mensaje);
        opc=Integer.parseInt(cs.next());
        do {
            switch (opc){
                case 1:{registro();}
                case 2:{listar();}
            }
            System.out.println(mensaje);
            opc=Integer.parseInt(cs.next());
        }while(opc!=0);
    }

}
