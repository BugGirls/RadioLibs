package hndt.radiolibs.jsf;

import hndt.radiolibs.bean.ManagerBean;
import hndt.radiolibs.biz.EnumValue;
import hndt.radiolibs.servlet.TipAnnotation;
import hndt.radiolibs.util.Utils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("requiredValidator")
public class RequiredValidator implements Validator {
    @Override
    public void validate(FacesContext context, UIComponent uiComponent, Object value) throws ValidatorException {
        if (Utils.isEmpty(value)) {
            ManagerBean mbean = null;
            Object obj = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(EnumValue.LITERAL_MANAGER);
            if (obj instanceof ManagerBean) {
                mbean = (ManagerBean) obj;
            }
            if (mbean != null) {
                TipAnnotation.send(mbean.getId(), 1, "不能为空");
            }

        }
    }
}
