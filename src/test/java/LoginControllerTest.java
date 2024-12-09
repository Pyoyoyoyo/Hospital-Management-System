import com.main.hospitalmanagementsys.controllers.LoginController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class LoginControllerTest {

    private LoginController loginController;
    private TextField usernameField;
    private PasswordField passwordField;
    private Stage mockStage;

    @BeforeEach
    void setUp() {
        loginController = new LoginController();
        usernameField = mock(TextField.class);
        passwordField = mock(PasswordField.class);
        mockStage = mock(Stage.class);

        loginController.usernameField = usernameField;
        loginController.passwordField = passwordField;
    }

    @Test
    void testHandleLoginSuccess() throws Exception {
        when(usernameField.getText()).thenReturn("admin");
        when(passwordField.getText()).thenReturn("admin123");

        FXMLLoader loader = mock(FXMLLoader.class);
        AnchorPane mockView = mock(AnchorPane.class);
        when(loader.load()).thenReturn(mockView);
        loginController.loadDoctorsView();

        loginController.handleLogin(null);

        verify(mockStage).setScene(any(Scene.class));
        verify(mockStage).show();
    }

    @Test
    void testHandleLoginFailure() {
        when(usernameField.getText()).thenReturn("wronguser");
        when(passwordField.getText()).thenReturn("wrongpass");

        loginController.handleLogin(null);

    }

    @Test
    void testHandleForgotPassword() throws Exception {
        FXMLLoader loader = mock(FXMLLoader.class);
        AnchorPane mockView = mock(AnchorPane.class);
        when(loader.load()).thenReturn(mockView);
        loginController.handleForgotPassword(null);

        verify(mockStage).setScene(any(Scene.class));
        verify(mockStage).show();
    }
}
