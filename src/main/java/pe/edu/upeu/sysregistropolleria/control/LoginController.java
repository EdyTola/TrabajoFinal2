package pe.edu.upeu.sysregistropolleria.control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import pe.edu.upeu.sysregistropolleria.componente.StageManager;
import pe.edu.upeu.sysregistropolleria.componente.Toast;
import pe.edu.upeu.sysregistropolleria.dto.SessionManager;
import pe.edu.upeu.sysregistropolleria.modelo.Usuario;
import pe.edu.upeu.sysregistropolleria.servicio.UsuarioService;

import java.io.IOException;

@Component
public class LoginController {

    @Autowired
    UsuarioService us;

    @Autowired
    private ApplicationContext context;

    @FXML
    TextField txtUsuario;
    @FXML
    PasswordField txtClave;
    @FXML
    Button btnIngresar;



    @FXML
    public void login(ActionEvent event) throws IOException {
        try {
            Usuario usu=us.loginUsuario(txtUsuario.getText(),
                    new String(txtClave.getText()));
            if (usu!=null) {

                SessionManager.getInstance().setUserId(usu.getIdUsuario());
                SessionManager.getInstance().setUserName(usu.getUser());
                SessionManager.getInstance().setNombrePerfil(usu.getIdPerfil().getNombre());

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/guimainfx.fxml"));
                loader.setControllerFactory(context::getBean);
                Parent mainRoot = loader.load();
                Screen screen = Screen.getPrimary();
                Rectangle2D bounds = screen.getBounds();
                Scene mainScene = new Scene(mainRoot,bounds.getWidth(), bounds.getHeight()-30);
                mainScene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.getIcons().add(new Image(getClass().getResource("/img/store.png").toExternalForm()));
                stage.setScene(mainScene);
                stage.setTitle("Pantalla Principal");
                stage.setX(bounds.getMinX());
                stage.setY(bounds.getMinY());
                stage.setResizable(true);
                StageManager.setPrimaryStage(stage);
                stage.setWidth(bounds.getWidth());
                stage.setHeight(bounds.getHeight());
                stage.show();
            } else {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                double with=stage.getWidth()*2;
                double h=stage.getHeight()/2;
                System.out.println(with + " h:"+h);
                Toast.showToast(stage, "Credencial invalido!! intente nuevamente", 2000, with, h);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    @FXML
    public void register(ActionEvent event) throws IOException {
        // Crear un nuevo objeto Usuario
        Usuario nuevoUsuario = new Usuario();

        // Obtener datos del formulario de registro (puedes tener otros campos en tu vista)
        nuevoUsuario.setUser(txtUsuario.getText()); // Asumiendo que usas el mismo campo para el nombre de usuario
        nuevoUsuario.setClave(txtClave.getText()); // Captura la contraseña
        nuevoUsuario.setEstado("activo"); // Estado por defecto, puedes cambiarlo según tu lógica

        // Asumir que tienes un perfil para asignar (puedes crear un combo box para elegir el perfil)
        // nuevoUsuario.setIdPerfil(seleccionPerfil); // Descomentar y asignar según tu lógica

        // Llamar al servicio para guardar el nuevo usuario
        us.save(nuevoUsuario);

        // Opcional: Mostrar un mensaje de éxito
        Toast.showToast((Stage) ((Node) event.getSource()).getScene().getWindow(),
                "Usuario registrado con éxito!", 2000, 300, 200);
    }
}