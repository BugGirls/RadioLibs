package hndt.radiolibs.servlet;

import org.apache.commons.lang3.math.NumberUtils;
import hndt.radiolibs.bean.TipBean;
import hndt.radiolibs.util.GSON;
import hndt.radiolibs.util.Logger;
import hndt.radiolibs.util.Utils;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@ServerEndpoint(value = "/api/ws/tip")
public class TipAnnotation {

    static Map<Long, Session> map = new HashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        String tmp = getParameter(session, "manager_id");
        if (Utils.isNotEmpty(tmp)) {
            Long manager_id = NumberUtils.toLong(tmp);
            map.put(new Long(manager_id), session);
        }
    }

    @OnClose
    public void end(Session session) {
        Long sid = null;
        for (Map.Entry<Long, Session> entry : map.entrySet()) {
            if (Objects.equals(session, entry.getValue())) {
                sid = entry.getKey();
            }
        }
        map.remove(sid);
    }

    @OnMessage
    public void incoming(String msg, Session session) {
        if (Utils.isEmpty(msg) || msg.length() == 1) {
//            try {
//                session.getBasicRemote().sendText("u");
//            } catch (IOException e) {
//                Logger.error(e);
//            }
//            return;
        }
    }

    @OnError
    public void onError(Session session, Throwable thr) {
        Logger.info("session:" + session.getId() + " was closed");
    }

    public static void send(Long manager_id, TipBean tip) {
        Session session = map.get(manager_id);
        if (session != null && session.isOpen()) {
            try {
                session.getBasicRemote().sendText(GSON.toJson(tip));
            } catch (Exception e) {
                Logger.error(e);
            }
        }
    }

    public static void send(Long manager_id, int code, String message) {
        send(manager_id, new TipBean(code, message));
    }

    public static void send(Long manager_id, String message) {
        send(manager_id, new TipBean(0, message));
    }

    private String getParameter(Session session, String name) {
        String value = null;
        Map<String, List<String>> parameters = session.getRequestParameterMap();
        List<String> values = parameters.get(name);
        if (values != null && !values.isEmpty()) {
            value = values.get(0);
        }
        return value;
    }


}
