package pl.rpieja.flat;

/**
 * Created by radix on 10/26/17.
 */

public class User {

        private String username ;
        private String password ;

        public void setUsername(String uname){
            this.username = uname ;
        }
        public String getUsername(){
            return username;
        }
        public void setPassword(String password){
            this.password = password ;
        }
        public String getPassword(){
            return password ;
        }
        public User(User user){
            this.username = user.getUsername();
            this.password = user.getPassword();
        }
        public User(){}
}