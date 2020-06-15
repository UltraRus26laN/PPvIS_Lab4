package sample;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class View
{
    private Stage stage = new Stage(); private Scene scene;
    private Controller controller = new Controller(); private VBox box;
    public View()
    {
        final int STAGE_WIDTH = 300, STAGE_HEIGHT = 600; final String STAGE_TITLE_TEXT = "Calculator 1.2"; initWindow(); stage.setWidth(STAGE_WIDTH);
        stage.setHeight(STAGE_HEIGHT); stage.setTitle(STAGE_TITLE_TEXT); stage.setScene(scene);
    }
    public void initWindow()
    {
        Button button1 = new Button("1"); Button button2 = new Button("2"); Button button3 = new Button("3");
        Button button4 = new Button("4"); Button button5 = new Button("5"); Button button6 = new Button("6");
        Button button7 = new Button("7"); Button button8 = new Button("8"); Button button9 = new Button("9");
        Button button0 = new Button("0"); Button buttonClear = new Button("CE"); Button buttonComma = new Button(",");
        Button buttonRemove = new Button("C"); Button buttonPlus = new Button("+"); Button buttonMinus = new Button("-");
        Button buttonBracketOpen = new Button("("); Button buttonBracketClose = new Button(")"); Button buttonPercent = new Button("%");
        Button buttonInverse = new Button("1/x"); Button buttonSqrt = new Button("√"); Button buttonPow = new Button("^");
        Button buttonCalculate = new Button("="); Button buttonMultiply = new Button("*"); Button buttonDivision = new Button("/");
        CheckBox trig = new CheckBox("trig"); TreeView tree = new TreeView();
        TextField formulaField = new TextField(controller.getFormula()); formulaField.setEditable(false);
        HBox firstRowButtons = new HBox(button1, button2, button3, buttonBracketOpen, buttonBracketClose, buttonSqrt, trig);
        HBox secondRowButtons = new HBox(button4, button5, button6, buttonPlus, buttonMinus);
        HBox thirdRowButtons = new HBox(button7, button8, button9, buttonMultiply, buttonDivision);
        HBox fourthRowButtons = new HBox(buttonComma, button0, buttonCalculate, buttonPercent);
        HBox fifthRowButtons = new HBox(buttonRemove, buttonClear, buttonInverse);
        box = new VBox(formulaField, firstRowButtons, secondRowButtons, thirdRowButtons, fourthRowButtons, fifthRowButtons, tree);
        scene = new Scene(box); stage.setScene(scene);
        button0.setOnAction(actionEvent -> { addSymbol("0"); });
        button1.setOnAction(actionEvent -> { addSymbol("1"); });
        button2.setOnAction(actionEvent -> { addSymbol("2"); });
        button3.setOnAction(actionEvent -> { addSymbol("3"); });
        button4.setOnAction(actionEvent -> { addSymbol("4"); });
        button5.setOnAction(actionEvent -> { addSymbol("5"); });
        button6.setOnAction(actionEvent -> { addSymbol("6"); });
        button7.setOnAction(actionEvent -> { addSymbol("7"); });
        button8.setOnAction(actionEvent -> { addSymbol("8"); });
        button9.setOnAction(actionEvent -> { addSymbol("9"); });
        buttonComma.setOnAction(actionEvent -> { addSymbol(","); });
        buttonClear.setOnAction(actionEvent -> { clear(); });
        buttonRemove.setOnAction(actionEvent -> { removeLast(); });
        buttonPlus.setOnAction(actionEvent -> { addSymbol("+"); });
        buttonMinus.setOnAction(actionEvent -> { addSymbol("-"); });
        buttonBracketOpen.setOnAction(actionEvent -> { addSymbol("("); });
        buttonBracketClose.setOnAction(actionEvent -> { addSymbol(")"); });
        buttonPercent.setOnAction(actionEvent -> { addSymbol("%("); });
        buttonInverse.setOnAction(actionEvent -> { addSymbol("inverse("); });
        buttonSqrt.setOnAction(actionEvent -> { addSymbol("sqrt("); });
        buttonPow.setOnAction(actionEvent -> { addSymbol("pow("); });
        buttonMultiply.setOnAction(actionEvent -> { addSymbol("*"); });
        buttonDivision.setOnAction(actionEvent -> { addSymbol("/"); });
        trig.setOnAction(actionEvent ->
        {
            if (!trig.isSelected())
            {
                box.getChildren().remove(2);
                box.getChildren().add(2, new HBox(button4, button5, button6, buttonMinus, buttonBracketClose));
            } else {
                box.getChildren().remove(2);
                box.getChildren().add(2, new HBox(button4, button5, button6, buttonMinus, buttonBracketClose,buttonPow));
            }
        });
        buttonCalculate.setOnAction(actionEvent -> { calculate(); });
    }
    public Stage getStage() { return stage; }
    public void addSymbol(String symbolToAdd)
    {
        controller.addSymbol(symbolToAdd); box.getChildren().remove(0);
        box.getChildren().add(0, new TextField(controller.getFormula()));
    }
    public void clear()
    {
        controller.clearFormula(); box.getChildren().remove(0);
        box.getChildren().add(0, new TextField(controller.getFormula()));
        box.getChildren().remove(box.getChildren().remove(box.getChildren().size() - 1));
    }
    public void removeLast()
    {
        controller.removeLast(); box.getChildren().remove(0);
        box.getChildren().add(0, new TextField(controller.getFormula()));
    }
    public void calculate()
    {
        try
        {
            controller.convert(); controller.calculate();
            box.getChildren().remove(box.getChildren().size() - 1);
            box.getChildren().add(new TreeView<>(controller.getTree()));
            box.getChildren().remove(0);
            box.getChildren().add(0, new TextField(controller.getResult()));

        } catch (Exception e) { e.printStackTrace(); }
    }
}