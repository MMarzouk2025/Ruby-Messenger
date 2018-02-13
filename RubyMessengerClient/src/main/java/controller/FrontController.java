package controller;
// abdelfata7 start

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import common.ClientInterface;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

// abdelfata7 end
// khaled start
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import common.ServerInterface;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import model.ClientImplementation;
import model.User;

//khaled end
public class FrontController implements Initializable {

    private Stage mStage;
    private double xOffset;
    private double yOffset;
    @FXML
    private BorderPane loginPane;
    @FXML
    private ImageView closeImgBtn;
    @FXML
    private ImageView minimizeImgBtn;
    @FXML
    private Button loginBtn;
    @FXML
    private Label signupTxt;
    @FXML
    private JFXTextField usernameField;
    @FXML
    private JFXPasswordField passwordField;
    
    // khaled start
    private FXMLLoader loader;
    private Scene scene;
    private Parent root;
    private ServerInterface serverRef;
    //khaled end
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Mahmoud Marzouk
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                initController();
            

                // khaled start
                loader = new FXMLLoader();
                try {
                    Registry reg = LocateRegistry.getRegistry(2000);
                    serverRef = (ServerInterface) reg.lookup("chat");
                } catch (RemoteException | NotBoundException ex) {
                    showServerError();
                }
                //khaled end
        
            }
        });
    }
    
    // khaled start
    @FXML
    public void signInAction() {
        String userName = this.usernameField.getText();
        String password = this.passwordField.getText();
        if(userName.trim().equals("") || password.trim().equals("") ){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("login error");
            alert.setContentText("you must type your username and password to sign in");
            alert.showAndWait();
        }
        else{
            try{
                User user = serverRef.signInUser(userName, password);
                if(user != null){
                    root = loader.load(getClass().getResource("/fxml/UserMainScene.fxml").openStream());
                    MainSceneController mainController = loader.getController();
                    ClientInterface client = new ClientImplementation(mainController);
                    client.setUser(user);

                    // Esraa Hassan
                    this.serverRef.register(client);
                    // khaled
                    //send client object to contacts scene controller
                    mainController.setClient(client);
                    mainController.setServer(serverRef);
                    System.out.println(client.getUser().getUsername());
                    scene = new Scene(root);
                    mStage.setScene(scene);
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("login error");
                    alert.setContentText("invalid userName or password");
                    alert.showAndWait();
                }
            }
            catch(RemoteException  ex){
                showServerError();
            } catch (IOException ex) {
                Logger.getLogger(FrontController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    @FXML
    public void signUpAction() {
            try {
                //change scene to sign-up scene
                root = loader.load(getClass().getResource("/fxml/Signup.fxml").openStream());
                SignupController sUpController = loader.<SignupController>getController();
                sUpController.setServer(serverRef);
                scene = new Scene(root);
                mStage.setScene(scene);
            } catch (IOException ex) {
                Logger.getLogger(FrontController.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    private void showServerError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("server error");
        alert.setContentText("server is down !");
        alert.showAndWait();
    }
    //khaled end

    // Mahmoud Marzouk
    private void initController() {
        mStage = (Stage) loginPane.getScene().getWindow();
        loginBtn.requestFocus();
        
        closeImgBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mStage.close();
            }
        });
        
        minimizeImgBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mStage.setIconified(true);
            }
        });
        
        loginBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                signInAction();
            }
        });
        
        signupTxt.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                signUpAction();
            }
        });
    }
    @FXML
    public void holdChatWindow(MouseEvent event) {
        xOffset = mStage.getX() - event.getScreenX();
        yOffset = mStage.getY() - event.getScreenY();
    }

    @FXML
    public void dragChatWindow(MouseEvent event) {
        mStage.setX(event.getScreenX() + xOffset);
        mStage.setY(event.getScreenY() + yOffset);
    }
}
