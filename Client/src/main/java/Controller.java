import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    public ListView<String> listview;
    public Button sendButton;
    public TextField textField;
    private static DataInputStream is;
    private static DataOutputStream os;
    private final String clientStoragePath = "Client/ClientStorage";


    public static void  stop(){
        try {
            os.writeUTF("quit");
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void sendMessage(ActionEvent actionEvent) {
        String message = textField.getText();
        try {
            os.writeUTF(message);
            os.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
        textField.clear();
    }

    public void initialize(URL location, ResourceBundle resources) {
        File dir = new File(clientStoragePath);

        for (File file : dir.listFiles() ){
            listview.getItems().add(file.getName());
        }
        
        textField.setOnAction(this::sendMessage);
        try {
            Socket socket = new Socket("localhost", 8189);
            is = new DataInputStream(socket.getInputStream());
            os = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                while (true) {
                    try {
                        String message = is.readUTF();
                        if (message.equals("quit")) {
                            break;
                        }
                        Platform.runLater(() -> listview.getItems().add(message));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleMouseClick(javafx.scene.input.MouseEvent mouseEvent) {
        textField.setText("/upload±" + listview.getSelectionModel().getSelectedItem());
    }
}
