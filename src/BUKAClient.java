import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BUKAClient extends Application
{
    public static void main(String[] args)
    {
    	//launch the JavaFX Application
    	launch(args);
    }

	@Override
	public void start(Stage primaryStage) throws Exception {
		BUKAClientPane root = new BUKAClientPane(primaryStage);
		Scene scene = new Scene(root,800,600);
		primaryStage.setScene(scene);
		primaryStage.setTitle("BUKAClient");
		primaryStage.show();
	}
}
