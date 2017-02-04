package com.mightymatth.questionpooljava.ui;

import com.mightymatth.questionpooljava.question.BaseQuestion;
import com.mightymatth.questionpooljava.question.MultipleAnswerQuestion;
import com.mightymatth.questionpooljava.question.SingleAnswerQuestion;
import com.mightymatth.questionpooljava.states.ParsingState;
import com.mightymatth.questionpooljava.states.UserState;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.Effect;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collections;
import java.util.LinkedList;


public class QuestionActivity {
    File mainFile;
    Button leftButton;
    Button rightButton;
    ProgressIndicator pind;
    Stage stage;
    LinkedList<BaseQuestion> questionList;
    CheckBox shuffleQuestions;
    CheckBox shuffleAnswers;

    Integer indexCurrentQuestion;
    UserState currentState;

    public QuestionActivity(File mainFile, CheckBox shuffleQuestions, CheckBox shuffleAnswers) {
        this.mainFile = mainFile;
        this.shuffleQuestions = shuffleQuestions;
        this.shuffleAnswers = shuffleAnswers;

        questionList = new LinkedList<>();

        // setting up a new stage (window)
        stage = new Stage();
        stage.setTitle("Question Pool - " + mainFile.getName());

        scene1();


    }

    // SCENE 1 - Parsing .txt file with questions
    private void scene1() {

        Task<Void> parsingTask = ParsingTask(mainFile, questionList);
        parsingTask.setOnSucceeded(event -> scene2());

        pind = new ProgressIndicator(); // indeterminate indicator

        HBox progressBox = new HBox();
        progressBox.setPadding(new Insets(15));
        progressBox.getChildren().addAll(pind);
        progressBox.setAlignment(Pos.CENTER);
        progressBox.setPrefSize(400, 430);

        stage.setScene(new Scene(progressBox));
        stage.show();

        Thread parsingThread = new Thread(parsingTask);
        parsingThread.start();
    }

    // SCENE 2 - Displaying content
    private void scene2() {

        indexCurrentQuestion = 0;
        currentState = UserState.READING_QUESTION;

        // shuffle questionList
        if (shuffleQuestions.isSelected()) Collections.shuffle(questionList);
        // shuffle answers
        if (shuffleAnswers.isSelected()) {
            for ( BaseQuestion tempQuestionObject : questionList) {
                if (tempQuestionObject instanceof MultipleAnswerQuestion) {
                    Collections.shuffle(((MultipleAnswerQuestion) tempQuestionObject).getAnswerList());
                }
            }
        }


        // configuring buttons
        leftButton = new Button("\u25C0  Back");
        leftButton.setFocusTraversable(false);
        leftButton.setPrefWidth(120);

        rightButton = new Button("Forward  \u25B6");
        rightButton.setFocusTraversable(false);
        rightButton.setPrefWidth(120);

        // 1) Text field
        VBox textBox = new VBox();
        textBox.setPadding(new Insets(15));
        textBox.setAlignment(Pos.TOP_CENTER);
        textBox.setSpacing(8);


        // textBox wrapper
        ScrollPane textBoxWrapper = new ScrollPane(textBox);
        textBoxWrapper.setFitToHeight(true);
        textBoxWrapper.setPrefSize(400, 400);
        textBoxWrapper.maxHeight(400);
        textBoxWrapper.setStyle("-fx-background-color:transparent;");

        // 2) Buttons to left and right
        HBox buttonsRow = new HBox();
        buttonsRow.setPadding(new Insets(1, 5, 1, 5));
        buttonsRow.setSpacing(150);
        buttonsRow.setPrefSize(400, 30);
        buttonsRow.getChildren().addAll(leftButton, rightButton);
        buttonsRow.setAlignment(Pos.BOTTOM_CENTER);

        // Vertical box with all components
        VBox verticalItems = new VBox();
        verticalItems.setPadding(new Insets(10));
        verticalItems.setSpacing(5);
        verticalItems.setAlignment(Pos.CENTER);
        verticalItems.getChildren().addAll(textBoxWrapper, buttonsRow);

        // defining listeners
        rightButton.setOnMouseClicked(event -> {
            goForward(textBox);
        });

        leftButton.setOnMouseClicked(event -> {
            goBackward(textBox);
        });



        // Main Question Scene on the new Stage
        Scene scene = new Scene(verticalItems);
        scene.setOnKeyReleased(event -> {
            if (event.getCode().equals(KeyCode.ENTER)
                    || event.getCode().equals(KeyCode.SPACE)
                    || event.getCode().equals(KeyCode.RIGHT)
                    || event.getCode().equals(KeyCode.DOWN)
                    || event.getCode().equals(KeyCode.PAGE_DOWN)) {
                goForward(textBox);
            } else if (event.getCode().equals(KeyCode.BACK_SPACE)
                    || event.getCode().equals(KeyCode.LEFT)
                    || event.getCode().equals(KeyCode.UP)
                    || event.getCode().equals(KeyCode.PAGE_UP)) {
                goBackward(textBox);
            }
        });
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();


        GuiInteraction(0, UserState.READING_QUESTION, textBox);


    }

