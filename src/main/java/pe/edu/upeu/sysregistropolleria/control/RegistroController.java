package pe.edu.upeu.sysregistropolleria.control;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
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

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        // Crear instancia de la clase genérica TableViewHelper
        TableViewHelper<Producto> tableViewHelper = new TableViewHelper<>();
        LinkedHashMap<String, ColumnInfo> columns = new LinkedHashMap<>();
        columns.put("ID Pro.", new ColumnInfo("idProducto", 60.0));
        columns.put("Nombre Cliente", new ColumnInfo("nombre", 200.0));
        columns.put("Menu", new ColumnInfo("menu.nombre", 200.0));
        columns.put("Reserva", new ColumnInfo("reserva.nombre", 200.0));
        columns.put("Precio", new ColumnInfo("precio.nombrePrecio",150.0));

        // Definir las acciones de actualizar y eliminar
        Consumer<Producto> updateAction = (Producto producto) -> {
            if (producto != null) {
                System.out.println("Actualizar: " + producto);
                // editForm(producto); // Descomenta esto si tienes un método para editar
            }
        };
        Consumer<Producto> deleteAction = (Producto producto) -> {
            if (producto != null) {
                ps.delete(producto.getIdProducto());
                double with = stage.getWidth() / 1.5;
                double h = stage.getHeight() / 2;
                showToast(stage, "Se eliminó correctamente!!", 2000, with, h);
                listar();
            }
        };

        tableViewHelper.addColumnsInOrderWithSize(tableView, columns, updateAction, deleteAction);
        tableView.setTableMenuButtonVisible(true);
        listar();
    }
    public void listar() {
        try {
            tableView.getItems().clear();
            listarProducto = FXCollections.observableArrayList(ps.list());
            System.out.println("Productos cargados: " + listarProducto); // Agregar log aquí
            tableView.getItems().addAll(listarProducto);
        } catch (Exception e) {
            System.out.println("Error al listar productos: " + e.getMessage());
        }
    }

    public void limpiarError() {
        txtNCliente.getStyleClass().remove("text-field-error");
        cbxReserva.getStyleClass().remove("text-field-error");
        cbxMenu.getStyleClass().remove("text-field-error");
        cbxPrecio.getStyleClass().remove("text-field-error");
    }

    public void clearForm() {
        txtNCliente.setText("");
        cbxReserva.getSelectionModel().select(null);
        cbxMenu.getSelectionModel().select(null);
        cbxPrecio.getSelectionModel().select(null);
        idProductoCE = 0L;
        limpiarError();
    }

    @FXML
    public void cancelarAccion() {
        clearForm();
        limpiarError();
    }

    void validarCampos(List<ConstraintViolation<Producto>> violacionesOrdenadasPorPropiedad) {
        LinkedHashMap<String, String> erroresOrdenados = new LinkedHashMap<>();
        for (ConstraintViolation<Producto> violacion : violacionesOrdenadasPorPropiedad) {
            String campo = violacion.getPropertyPath().toString();
            if (campo.equals("nombre")) {
                erroresOrdenados.put("nombre", violacion.getMessage());
                txtNCliente.getStyleClass().add("text-field-error");
            } else if (campo.equals("reserva")) {
                erroresOrdenados.put("reserva", violacion.getMessage());
                cbxReserva.getStyleClass().add("text-field-error");
            } else if (campo.equals("menu")) {
                erroresOrdenados.put("menu", violacion.getMessage());
                cbxMenu.getStyleClass().add("text-field-error");
            } else if (campo.equals("precio")) {
                erroresOrdenados.put("precio", violacion.getMessage());
                cbxPrecio.getStyleClass().add("text-field-error");
            }
        }
        try {
            Map.Entry<String, String> primerError = erroresOrdenados.entrySet().iterator().next();
            lbnMsg.setText(primerError.getValue());
            lbnMsg.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
        } catch (NoSuchElementException e) {
            lbnMsg.setText("No se encontraron errores de validación.");
            lbnMsg.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
        }
    }

    @FXML
    public void validarFormulario() {
        formulario = new Producto();
        formulario.setNombre(txtNCliente.getText());

        // Manejar el caso donde los ComboBox no tienen selección
        String idxM = cbxReserva.getSelectionModel().getSelectedItem() == null ? "0" : cbxReserva.getSelectionModel().getSelectedItem().getKey();
        if (!idxM.equals("0")) {
            formulario.setReserva(ms.searchById(Long.parseLong(idxM)));
        }

        String idxC = cbxMenu.getSelectionModel().getSelectedItem() == null ? "0" : cbxMenu.getSelectionModel().getSelectedItem().getKey();
        if (!idxC.equals("0")) {
            formulario.setMenu(cs.searchById(Long.parseLong(idxC)));
        }

        String idxUM = cbxPrecio.getSelectionModel().getSelectedItem() == null ? "0" : cbxPrecio.getSelectionModel().getSelectedItem().getKey();
        if (!idxUM.equals("0")) {
            formulario.setPrecio(ums.searchById(Long.parseLong(idxUM)));
        }

        Set<ConstraintViolation<Producto>> violaciones = validator.validate(formulario);
        // Si prefieres ordenarlo por el nombre de la propiedad que violó la restricción, podrías usar:
        List<ConstraintViolation<Producto>> violacionesOrdenadasPorPropiedad = violaciones.stream()
                .sorted((v1, v2) -> v1.getPropertyPath().toString().compareTo(v2.getPropertyPath().toString()))
                .collect(Collectors.toList());
        if (violacionesOrdenadasPorPropiedad.isEmpty()) {
            // Los datos son válidos
            lbnMsg.setText("Formulario válido");
            lbnMsg.setStyle("-fx-text-fill: green; -fx-font-size: 16px;");
            limpiarError();
            double with=stage.getWidth()/1.5;
            double h=stage.getHeight()/2;
            if (idProductoCE != 0L && idProductoCE > 0L) {
                formulario.setIdProducto(idProductoCE);
                ps.update(formulario);
                System.out.println("Producto actualizado: " + formulario); // Log para actualizar
                showToast(stage, "Se actualizó correctamente!!", 2000, with, h);
                clearForm();
            } else {
                ps.save(formulario);
                System.out.println("Producto guardado: " + formulario); // Log para guardar
                showToast(stage, "Se guardó correctamente!!", 2000, with, h);
                clearForm();
            }
            listar();
        } else {
            validarCampos(violacionesOrdenadasPorPropiedad);
        }
    }

}

