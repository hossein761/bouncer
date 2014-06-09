package utils;

import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static org.junit.Assert.*;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

public class PasswordHashTest {

    @Test
    public void shouldCreateHash() throws Exception {
        running(fakeApplication(inMemoryDatabase()),new Runnable(){
            @Override
            public void run() {
                final String password = "password";
                try {
                    final PBKDF2Hash hash = PasswordHash.createHash(password);
                    System.out.println("hash: "+ hash);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Test
    public void shouldValidatePassword() throws Exception {

    }

    @Test
    public void shouldValidatePassword1() throws Exception {

    }
}