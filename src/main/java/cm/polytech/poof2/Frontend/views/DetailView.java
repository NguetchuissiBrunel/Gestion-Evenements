package cm.polytech.poof2.Frontend.views;

import javafx.stage.Stage;

public abstract class DetailView {
    protected Stage stage;

    public DetailView() {
        stage = new Stage();
    }

    protected abstract void buildUI();

    public void show() {
        buildUI();
        stage.show();
    }
}