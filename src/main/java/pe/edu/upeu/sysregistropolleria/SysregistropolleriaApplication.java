package pe.edu.upeu.sysregistropolleria;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SysregistropolleriaApplication extends Application {

	private static ConfigurableApplicationContext configurableApplicationContext;
	private Parent parent;


	public static void main(String[] args) {
		//SpringApplication.run(SysAlmacenFxApplication.class, args);
		launch(args);
	}

	@Override
	public void init() throws Exception {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(SysregistropolleriaApplication.class);
		builder.application().setWebApplicationType(WebApplicationType.NONE);
		configurableApplicationContext = builder.run(getParameters().getRaw().toArray(new String[0]));
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/main_registro.fxml"));
		fxmlLoader.setControllerFactory(configurableApplicationContext::getBean);
		parent= fxmlLoader.load(); // Asegúrate de que este archivo exista en la ruta especificada
	}
	@Override
	public void start(Stage stage) throws Exception {
		Scene scene = new Scene(parent);
		scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
		stage.setScene(scene);
		stage.setTitle("SysRegistroPolleria Spring Java-FX");
		stage.setResizable(false);
		stage.show();
	}
	@Override
	public void stop() throws Exception {
		configurableApplicationContext.close();
	}
}
