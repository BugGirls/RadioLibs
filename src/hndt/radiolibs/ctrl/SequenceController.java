package hndt.radiolibs.ctrl;


import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class SequenceController implements Serializable {
    int seq = 1;

    public int next() {
        return seq++;
    }

    public void reset() {
        seq = 1;
    }

}
