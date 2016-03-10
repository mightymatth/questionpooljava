import javafx.scene.text.Text;

/**
 * Created by matth on 22.02.16..
 */
public class ModifiedText extends Text {
    public ModifiedText() {
        super();
        setWrappingWidth(400*0.85);
        setStyle("-fx-font-size: 120%;");

    }

    public ModifiedText(String text) {
        super(text);
        setWrappingWidth(400*0.85);
        setStyle("-fx-font-size: 120%;");

    }

    public ModifiedText(double x, double y, String text) {
        super(x, y, text);
        setWrappingWidth(400*0.85);
        setStyle("-fx-font-size: 120%;");
    }
}
