package utils;

/**
 * Domain model holding password hash
 *
 * @author Hossein Kazemi <a href="mailto:mrhosseinkazemi@gmail.com">mrhosseinkazemi@gmail.com</a>
 */
public class PBKDF2Hash {
    public String hash;
    public String salt;
    public int iterations;
    public PBKDF2Hash(final String hash,
                      final String salt,
                      final int iterations) {
        this.hash = hash;
        this.salt = salt;
        this.iterations = iterations;
    }

    @Override
    public String toString() {
        return "PBKDF2Hash{" +
                "hash='" + hash + '\'' +
                ", salt='" + salt + '\'' +
                ", iterations=" + iterations +
                '}';
    }
}
