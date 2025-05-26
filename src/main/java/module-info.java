module cm.polytech.poof2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;


    exports cm.polytech.poof2.Frontend;
    exports cm.polytech.poof2.classes to com.fasterxml.jackson.databind;
    exports cm.polytech.poof2.Sauvegarde to com.fasterxml.jackson.databind;

    opens cm.polytech.poof2.Frontend to javafx.fxml;
    opens cm.polytech.poof2.classes to com.fasterxml.jackson.databind;
    opens cm.polytech.poof2.Sauvegarde to com.fasterxml.jackson.databind;
    exports cm.polytech.poof2.Frontend.views;
    opens cm.polytech.poof2.Frontend.views to javafx.fxml;
}

