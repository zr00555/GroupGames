package com.groupgames.web.game;

import com.groupgames.web.core.Player;
import com.groupgames.web.game.view.JsonView;
import com.groupgames.web.game.view.View;
import javafx.beans.binding.ObjectBinding;

import javax.websocket.Session;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class State {
    public static final String USERS_TAG = "users";
    public static final String GAME_CODE_TAG = "gamecode";
    public static final String HOST_WS_TAG = "hostWS";

    protected StateManager manager;
    protected HashMap<String, Player> usersMap;

    private Map<String, Object> context;

    /**
     * A State must be constructed with a reference to a StateManager so that the state may
     * conduct state changes
     *
     * @param manager StateManager for the state to use for switching states
     */
    public State(StateManager manager){
        this.manager = manager;
    }

    /**
     * Construct a state with a preexisting context from another state. Copies the state's context
     * into a new reference before use
     *
     * @param manager
     * @param context
     */
    public State(StateManager manager, Map<String, Object> context) {
        // Call the base constructor before adding functionality
        this(manager);

        if (context != null) {
            // Copy the existing context to prevent editing the existing one
            this.context = new HashMap<>(context);
        }

        usersMap = (HashMap<String, Player>) getContext().get(USERS_TAG);
    }

    public void setWebsocket(Session peer) {
        this.getContext().put(HOST_WS_TAG, peer);
    }

    public boolean writeUpdate(String updateText) {
        Session websocket = (Session) getContext().get(HOST_WS_TAG);
        if (websocket != null && websocket.isOpen()) {
            try {
                websocket.getBasicRemote().sendText(updateText);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        // Failed to write update. User hasn't registered websocket connection yet
        return false;
    }

    public void broadcast(HashMap<String, Object> jsonData){

        JsonView jsonView = new JsonView(jsonData);
        String jsonEncoded = jsonView.toString();

        for(Player p : usersMap.values()) {
            p.writeUpdate(jsonEncoded);
        }
        writeUpdate(jsonEncoded);
    }

    public void broadcastRefresh(){
        HashMap<String, Object> broadcastData = new HashMap<>();
        broadcastData.put("method", "refresh");
        broadcast(broadcastData);
    }

    public void kickPlayer(String uid){
        if (usersMap.containsKey(uid)) {
            String username = usersMap.get(uid).getUsername();

            HashMap<String, Object> broadcastData = new HashMap<>();
            broadcastData.put("kick", uid);

            HashMap<String, Player> withoutUser = new HashMap<>(usersMap);
            withoutUser.remove(uid);
            broadcastData.put("users", withoutUser.values());

            String kickMessage = String.format("%s has been kicked from the game", username);
            broadcastData.put("message", kickMessage);
            broadcast(broadcastData);

            usersMap.remove(uid);
        }
    }

    /**
     * Return the current state's context
     *
     * @return map of context variables
     */
    public Map<String, Object> getContext() {
        return context;
    }

     /**
      * Should be used for updating context information in the background such as countdown timers
      *
      */
    public abstract void update();

    /**
     * Uses the user ID to generate the associated view for the user.
     *
     * @param uid user ID performing the action
     * @return current View for the respective used
     */
    public abstract View getView(String uid, String webRootPath);

    /**
     * Perform an action by a user in the lobby. It is up to the implementation to verify the GameAction is valid for
     * the given game type. A null user ID represents the host executing the action
     *
     * @param uid user ID performing the action
     * @param action action to be performed
     */
    public abstract void doAction(String uid, GameAction action);
}