    private void goBackward(VBox textBox) {
        if (currentState.equals(UserState.SHOWING_ANSWER)) {
            GuiInteraction(0, UserState.READING_QUESTION, textBox);
        } else {
            GuiInteraction(-1, UserState.SHOWING_ANSWER, textBox);
        }
    }

    private void goForward(VBox textBox) {
        if (currentState.equals(UserState.READING_QUESTION)) {
            GuiInteraction(0, UserState.SHOWING_ANSWER, textBox);
        } else {
            GuiInteraction(1, UserState.READING_QUESTION, textBox);
        }
    }

    /**
     * Changing scenes depending on which question is selected
     * and which state of question should be shown (showing
     * question or showing answer).
     *
     * @param relativeOffset value of offset to be added to index
     * @param nextState the next state of question
     * @param textBox GUI place where changes will be applied
     */
    private void GuiInteraction (Integer relativeOffset, UserState nextState, VBox textBox) {

        // checking whether index is reachable when relativeOffset
        // is added to current index...
        if (indexCurrentQuestion + relativeOffset < 0
                || indexCurrentQuestion + relativeOffset > questionList.size()) return;

        currentState = nextState;
        indexCurrentQuestion += relativeOffset;


        // if located on first question and first stage,
        // disable back button.
        if (indexCurrentQuestion.equals(0) && !leftButton.isDisabled()
                && currentState.equals(UserState.READING_QUESTION)) {
            leftButton.setDisable(true);
        } else if (leftButton.isDisabled()){
            leftButton.setDisable(false);
        }

        // if located on last question and last stage,
        // change right button to 'Finish!'.
        if (indexCurrentQuestion.equals(questionList.size() - 1)
                && currentState.equals(UserState.SHOWING_ANSWER)) rightButton.setText("Finish  \u2714");
        else if (currentState.equals(UserState.READING_QUESTION)) rightButton.setText("Forward  \u25B6");

        // go to the end scene if finished with all questions.
        if (indexCurrentQuestion.equals(questionList.size())) {
            theEndScene();
            return;
        }


        // setting ordering numbers on beginning of every question.
        BaseQuestion tempQuestionObject = questionList.get(indexCurrentQuestion);
        String info = "(" + (indexCurrentQuestion + 1) + "/" + questionList.size() + ") ";
        double progress = (indexCurrentQuestion + 1.0) / questionList.size();


        // clearing old data to put new.
        textBox.getChildren().clear();

        // adding question number
        Text questionNumberText = new Text(info);
        questionNumberText.setFill(getColorByPercentage(progress, Color.GREEN, Color.RED));
        questionNumberText.setTextAlignment(TextAlignment.CENTER);
        textBox.getChildren().add(questionNumberText);

        // adding question text
        ModifiedText questionText = new ModifiedText(tempQuestionObject.getQuestionText() + "\n");
        questionText.setStyle("-fx-font-weight: bolder; -fx-font-size: 120%;");
        textBox.getChildren().add(questionText);

        // adding question answers ( + its logic )
        if (tempQuestionObject instanceof MultipleAnswerQuestion) {


            int i = 0;
            for (MultipleAnswerQuestion.answerPair tempPair : ((MultipleAnswerQuestion) tempQuestionObject).getAnswerList()) {

                ModifiedText answer =
                        new ModifiedText(Character.toString((char)(97+i)) + ") " + tempPair.getAnswer());
                if (tempPair.getIsCorrect() && nextState.equals(UserState.SHOWING_ANSWER)) {
                    answer.setFill(Color.GREEN);
                    answer.setStyle("-fx-font-weight: bolder; -fx-font-size: 120%;");
                }
                else if (nextState.equals(UserState.SHOWING_ANSWER)) answer.setFill(Color.GREY);
                textBox.getChildren().add(answer);
                i++;
            }
        } else {
            if (nextState.equals(UserState.SHOWING_ANSWER)) {
                ModifiedText answer = new ModifiedText(((SingleAnswerQuestion) tempQuestionObject).getAnswer());
                answer.setFill(Color.GREEN);
                answer.setStyle("-fx-font-weight: bolder; -fx-font-size: 120%;");
                textBox.getChildren().add(answer);
            }
        }

        if (nextState.equals(UserState.SHOWING_ANSWER)) {
            ModifiedText explanation = new ModifiedText("\n" + tempQuestionObject.getQuestionExplanation());
            explanation.setFill(Color.ORANGE);
            textBox.getChildren().add(explanation);
        }
    }

