package pe.edu.upeu.sysregistropolleria.control;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.edu.upeu.sysregistropolleria.componente.*;
import pe.edu.upeu.sysregistropolleria.dto.ModeloDataAutocomplet;
import pe.edu.upeu.sysregistropolleria.dto.SessionManager;
import pe.edu.upeu.sysregistropolleria.modelo.VentCarrito;
import pe.edu.upeu.sysregistropolleria.modelo.Venta;
import pe.edu.upeu.sysregistropolleria.modelo.VentaDetalle;
import pe.edu.upeu.sysregistropolleria.servicio.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;

@Component
public class VentaController {
    @FXML
    TextField autocompProducto;
    @FXML
    TextField nombreProducto, codigoPro, cantidadPro, txtBaseImp, txtIgv, txtDescuento, txtImporteT;
    @FXML
    Button btnRegVenta, btnRegCarrito, btnFormCliente;
    @FXML
    TextField autocompCliente;
    @FXML
    TextField razonSocial;
    @FXML
    TextField dniRuc;
    @FXML
    TableView<VentCarrito> tableView;
    AutoCompleteTextField actf;
    ModeloDataAutocomplet lastProducto;
    AutoCompleteTextField actfC;
    ModeloDataAutocomplet lastCliente;

    @Autowired
    ProductoService ps;
    @Autowired
    ClienteService cs;
    @Autowired
    VentCarritoService daoC;
    @Autowired
    UsuarioService daoU;
    @Autowired
    VentaService daoV;
    @Autowired
    VentaDetalleService daoVD;

    Stage stage;
    @FXML
    private AnchorPane miContenedor;

    private JasperPrint jasperPrint;

