package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.validator.routines.EmailValidator;

public class Controller {
    @FXML
    private TextField nameField,fullNameField,emailField,phoneField;
    @FXML
    private Label nameLabel,passwordLabel,fullNameLabel,emailLabel,phoneLabel,dateLabel,invalidEmailLabel,invalidPhoneLabel;
    @FXML
    private PasswordField passwordField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button enterBtn;

    private DateTimeFormatter dateTimeFormatter;

    @FXML
    public void initialize() {
        System.out.println("App is running...");

        final String datePattern = "M/dd/yyyy";
        dateTimeFormatter = DateTimeFormatter.ofPattern(datePattern);
        datePicker.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                String finalDate = null;
                if (date != null)
                    finalDate = dateTimeFormatter.format(date);
                return finalDate;
            }

            @Override
            public LocalDate fromString(String string) {
                LocalDate date = null;
                if (string != null)
                    date = LocalDate.parse(string, dateTimeFormatter);
                return date;
            }
        });
    }

    @FXML
    public boolean isValidPhone(String phone) {
        Pattern pattern = Pattern.compile("^([0-9]{3}[-]){2}[0-9]{4}$");
        Matcher matcher = pattern.matcher(phoneField.getText());
        return matcher.matches();
    }

    @FXML
    public static boolean isValidEmail(String email) {
        // create the EmailValidator instance
        EmailValidator validator = EmailValidator.getInstance();

        // check for valid email addresses using isValid method
        return validator.isValid(email);
    }

    @FXML
    public void btnOnPress(ActionEvent event) {
        System.out.println();
        System.out.print(nameLabel.getText());
        System.out.println(nameField.getText());

        System.out.print(passwordLabel.getText());
        System.out.println(passwordField.getText());

        System.out.print(fullNameLabel.getText());
        System.out.println(fullNameField.getText());

        if (isValidEmail(emailField.getText())) {
            System.out.print(emailLabel.getText());
            System.out.println(emailField.getText());
            invalidEmailLabel.setText("");
        } else {
            invalidEmailLabel.setText("Invalid E-Mail Address");
            System.out.println("Invalid E-Mail Address");
        }

        if (isValidPhone(phoneField.getText())) {
            System.out.print(phoneLabel.getText());
            System.out.println(phoneField.getText());
            invalidPhoneLabel.setText("");
        } else {
            invalidPhoneLabel.setText("Invalid Phone Number Format");
            System.out.println("Invalid Phone Number Format");
        }


        System.out.print(dateLabel.getText());
        System.out.println(datePicker.getEditor().getText());
    }
}
