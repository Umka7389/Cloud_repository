import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    public ListView<String> clientFiles;
    public Button uploadButton;
    public TextField textField;
    public ListView<String> serverFiles;
    private static DataInputStream is;
    private static DataOutputStream os;
    private final String clientStoragePath = "Client/ClientStorage";
    private final String serverStoragePath = "Server/ServerStorage";

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
        File clDir = new File(clientStoragePath);
        for (File file : Objects.requireNonNull(clDir.listFiles())){
            clientFiles.getItems().add(file.getName());
        }

        File srvDir = new File(serverStoragePath);
        for (File file : Objects.requireNonNull(srvDir.listFiles())){
            serverFiles.getItems().add(file.getName());
        }

        textField.setOnAction(this::sendMessage);
        try {
            Socket socket = new Socket("localhost", 8189);
            is = new DataInputStream(socket.getInputStream());
            os = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void choseClientFiles(javafx.scene.input.MouseEvent mouseEvent) {
        textField.setText("/upload±" + clientFiles.getSelectionModel().getSelectedItem());
    }

    public void choseServerFiles(javafx.scene.input.MouseEvent mouseEvent) {
        textField.setText("/download±" + serverFiles.getSelectionModel().getSelectedItem());
    }

}