    /**
     * Preparing and executing the last scene where user
     * is asked to try again or quit and close the window.
     */
    private void theEndScene () {

        // configuring buttons
        Button quitButton =  new Button("    Quit   ");
        Button tryAgainButton = new Button("   Try Again   ");
        quitButton.setFocusTraversable(false);
        tryAgainButton.setFocusTraversable(false);

        // defining main box with buttons inside.
        HBox buttonBox = new HBox();
        buttonBox.getChildren().addAll(quitButton, tryAgainButton);
        buttonBox.setSpacing(30);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPrefSize(400, 430);

        // listeners for buttons.
        quitButton.setOnMouseClicked(event -> stage.close());
        tryAgainButton.setOnMouseClicked(event -> scene2());

        // applying scenes on main window.
        Scene scene = new Scene(buttonBox);
        scene.setOnKeyReleased(event -> {
            if (event.getCode().equals(KeyCode.ENTER)
                    || event.getCode().equals(KeyCode.SPACE)) {
                scene2();
            } else if (event.getCode().equals(KeyCode.ESCAPE)) {
                stage.close();
            }

        });
        stage.setScene(scene);
    }

    /**
     * Parsing given file through the defined rules of making QuestionPool. <br>
     * Rules: <br>
     * - every section can contain multiple lines <br>
     * - sections must be in proper order <br>
     * - sections must be divided by <b>one</b> empty line <br><br>
     * <b>Question Section</b> -> Begins with character <b>'#'</b> (if question has provided answers)
     * or <b>'$'</b> (if question has not provided answers) <br><br>
     * <b>Answer Section</b> -> <i>if questions has provided answers</i>: every line is one answer.
     * if answer is correct, line must begin with character <b>'.'</b>. <br>
     * <i>if questions has not provided answers</i>: answer can contain multiple lines. <br><br>
     * <b>Comment Section</b> -> comments on answers (can contain multiple lines too) <br>
     *
     * @param file selected to parse.
     * @param questionList a list where data is being saved after parsing a file.
     * @return not important for this method
     *
     *
     */
    private Task<Void> ParsingTask (File file, LinkedList<BaseQuestion> questionList) {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                ParsingState state = ParsingState.NONE;
                String question = "";
                String singleAnswer = "";
                LinkedList<MultipleAnswerQuestion.answerPair> multipleAnswer = new LinkedList<>();

                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;


                // parsing process
                while ((line = reader.readLine()) != null) {
                    line = line.trim();

                    if(line.equals("")) {
                        if (state.equals(ParsingState.QUESTION1)) state = ParsingState.ANSWER1;
                        else if (state.equals(ParsingState.QUESTION2)) state = ParsingState.ANSWER2;
                        else if (state.equals(ParsingState.ANSWER1)){
                            questionList.add(new MultipleAnswerQuestion(question,
                                    new LinkedList<>(multipleAnswer)));
                            state = ParsingState.EXPLANATION;
                        } else if (state.equals(ParsingState.ANSWER2)) {
                            questionList.add(new SingleAnswerQuestion(question, singleAnswer));
                            state = ParsingState.EXPLANATION;
                        } else if (state.equals(ParsingState.EXPLANATION)) state = ParsingState.NONE;
                    }

                    else if (state.equals(ParsingState.ANSWER1)) {
                        if (line.startsWith("."))
                            multipleAnswer.add(new MultipleAnswerQuestion.answerPair(line.substring(1), true));
                        else multipleAnswer.add(new MultipleAnswerQuestion.answerPair(line, false));
                    }

                    else if (state.equals(ParsingState.ANSWER2))
                        singleAnswer = (singleAnswer + "\n" + line).trim();

                    else if (state.equals(ParsingState.QUESTION1) ||
                            state.equals(ParsingState.QUESTION2))
                        question = (question + "\n" + line).trim();
                    else if (state.equals(ParsingState.EXPLANATION)
                            && !line.startsWith("#") && !line.startsWith("$"))
                        questionList.peekLast().appendQuestionExplanation(line);
                    else if (line.startsWith("#")){
                        state = ParsingState.QUESTION1;
                        question = line.substring(1);
                        multipleAnswer.clear();
                    } else if (line.startsWith("$")) {
                        state = ParsingState.QUESTION2;
                        question = line.substring(1);
                        singleAnswer = "";
                    }
                }

                // saving last question
                if (state.equals(ParsingState.ANSWER1))
                    questionList.add(new MultipleAnswerQuestion(question,
                            new LinkedList<>(multipleAnswer)));
                else if (state.equals(ParsingState.ANSWER2))
                    questionList.add(new SingleAnswerQuestion(question, singleAnswer));

                reader.close();

                return null;
            }
        };
    }

    private Paint getColorByPercentage(double percentage, Color paintFrom, Color paintTo) {
        double hue = (percentage*(paintTo.getHue() - paintFrom.getHue())) + paintFrom.getHue();
        return Color.hsb(hue, 1, 0.5);
    }

}

