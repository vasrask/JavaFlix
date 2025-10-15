
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.*;

/**
 *
 * @author Vasiliki Raskopoulou
 */
public class Main extends Thread{

    public static void main(String[] args) throws InterruptedException,IOException {
        Programs programs = new Programs();
        int l = 0;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("programs.dat"))) {
            int i = 1;
            Program program = null;
            do {
                try {
                    program = (Program) in.readObject();
                    programs.ProgramCatalog.put(i, program);
                    i++;
                } catch (EOFException exc) {
                    break;
                }
            }
            while (program != null);

        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
        Users users = new Users();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("users.dat"))) {
            int i = 1;
            User user = null;
            do {
                try {
                    user = (User) in.readObject();
                    users.UserCatalog.put(i, user);
                    i++;
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
                    for (User user : users.UserCatalog.values()) {
                        out.writeObject(user);
                    }
                }
                catch (IOException e) {
                    System.out.println(e);
                }
                try (ObjectOutputStream out = new ObjectOutputStream(
                        new FileOutputStream("programs.dat"))) {
                    for (Program program : programs.ProgramCatalog.values()) {
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