    private final SortedSet<ModeloDataAutocomplet> entries = new TreeSet<>((ModeloDataAutocomplet o1, ModeloDataAutocomplet o2) -> o1.toString().compareTo(o2.toString()));
    private final SortedSet<ModeloDataAutocomplet> entriesC = new TreeSet<>((ModeloDataAutocomplet o1, ModeloDataAutocomplet o2) -> o1.toString().compareTo(o2.toString()));


    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            stage = (Stage) miContenedor.getScene().getWindow();
            System.out.println("El título del stage es: " + stage.getTitle());
        });

        listarCliente();
        actfC = new AutoCompleteTextField<>(entriesC, autocompCliente);
        autocompCliente.setOnKeyReleased(e -> {
            lastCliente = (ModeloDataAutocomplet) actfC.getLastSelectedObject();
            if (lastCliente != null) {
                razonSocial.setText(lastCliente.getNameDysplay());
                dniRuc.setText(lastCliente.getIdx());
                listar();
            }
        });

        listarProducto();
        actf = new AutoCompleteTextField<>(entries, autocompProducto);
        autocompProducto.setOnKeyReleased(e -> {
            lastProducto = (ModeloDataAutocomplet) actf.getLastSelectedObject();
            if (lastProducto != null) {
                nombreProducto.setText(lastProducto.getNameDysplay());
                codigoPro.setText(lastProducto.getIdx());
            }
        });

        personalizarTabla();
        btnRegCarrito.setDisable(true);
    }

    public void listarProducto() {
        entries.addAll(ps.listAutoCompletProducto());
    }

    public void listarCliente() {
        entriesC.addAll(cs.listAutoCompletCliente());
    }

    public void personalizarTabla() {
        TableViewHelper<VentCarrito> tableViewHelper = new TableViewHelper<>();
        LinkedHashMap<String, ColumnInfo> columns = new LinkedHashMap<>();
        columns.put("ID Prod", new ColumnInfo("producto.idProducto", 100.0));
        columns.put("Nombre Producto", new ColumnInfo("nombreProducto", 300.0));
        columns.put("Cantidad", new ColumnInfo("cantidad", 60.0));
        columns.put("P.Unitario", new ColumnInfo("punitario", 100.0));
        columns.put("P.Total", new ColumnInfo("ptotal", 100.0));
        Consumer<VentCarrito> updateAction = ventCarrito -> System.out.println("Actualizar: " + ventCarrito);
        Consumer<VentCarrito> deleteAction = this::deleteReg;
        tableViewHelper.addColumnsInOrderWithSize(tableView, columns, updateAction, deleteAction);
        tableView.setTableMenuButtonVisible(true);
    }

    public void listar(){
        tableView.getItems().clear();
        List<VentCarrito> lista=daoC.listaCarritoCliente(dniRuc.getText());
        double impoTotal = 0, igv = 0;
        for (VentCarrito dato: lista){
            impoTotal += Double.parseDouble(String.valueOf(dato.getPtotal()));
        }
        txtImporteT.setText(String.valueOf(impoTotal));
        double pv = impoTotal / 1.18;
        txtBaseImp.setText(String.valueOf(Math.round(pv * 100.0) / 100.0));
        txtIgv.setText(String.valueOf(Math.round((pv * 0.18) * 100.0) / 100.0));
        tableView.getItems().addAll(lista);
    }
    public void editVenCarrito(VentCarrito obj) {
        System.out.println(obj.getDniruc());
    }
    public void deleteReg(VentCarrito obj) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("Confirmar acción");
        alert.setContentText("¿Estás seguro de que deseas eliminar este elemento?");
        // Mostrar el diálogo y esperar la respuesta del usuario
        Optional<ButtonType> result = alert.showAndWait();
        // Verificar si el usuario hizo clic en "Aceptar"
        if (result.isPresent() && result.get() == ButtonType.OK) {
            daoC.delete(obj.getIdCarrito());
            Stage stage = StageManager.getPrimaryStage();
            double with=stage.getMaxWidth()/2;
            Toast.showToast(stage, "¡Acción completada!", 2000, with, 50);
            listar();
        } else {
            // Si el usuario cancela, no se hace nada
            System.out.println("Acción cancelada");
        }
    }

    @FXML
    private void calcularPT() {
        if (!cantidadPro.getText().isEmpty()) {
            try {
                // Obtener el precio unitario del producto desde la base de datos
                double precioUnitario = ps.getPrecioProducto(Long.parseLong(codigoPro.getText()));
                double cantidad = Double.parseDouble(cantidadPro.getText());

                // Calcular el precio total
                double precioTotal = precioUnitario * cantidad;

                // Mostrar precio total en txtImporteT
                txtImporteT.setText(String.valueOf(precioTotal));

                // Habilitar o deshabilitar el botón según la cantidad
                btnRegCarrito.setDisable(cantidad <= 0.0);
            } catch (NumberFormatException | NullPointerException e) {
                System.out.println("Error al calcular el precio: " + e.getMessage());
                btnRegCarrito.setDisable(true);
            }
        } else {
            btnRegCarrito.setDisable(true);
        }
    }


    @FXML
    private void registarPCarrito() {
        try {
            VentCarrito ss = VentCarrito.builder()
                    .dniruc(dniRuc.getText())
                    .producto(ps.searchById(Long.parseLong(codigoPro.getText())))
                    .nombreProducto(nombreProducto.getText())
                    .cantidad(Double.parseDouble(cantidadPro.getText()))
                    .punitario(ps.getPrecioProducto(Long.parseLong(codigoPro.getText()))) // Obtener desde la base de datos
                    .ptotal(Double.parseDouble(cantidadPro.getText()) * ps.getPrecioProducto(Long.parseLong(codigoPro.getText())))
                    .estado(1)
                    .usuario(daoU.searchById(SessionManager.getInstance().getUserId()))
                    .build();
            daoC.save(ss);
            listar();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void registrarVenta(){
        Locale locale = new Locale("es", "es -PE");
        LocalDateTime localDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd -MM -yyyy HH:mm:ss ", locale);
        String fechaFormateada = localDate.format(formatter);
        Venta to =Venta.builder()
                .cliente(cs.searchById(dniRuc.getText())).precioBase(Double
                        .parseDouble(txtBaseImp.getText()))
                .igv(Double.parseDouble(txtIgv.getText()))
                .precioTotal(Double.parseDouble(txtImporteT.getText()))
                .usuario(daoU.searchById(SessionManager.getInstance().getUserId()))
                .serie("V")
                .tipoDoc("Factura")
                .fechaGener(localDate.parse(fechaFormateada, formatter))
                .numDoc("00")
                .build();Venta idX = daoV.save
                (to);List
                <VentCarrito> dd = daoC.listaCarritoCliente
                (dniRuc.getText());
        if
        (idX.getIdVenta() != 0) {
            for
            (VentCarrito car : dd) {
                VentaDetalle vd = VentaDetalle
                        .builder()
                        .venta(idX)
                        .producto(ps.searchById(car.producto.getIdProducto()))
                        .cantidad(car.getCantidad())
                        .descuento(0.0)
                        .pu(car.getPunitario())
                        .subtotal(car.getPtotal())
                        .build();
                daoVD.save
                        (vd);

            }

        }
        daoC.deleteCarAll
                (dniRuc.getText());
        listar();
        try {jasperPrint= daoV.runReport(Long.parseLong(String.valueOf(idX.getIdVenta())));
            Platform.runLater(() -> {
                ReportAlert reportAlert =new ReportAlert(jasperPrint);
                reportAlert.show();
                        /*ReportDialog reportDialog = new ReportDialog(jasperPrint);
                        reportDialog.show();*/
            });
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}

