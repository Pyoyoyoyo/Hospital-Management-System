import com.main.hospitalmanagementsys.controllers.ForgotPasswordController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ForgotPasswordControllerTest {

    private ForgotPasswordController forgotPasswordController;
    private TextField emailField;
    private Stage mockStage;

    @BeforeEach
    void setUp() {
        forgotPasswordController = new ForgotPasswordController();
        emailField = mock(TextField.class);
        mockStage = mock(Stage.class);

        forgotPasswordController.emailField = emailField;
    }

    @Test
    void testHandleResetPasswordValidEmail() throws Exception {
        when(emailField.getText()).thenReturn("test@example.com");

        assertTrue(forgotPasswordController.isValidEmail(emailField.getText()));

        forgotPasswordController.handleResetPassword(null);

        // Here we're verifying if certain internal actions happened
        verify(mockStage).setScene(any(Scene.class));
        verify(mockStage).show();
    }

    @Test
    void testHandleResetPasswordInvalidEmail() {
        when(emailField.getText()).thenReturn("invalid-email");

        assertFalse(forgotPasswordController.isValidEmail(emailField.getText()));

        forgotPasswordController.handleResetPassword(null);

        verify(mockStage, never()).setScene(any(Scene.class));
        verify(mockStage, never()).show();
    }

    @Test
    void testIsValidEmailValid() {
        assertTrue(forgotPasswordController.isValidEmail("valid@example.com"));
    }

    @Test
    void testIsValidEmailInvalid() {
        assertFalse(forgotPasswordController.isValidEmail("invalid-email"));
        assertFalse(forgotPasswordController.isValidEmail("missing-at-sign.com"));
    }

    @Test
    void testLoadLoginForm() throws Exception {
        FXMLLoader loader = mock(FXMLLoader.class);
        AnchorPane mockView = mock(AnchorPane.class);
        when(loader.load()).thenReturn(mockView);

        forgotPasswordController.loadLoginForm();

        verify(mockStage).setScene(any(Scene.class));
        verify(mockStage).show();
    }
}
