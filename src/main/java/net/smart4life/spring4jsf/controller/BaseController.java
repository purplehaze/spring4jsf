package net.smart4life.spring4jsf.controller;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by roman on 06.03.2015.
 */
public abstract class BaseController {
    public static final String ID_PARAM = "id";

    protected void navigate(String outcome, boolean redirect){
        navigate(outcome, redirect, null);
    }

    protected void navigate(String outcome, String idValue, boolean redirect){
        Map<String, String> params = new HashMap<>();
        params.put(ID_PARAM, idValue);

        navigate(outcome, redirect, params);
    }

    private void navigate(String outcome, boolean redirect, Map<String, String> params){
        FacesContext fCtx = FacesContext.getCurrentInstance();
        if(params == null){
            params = new HashMap<>();
        }
        params.put("faces-redirect", ""+redirect);
        StringBuilder sb = new StringBuilder();
        sb.append(outcome+"?");
        for(Map.Entry<String, String> entry: params.entrySet()){
            if(!sb.toString().endsWith("?")){
                sb.append("&");
            }
            sb.append(entry.getKey()+"="+entry.getValue());
        }
        NavigationHandler myNav = fCtx.getApplication().getNavigationHandler();
        myNav.handleNavigation(fCtx, null, sb.toString());
    }

    protected String getIdAsString(){
        Map<String, String> paramMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String idStr = paramMap.get(ID_PARAM);
       return idStr;
    }
}
