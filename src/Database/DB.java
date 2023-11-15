package Database;


import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.Map;

public class DB {
    private Map<String, byte[]> userPasswords;
    
    public DB() {
        userPasswords = new HashMap<String, byte[]>();
        insertUser("john smith", HexFormat.of().parseHex("d404559f602eab6fd602ac7680dacbfaadd13630335e951f097af3900e9de176b6db28512f2e000b9d04fba5133e8b1c6e8df59db3a8ab9d60be4b97cc9e81db")); // password
        insertUser("alice", HexFormat.of().parseHex("34a2b9bfaf1a136c0c9c998e18eb0c116764e087a0e6e2ac1334d8a95768765f4f9782e80d8deb167fb1e58d4919eb48f985db812b5641cfe5341a0224377f0f")); // alicepassword
        insertUser("bob", HexFormat.of().parseHex("bb124acce009b4bd3e5a992b099eeb83bbf5e8aed97b71cb2061a3f6b5177d05e5d56b9e3689acc4c6a3e409289eba9c2f3424494770165ea600da62b06dcecf")); // bobpassword
        insertUser("cecilia", HexFormat.of().parseHex("c83f14b7176a20dadd5d6ef8f4e5a865e75731a0bae5bf72a72c2163cee59596036c9ce7edab3463c61c2db21c251bfb1d57dcedf829ec1451d0eac48b2a95ac")); // ceciliapassword
        insertUser("will", HexFormat.of().parseHex("628096c9b7b98909a2bf82fb59127dbbe177786203eaa6c95333769ad9e9df1a7ebabaa78e1115c6cfddd995699ad15a95335044c3284399b676214907352519")); // willpassword
        insertUser("david", HexFormat.of().parseHex("b8456ba9ffd655f59652ee2673b018a2c444facf8ac3c75dfc1d77eb1af46121e00db3fa9d5a97c415c7c4b204ebe6de7aa13d5102258d4a5c44019c12cf12d6")); // davidpassword
        insertUser("erica", HexFormat.of().parseHex("f82600dcdb1681672861d6c1b054bf47585230c1e083367273ab217f5b686b2b4ffcf02ea46534d25035ccf716eeb8e72c454e0509b8b6b85e1646a2cea3587d")); // ericapassword
        insertUser("fred", HexFormat.of().parseHex("e690d384cdc25284a9502e68f9a930f10c7173657267f5431d2acce3e8dfa87ab762c2a81ed9bb1bddfb955c8144c3205e498680cb3acf4bd314ee76b7792dda")); // fredpassword
        insertUser("george", HexFormat.of().parseHex("d9437ffbf3de39d6f405c4f285f5a3e26d4a8b1e78a3b8a16b0c254523c41111f39e1710e5255e2b7b6921eeae29f66c4c9056a97aa0df8d7bcd2bc504937d1d")); // georgepassword
    }
    
    public boolean validateLogin(String user, byte[] password) {
        if (!userPasswords.containsKey(user)) {
            return false;
        } 
        String databasePassword = (new String(userPasswords.get(user), StandardCharsets.UTF_8));
        return databasePassword.equals((new String(password, StandardCharsets.UTF_8)));
    }
    public void insertUser(String user, byte[] password) {
        userPasswords.put(user,  password);
    }
} 