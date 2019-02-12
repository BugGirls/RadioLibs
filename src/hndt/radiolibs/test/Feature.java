package hndt.radiolibs.test;

import hndt.radiolibs.util.GSON;
import hndt.radiolibs.util.Utils;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.ConversationScoped;
import javax.faces.annotation.RequestParameterMap;
import javax.faces.component.UIComponent;
import javax.faces.component.search.SearchExpressionContext;
import javax.faces.component.search.SearchKeywordContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Named
@RequestScoped
public class Feature implements Serializable {


    Map<String, String> fmap = new HashMap<>();

    public Feature() {
        fmap = Utils.asHashMap("a", 1, "b", 2, "c", 3, "d", "d");
        System.out.println(GSON.toJson(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()));
    }

    public void findComponent() {
        String expression = ":form";
        SearchExpressionContext searchContext = SearchExpressionContext.createSearchExpressionContext(FacesContext.getCurrentInstance(), FacesContext.getCurrentInstance().getViewRoot().findComponent("@id(form)"));
        FacesContext.getCurrentInstance().getApplication().getSearchExpressionHandler().resolveComponent(searchContext, expression, (context, target) -> System.out.print(target.getId()));
    }

    public void clearSession() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
    }

    public String doit() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String user = params.get("user");
        System.out.println("user:" + user);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", user);
        FacesContext.getCurrentInstance().getPartialViewContext().getEvalScripts().add("alert('渲染完毕')");
        return "user:" + user;
    }

    public void resolve(SearchKeywordContext searchKeywordContext, UIComponent current, String keyword) {
        UIComponent parent = current.getParent();
        if (parent != null) {
            searchKeywordContext.invokeContextCallback(parent.getParent());
        } else {
            searchKeywordContext.setKeywordResolved(true);
        }
    }


    public Map<String, String> getFmap() {
        return fmap;
    }

    public void setFmap(Map<String, String> fmap) {
        this.fmap = fmap;
    }
}
