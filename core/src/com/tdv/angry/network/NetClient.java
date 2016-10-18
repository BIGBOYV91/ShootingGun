package com.tdv.angry.network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Administrator on 15/09/2016.
 */
public class NetClient {
    public HashMap<Integer, Character> characters = new HashMap();
    public Client client;
    public String name;
    public NetClient(){
        client = new Client();
        client.start();

        // For consistency, the classes to be sent over the network are
        // registered by the same method for both the client and server.
        Network.register(client);

        // ThreadedListener runs the listener methods on a different thread.
        client.addListener(new Listener.ThreadedListener(new Listener() {
            public void connected (Connection connection) {

            }
            public void received (Connection connection, Object object) {
                if (object instanceof Network.RegistrationRequired) {
                    Network.Register register = new Network.Register();
                    register.name = name;
                    register.otherStuff = "123456";
                    client.sendTCP(register);
                }
                if (object instanceof Network.AddCharacter) {
                    Network.AddCharacter msg = (Network.AddCharacter)object;
                    characters.put(msg.character.id, msg.character);
                    return;
                }

                if (object instanceof Network.UpdateCharacter) {
                    Network.UpdateCharacter msg = ((Network.UpdateCharacter)object);

                    Character character = characters.get(msg.id);
                    if (character == null) return;
                    character.x = msg.x;
                    character.y = msg.y;
                    //System.out.println(character.name + " moved to " + character.x + ", " + character.y);

                    return;
                }
            }
            public void disconnected (Connection connection) {
                System.exit(0);
            }
        }));

        try {
            client.connect(5000, "192.168.1.87", Network.port);
            // Server communication after connection can go here, or in Listener#connected().
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
