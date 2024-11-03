package pe.edu.upeu.sysregistropolleria.control;

import jakarta.validation.Validator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.edu.upeu.sysregistropolleria.componente.ColumnInfo;
import pe.edu.upeu.sysregistropolleria.componente.ComboBoxAutoComplete;
import pe.edu.upeu.sysregistropolleria.componente.TableViewHelper;
import pe.edu.upeu.sysregistropolleria.dto.ComboBoxOption;
import pe.edu.upeu.sysregistropolleria.modelo.Producto;
import pe.edu.upeu.sysregistropolleria.servicio.MenusService;
import pe.edu.upeu.sysregistropolleria.servicio.PrecioService;
import pe.edu.upeu.sysregistropolleria.servicio.ProductoService;
import pe.edu.upeu.sysregistropolleria.servicio.ReservaService;

import java.util.LinkedHashMap;
import java.util.function.Consumer;

import static pe.edu.upeu.sysregistropolleria.componente.Toast.showToast;

@Component
public class RegistroController {

    @FXML
    TextField txtNCliente, txtFiltroDato;
    @FXML
    ComboBox<ComboBoxOption> cbxReserva; //Marca
    @FXML
    ComboBox<ComboBoxOption> cbxMenu; //Categoria
    @FXML
    ComboBox<ComboBoxOption> cbxPrecio;//UnidMedida
    @FXML
    private TableView<Producto> tableView;
    @FXML
    Label lbnMsg;
    @FXML
    private AnchorPane miContenedor;
    Stage stage;

    @Autowired
    ReservaService ms;
    @Autowired
    MenusService cs;
    @Autowired
    ProductoService ps;
    @Autowired
    PrecioService ums;


    private Validator validator;
    ObservableList<Producto> listarProducto;
    Producto formulario;
    Long idProductoCE=0L;


    public void initialize() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2000), event -> {
            stage = (Stage) miContenedor.getScene().getWindow();
            if (stage != null) {
                System.out.println("El título del stage es: " + stage.getTitle());
            } else {
                System.out.println("Stage aún no disponible.");
            }
        }));
        timeline.setCycleCount(1);
        timeline.play();

        cbxMenu.setTooltip(new Tooltip());
        cbxMenu.getItems().addAll(cs.listarCombobox());
        cbxMenu.setOnAction(event -> {
            ComboBoxOption selectedProduct = cbxMenu.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                String selectedId = selectedProduct.getKey(); // Obtener el ID
                System.out.println("ID del Menu seleccionado: " + selectedId);
            }
        });
        new ComboBoxAutoComplete<>(cbxMenu);


        cbxReserva.setTooltip(new Tooltip());
        cbxReserva.getItems().addAll(ms.listarCombobox());
        cbxReserva.setOnAction(event -> {
            ComboBoxOption selectedProduct = cbxReserva.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                String selectedId = selectedProduct.getKey(); // Obtener el ID
                System.out.println("ID del producto seleccionado: " + selectedId);
            }
        });
        new ComboBoxAutoComplete<>(cbxReserva);


        cbxPrecio.setTooltip(new Tooltip());
        cbxPrecio.getItems().addAll(ums.listarCombobox());
        cbxPrecio.setOnAction(event -> {
            ComboBoxOption selectedProduct = cbxPrecio.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                String selectedId = selectedProduct.getKey(); // Obtener el ID
                System.out.println("ID del producto seleccionado: " + selectedId);
            }
        });
        new ComboBoxAutoComplete<>(cbxPrecio);

        // Crear instancia de la clase genérica TableViewHelper
        TableViewHelper<Producto> tableViewHelper = new TableViewHelper<>();
        LinkedHashMap<String, ColumnInfo> columns = new LinkedHashMap<>();
        columns.put("ID Pro.", new ColumnInfo("idProducto", 60.0)); // Columna visible "Columna 1" mapea al campo "campo1"
        columns.put("Nombre Cliente", new ColumnInfo("nombre", 200.0)); // Columna visible "Columna 2" mapea al campo "campo2"
        //columns.put("P. Unitario", new ColumnInfo("pu", 150.0));
        //columns.put("Utilidad", new ColumnInfo("utilidad", 100.0));
        columns.put("Menu", new ColumnInfo("menu.nombre", 200.0)); // Columna visible "Columna 2" mapea al campo "campo2"
        columns.put("Reserva", new ColumnInfo("reserva.nombre", 200.0));
        columns.put("Precio", new ColumnInfo("precio.nombrePrecio",150.0));


        // Definir las acciones de actualizar y eliminar
        Consumer<Producto> updateAction = (Producto producto) -> {
            System.out.println("Actualizar: " + producto);
            //editForm(producto);
        };
        Consumer<Producto> deleteAction = (Producto producto) -> {System.out.println("Actualizar: " + producto);
            ps.delete(producto.getIdProducto()); /*deletePerson(usuario);*/
            double with=stage.getWidth()/1.5;
            double h=stage.getHeight()/2;
            showToast(stage, "Se eliminó correctamente!!", 2000, with, h);
            listar();
        };

        tableViewHelper.addColumnsInOrderWithSize(tableView, columns,updateAction, deleteAction );
        tableView.setTableMenuButtonVisible(true);
        listar();


    }

    public void listar(){
        try {
            tableView.getItems().clear();
            listarProducto = FXCollections.observableArrayList(ps.list());
            tableView.getItems().addAll(listarProducto);
            // Agregar un listener al campo de texto txtFiltroDato para filtrar los productos
            txtFiltroDato.textProperty().addListener((observable, oldValue, newValue) -> {
                //filtrarProductos(newValue);
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }



    }
}

