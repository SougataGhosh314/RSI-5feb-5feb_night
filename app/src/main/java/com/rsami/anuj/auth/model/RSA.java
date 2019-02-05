package com.rsami.anuj.auth.model;
import java.util.Random;
public class RSA {

    Random rand = new Random();

    public RSA(){

    }

    String ace = "qwertyuiop[]asdfghjkl;zxc7896321450vbnm,.QWERTYUI741852963OP[]ASDFGHJKL;ZXCVBNM,.7418529630*-+!@#$%^&*()_+";
    int k1[] = {25,4,8,15,12,3,7,13,20,21,5,1,2,9,11,10,17,19,18,24,16,22,23,14,6,0};
    int fndNo(int x){
        for(int i=0;i<k1.length;i++) {
            if(k1[i]==x) return i;
        }
        return 0;
    }
    char enc(char c){
        if(c>='a' && c<='z') return encAlpha(c);
        else if(c>='A' && c<='Z') return encAlpha(c);
        else return c;
    }
    char dec(char c){
        if(c>='a' && c<='z') return decAlpha(c);
        else if(c>='A' && c<='Z') return decAlpha(c);
        else return c;
    }
    char encAlpha(char c){
        char ans = 'a';
        int i,j,k,l;
        if(c>='a' && c<='z'){
            i=c-'a';
            j=k1[i];
            ans=(char) ((char)'a'+j);
        }
        else{
            i=c-'A';
            j=k1[i];
            ans=(char) ((char)'A'+j);
        }
        return ans;
    }

    char decAlpha(char c){
        char ans = ' ';
        int i,j,k,l;
        if(c>='a' && c<='z'){
            i=c-'a';
            j=fndNo(i);
            ans=(char) ((char)'a'+j);
        }
        else{
            i=c-'A';
            j=fndNo(i);
            ans=(char) ((char)'A'+j);
        }
        return ans;
    }

    char giveMeSomething(){
        int n = rand.nextInt(ace.length()) + 1;
        return ace.charAt(n%ace.length());
    }
    public String Encrypt(String msg) {
        int i,j=0,k,l,n=msg.length();
        String ans = new String();
        while(j<n){
            for(i=0;i<15;i++){
                ans+=giveMeSomething();
            }

            ans+=enc(msg.charAt(j));
            j++;
        }
        return ans;
    }

    public String Decrypt(String msg) {
        int i,j=0,k,l,n=msg.length();
        String ans = new String();
        while(j<n){
            for(i=0;i<15;i++){
                j++;
            }
            ans+=dec(msg.charAt(j));
            j++;
        }
        return ans;
    }

    public static void main(String[] args) {
        try{
            RSA r = new RSA();
            String msg = "SurajSinghBisht9325490843";
            String e = r.Encrypt(msg);
            System.out.print("\n\nmessage is "+msg+"\nEncrypted msg "+e);
            String d = r.Decrypt(e);
            System.out.print("\n\n\ndecrypted is "+d);
            System.out.print("\n\n"+d.equals(msg));
            System.out.print("qwertyuiopasdfghjklzxcvbnm741852963QWERTYUIOPASDFGHJKLZXCVBNM".equals("qwertyuiopasdfghjklzxcvbnm741852963QWERTYUIOPASDFGHJKLZXCVBNM"));
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }



}