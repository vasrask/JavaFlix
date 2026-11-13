
package javaflix;

import javaflix.model.Program;
import javaflix.model.User;
import javaflix.service.Programs;
import javaflix.service.Users;
import javaflix.ui.LogIn;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.*;

/**
 *
 * @author Vasiliki Raskopoulou
 */
public class Main extends Thread{

    public static void main(String[] args) throws InterruptedException,IOException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("programs.dat"))) {
            Program program = null;
            do {
                try {
                    program = (Program) in.readObject();
                    Programs.addProgram(program);
                } catch (EOFException exc) {
                    break;
                }
            }
            while (program != null);

        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("users.dat"))) {
            User user = null;
            do {
                try {
                    user = (User) in.readObject();
                    Users.addUser(user);
                } catch (EOFException exc) {
                    break;
                }
            }
            while (user != null);
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }

        new LogIn();

        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
                try (ObjectOutputStream out = new ObjectOutputStream(
                        new FileOutputStream("users.dat"))) {
                    for (User user : Users.getAllUsers()) {
                        out.writeObject(user);
                    }
                }
                catch (IOException e) {
                    System.out.println(e);
                }
                try (ObjectOutputStream out = new ObjectOutputStream(
                        new FileOutputStream("programs.dat"))) {
                    for (Program program : Programs.getAllPrograms()) {
                        out.writeObject(program);
                    }
                }
                catch (IOException e) {
                    System.out.println(e);
                }
            }
        });

    }

}

