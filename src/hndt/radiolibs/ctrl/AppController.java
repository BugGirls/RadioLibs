package hndt.radiolibs.ctrl;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by tengfei on 2017/6/15.
 */
@Named("app")
@ApplicationScoped
public class AppController implements Serializable{
    public AppController(){

    }
}
