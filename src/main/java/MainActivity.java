import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.CodeSource;

/**
 * Created by Matija Pevec on 18.02.16..
 * This class defines an intro stage and scene where user
 * chooses the .txt file and starts next scene.
 */
public class MainActivity extends Application {

    // defining elements
    Button chooseFileButton = new Button("Choose File");
    Button startButton  = new Button("Start");
    Image coverImage = new Image(MainActivity.class.getResourceAsStream("cover.jpg"));
    Text message = new Text("Welcome!");
    CheckBox shuffleQuestions;
    CheckBox shuffleAnswers;
    Text copyrightLabel = new Text("\u00a9 2016 - Matija Pevec @ FER - pevec@live.com");
    File mainFile = null;

    public static void main (String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        primaryStage.setTitle("Question Pool");

        // Initializing window where the user is selecting the file.
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Resource File");

        // Finding .jar file and his parent directory path
        CodeSource codeSource = MainActivity.class.getProtectionDomain().getCodeSource();
        File jarFile;
        try {
            jarFile = new File(codeSource.getLocation().toURI().getPath());
        } catch (URISyntaxException e) {
            System.out.println("init error");
            jarFile = new File(Paths.get("").toAbsolutePath().toString());
        }
        String jarDir = jarFile.getParentFile().getPath();


        fileChooser.setInitialDirectory(new File(jarDir));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt"),
                new FileChooser.ExtensionFilter("All files", "*.*")
        );


        // 1) buttons in a row
        HBox buttonRow = new HBox();
        buttonRow.setPadding(new Insets(15));
        buttonRow.setSpacing(10);
        buttonRow.getChildren().addAll(chooseFileButton, startButton);
        buttonRow.setAlignment(Pos.CENTER);

        // 2) message/info/warnings
        HBox messageLine = new HBox();
        messageLine.setPadding(new Insets(5));
        messageLine.setSpacing(10);
        messageLine.getChildren().addAll(message);
        messageLine.setAlignment(Pos.CENTER);

        // 3) check buttons
        shuffleQuestions = new CheckBox("Shuffle Questions");
        shuffleQuestions.setSelected(true);
        shuffleAnswers = new CheckBox("Shuffle Answers");
        shuffleAnswers.setSelected(true);
        VBox checkBoxes = new VBox();
        checkBoxes.setSpacing(5);
        checkBoxes.getChildren().addAll(shuffleQuestions, shuffleAnswers);
        checkBoxes.setAlignment(Pos.CENTER_LEFT);
        checkBoxes.setPadding(new Insets(2, 2, 2, 60));

        // 4) cover photo
        ImageView imagePane = new ImageView();
        imagePane.resize(150, 150);
        imagePane.setImage(coverImage);

        // VBox
        VBox verticalItems = new VBox();
        verticalItems.setPadding(new Insets(10));
        verticalItems.setSpacing(5);
        verticalItems.setAlignment(Pos.CENTER);
        verticalItems.getChildren().addAll(buttonRow, messageLine, checkBoxes, imagePane);

        // Main frame box + credits
        VBox mainFrameCredits = new VBox();
        HBox copyrightLine = new HBox();
        copyrightLabel.setFont(Font.font(Font.getDefault().getSize() - 2));
        copyrightLabel.setFill(Color.WHITE);
        copyrightLine.setAlignment(Pos.BOTTOM_LEFT);
        copyrightLine.getChildren().add(copyrightLabel);
        copyrightLine.setPadding(new Insets(1, 5, 1, 5));
        copyrightLine.setStyle("-fx-background-color: #808080;");
        mainFrameCredits.getChildren().addAll(verticalItems, copyrightLine);

        // Main Scene
        Scene scene = new Scene(mainFrameCredits);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();


        // Defining listeners

        chooseFileButton.setOnMouseClicked(event -> {

            File lastFileSelected = mainFile;
            mainFile = fileChooser.showOpenDialog(primaryStage);
            if (mainFile == null) {
                mainFile = lastFileSelected;
                return;
            }
            if (!mainFile.getName().endsWith(".txt")) {
                message.setText("Wrong file extension!");
                message.setFill(Color.RED);
                return;
            }


            // check file encoding
            boolean isGuessingCorrect = true;
            Charset charset = Charset.defaultCharset();
            System.out.println("Default encoding: " + charset);

            String mainFileEncoding = "";
            try {
                mainFileEncoding = guessEncoding(Files.readAllBytes(mainFile.toPath()));
            } catch (IOException e) {
                System.out.println("Error in guessing file encoding.");
                isGuessingCorrect = false;
            }
            System.out.println("mainFile encoding: " + mainFileEncoding);


            if (!charset.toString().equals(mainFileEncoding) || !isGuessingCorrect) {
                System.out.println("encoding is different or guessing is not correct. " +
                        "trying to convert.");
                String content = null;
                try {
                    content = FileUtils.readFileToString(mainFile, mainFileEncoding);
                } catch (IOException e) {
                    System.out.println("Reading content of encoded file failed!");
                }

                mainFile = new File(mainFile.getParent()
                        + File.separator
                        + mainFile.getName().substring(0, mainFile.getName().length() - 4)
                        + "_" + charset.toString() + ".txt");
                System.out.println(mainFile.getPath());

                try {
                    FileUtils.writeStringToFile(mainFile, content, charset.toString());
                } catch (IOException e) {
                    System.out.println("Converting the encoded file failed!");
                }
            }

            // prints on GUI
            fileChooser.setInitialDirectory(new File(mainFile.getParent()));
            message.setText(mainFile.getName() + " selected.");
            message.setFill(Color.GREEN);







        });

        startButton.setOnMouseClicked(event -> {

            if (mainFile == null) {
                message.setText("Select a proper file.");
                message.setFill(Color.RED);
            } else if (!mainFile.getName().endsWith(".txt")) {
                message.setText("Wrong file extension!");
                message.setFill(Color.RED);
            } else {
                message.setText("Succeed!");
                message.setFill(Color.GREEN);

                // opening new question window
                new QuestionActivity(mainFile, shuffleQuestions, shuffleAnswers);
            }
        });


    }

    /**
     * Method will guess the encoding of certain array of bytes. Results
     * will depend on ICU (developed by IBM) library.
     * Warning: results will not be correct if the file (represented in
     * array of bytes) is not long enough to detect right file encoding.
     *
     * @param bytes file (or any other string resource) represented
     *              in array of bytes.
     * @return guessed encoding name of the file.
     */
    public static String guessEncoding(byte[] bytes) {
        CharsetMatch match;
        CharsetDetector detector = new CharsetDetector();
        detector.setText(bytes);
        match = detector.detect();
        return match.getName();
    }


}
