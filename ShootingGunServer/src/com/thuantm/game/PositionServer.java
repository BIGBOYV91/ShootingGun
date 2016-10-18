package com.thuantm.game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.thuantm.game.Network.AddCharacter;
import com.thuantm.game.Network.Login;
import com.thuantm.game.Network.MoveCharacter;
import com.thuantm.game.Network.Register;
import com.thuantm.game.Network.RegistrationRequired;
import com.thuantm.game.Network.RemoveCharacter;
import com.thuantm.game.Network.UpdateCharacter;

public class PositionServer {
	Server server;
	HashSet<Character> loggedIn = new HashSet<Character>();

	public PositionServer () throws IOException {
		server = new Server() {
			protected Connection newConnection () {
				// By providing our own connection implementation, we can store per
				// connection state without a connection ID to state look up.
				return new CharacterConnection();
			}
		};

		// For consistency, the classes to be sent over the network are
		// registered by the same method for both the client and server.
		Network.register(server);

		server.addListener(new Listener() {
			public void received (Connection c, Object object) {
				// We know all connections for this server are actually CharacterConnections.
				CharacterConnection connection = (CharacterConnection)c;
				Character character = connection.character;

				if (object instanceof Login) {
					// Ignore if already logged in.
					if (character != null) return;

					// Reject if the name is invalid.
					String name = ((Login)object).name;
					if (!isValid(name)) {
						c.close();
						return;
					}
					System.out.println("Deteched: User login: "+name);
					// Reject if already logged in.
					for (Character other : loggedIn) {
						if (other.name.equals(name)) {
							c.close();
							
							System.out.println("USER: ("+name +") da ton tai reject conection");
							return;
						}
					}

					character = loadCharacter(name);

					// Reject if couldn't load character.
					if (character == null) {
						c.sendTCP(new RegistrationRequired());
						return;
					}

					loggedIn(connection, character);
					return;
				}

				if (object instanceof Register) {
					// Ignore if already logged in.
					if (character != null) return;

					Register register = (Register)object;

					// Reject if the login is invalid.
					if (!isValid(register.name)) {
						c.close();
						return;
					}
					if (!isValid(register.otherStuff)) {
						c.close();
						return;
					}

					// Reject if character alread exists.
					if (loadCharacter(register.name) != null) {
						c.close();
						return;
					}

					character = new Character();
					character.name = register.name;
					character.otherStuff = register.otherStuff;
					character.x = 0;
					character.y = 0;
					if (!saveCharacter(character)) {
						c.close();
						return;
					}

					loggedIn(connection, character);
					return;
				}

				if (object instanceof MoveCharacter) {
					// Ignore if not logged in.
					if (character == null) return;

					MoveCharacter msg = (MoveCharacter)object;

					// Ignore if invalid move.
					//if (Math.abs(msg.x) != 1 && Math.abs(msg.y) != 1) return;

					character.x = msg.x;
					character.y = msg.y;
					
					if (!saveCharacter(character)) {
						connection.close();
						return;
					}
					UpdateCharacter update = new UpdateCharacter();
					update.id = character.id;
					update.x = character.x;
					update.y = character.y;
					//server.sendToAllTCP(update);
					//connection.sendTCP(update);
					
					server.sendToTCP(connection.getID(), update);
					Connection conx = getConnection(connection.character);
					
					if(conx !=null){
						conx.sendTCP(update);
					}
					
					//System.out.println("ServerX: "+character.x +"   ServerY: "+character.y);
					return;
				}
			}

			private boolean isValid (String value) {
				if (value == null) return false;
				value = value.trim();
				if (value.length() == 0) return false;
				return true;
			}

			public void disconnected (Connection c) {
				CharacterConnection connection = (CharacterConnection)c;
				if (connection.character != null) {
					loggedIn.remove(connection.character);

					RemoveCharacter removeCharacter = new RemoveCharacter();
					removeCharacter.id = connection.character.id;
					server.sendToAllTCP(removeCharacter);
				}
			}
		});
		server.bind(Network.port);
		server.start();
	}
	
	private Connection getConnection(Character c){
		if(c == null) return null;
		//System.out.println(c.name+ " --- SO LUONG KET NOI:  "+server.getConnections().length);
		Connection[]  cons =  server.getConnections();
		
		for (Connection connection : cons) {
			CharacterConnection con = (CharacterConnection) connection;
			
			if(con!=null&& con.character!=null && con.character.otherStuff.equals(c.otherStuff)
				&& 	!con.character.name.equals(c.name)){
				return connection;
			}
		}
		return null;
	}

	void loggedIn (CharacterConnection c, Character character) {
		c.character = character;
		System.out.println("Den day: xx 3333 ");
		// Add existing characters to new logged in connection.
		for (Character other : loggedIn) {
			AddCharacter addCharacter = new AddCharacter();
			addCharacter.character = other;
			c.sendTCP(addCharacter);
		}

		loggedIn.add(character);
		System.out.println("Den day: xx 4444 ");
		// Add logged in character to all connections.
		AddCharacter addCharacter = new AddCharacter();
		addCharacter.character = character;
		server.sendToAllTCP(addCharacter);
	}

	boolean saveCharacter (Character character) {
		File file = new File("characters", character.name.toLowerCase());
		file.getParentFile().mkdirs();

		if (character.id == 0) {
			String[] children = file.getParentFile().list();
			if (children == null) return false;
			character.id = children.length + 1;
		}

		DataOutputStream output = null;
		try {
			output = new DataOutputStream(new FileOutputStream(file));
			output.writeInt(character.id);
			output.writeUTF(character.otherStuff);
			output.writeFloat(character.x);
			output.writeFloat(character.y);
			return true;
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		} finally {
			try {
				output.close();
			} catch (IOException ignored) {
			}
		}
	}

	Character loadCharacter (String name) {
		File file = new File("characters", name.toLowerCase());
		if (!file.exists()) return null;
		DataInputStream input = null;
		try {
			input = new DataInputStream(new FileInputStream(file));
			Character character = new Character();
			character.id = input.readInt();
			character.name = name;
			character.otherStuff = input.readUTF();
			character.x = input.readFloat();
			character.y = input.readFloat();
			input.close();
			return character;
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		} finally {
			try {
				if (input != null) input.close();
			} catch (IOException ignored) {
			}
		}
	}

	// This holds per connection state.
	static class CharacterConnection extends Connection {
		public Character character;
	}

	public static void main (String[] args) throws IOException {
		Log.set(Log.LEVEL_DEBUG);
		new PositionServer();
	}
}