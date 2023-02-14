package com.itheima.ATMDEMO;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class ATMSystem {
    public static void main(String[] args) {
        ArrayList<Account> accounts=new ArrayList<>();
        Scanner sc=new Scanner(System.in);
        while (true) {
            System.out.println("==========欢迎您进入ATM系统==========");
            System.out.println("1.账户登陆");
            System.out.println("2.账户开户");

            System.out.println("请选择操作指令");
            int command=sc.nextInt();
            switch (command){
                case 1:
                    login(accounts,sc);
                    break;
                case 2:
                    register(accounts,sc);
                    break;
                default:
                    System.out.println("您输入有误，请按指令重新输入");
                    break;
            }
        }
    }

    //登陆方法
    private static void login(ArrayList<Account> accounts, Scanner sc) {
        System.out.println("=========欢迎来到ATM系统登陆界面=========");
        if(accounts.size()==0){
            System.out.println("对不起，当前系统中无任何账户，清先进行开户操作!");
            return;
        }
        while (true) {
            System.out.println("请您输入登陆卡号");
            String cardID = sc.next();
            Account acc=getAccountByCardID(cardID,accounts);
            if(acc!=null){
                while (true) {
                    System.out.println("请您输入登陆密码");
                    String password=sc.next();
                    if(acc.getPassword().equals(password)){
                        System.out.println("欢迎" + acc.getUserName() + "进入ATM系统");
                        showUserCommand(sc,acc,accounts);
                        return;//结束登陆方法以便回到上一个界面
                    }else{
                        System.out.println("对不起，您输入的密码有误");
                    }
                }
            }else{
                System.out.println("对不起，系统中不存在该用户");
            }
        }

    }

    //登陆后操作页
    private static void showUserCommand(Scanner sc,Account acc,ArrayList<Account> accounts) {
        while (true) {
            System.out.println("==========ATM系统操作页面==========");
            System.out.println("1.查询账户");
            System.out.println("2.存款");
            System.out.println("3.取款");
            System.out.println("4.转账");
            System.out.println("5.修改密码");
            System.out.println("6.退出登陆");
            System.out.println("7.注销账户");
            System.out.println("请您输入指令");
            int command=sc.nextInt();
            switch (command){
                case 1:
                    //1.查询账户
                    showAccount(acc);
                    break;
                case 2:
                    //2.存款
                    depositMoney(acc,sc);
                    break;
                case 3:
                    //3.取款
                    drawMoney(acc,sc);
                    break;
                case 4:
                    //4.转账
                    transferMoney(sc,acc,accounts);
                    break;
                case 5:
                    //5.修改密码
                    updatePassword(sc,acc);
                    return;
                case 6:
                    //6.退出登陆
                    System.out.println("退出成功!欢迎下次继续使用");
                    return;
                case 7:
                    //7.注销账户
                    if(cancel(acc,sc,accounts)){
                        return;
                    }else {
                     break;
                    }
                default:
                    System.out.println("您输入有误，请重新输入");
            }
        }
    }

    //注销账户方法
    private static boolean cancel(Account acc, Scanner sc,ArrayList<Account> accounts) {
        System.out.println("==========账户注销页面==========");
        System.out.println(acc.getUserName() + "，你好！您是否真的要注销账户?");
        System.out.println("1.是");
        System.out.println("2.否");
        int select=sc.nextInt();
        switch (select){
            case 1:
                if(acc.getMoney()>0){
                    System.out.println("您的账户余额不为0,请谨慎销户");
                }else {
                    accounts.remove(acc);
                    System.out.println("销户操作完成！");
                    return true;
                }
                break;
            case 2:
            default:
                System.out.println("账户销户取消！");
        }
        return false;
    }

    //修改密码方法
    private static void updatePassword(Scanner sc, Account acc) {
        System.out.println("==========用户密码修改界面==========");
        System.out.println("你好，" + acc.getUserName() + "，请您输入当前密码");
        while (true) {
            String password=sc.next();
            if(password.equals(acc.getPassword())){
                System.out.println("密码正确！请您输入新密码");
                while (true) {
                    String newPassword=sc.next();
                    System.out.println("请您再次确认新密码");
                    String ConfirmPassword=sc.next();
                    if(newPassword.equals(ConfirmPassword)){
                        System.out.println("修改密码成功！请重新登陆!");
                        acc.setPassword(newPassword);
                        return;
                    }else{
                        System.out.println("您俩次输入的密码不一致，请重新设置新密码");
                    }
                }
            }else{
                System.out.println("您输入的密码有误，请重新输入");
            }
        }
    }

    //转账方法
    private static void transferMoney(Scanner sc, Account acc, ArrayList<Account> accounts) {
        System.out.println("==========转账页面==========");
        if(accounts.size()<2){
            System.out.println("当前ATM系统中账户数量不足俩个，无法完成转账操作，请先进行开户操作");
            return;
        }

        if(acc.getMoney()==0){
            System.out.println("对不起！您的余额不足，请先进行存款操作");
            return;
        }

        while (true) {
            System.out.println("请您输入对方账号卡号");
            String cardID=sc.next();
            if(cardID.equals(acc.getCardID())){
                System.out.println("对不起，您不可以给自己转账");
                continue;
            }

            Account account=getAccountByCardID(cardID,accounts);
            if(account==null){
                System.out.println("对不起！您输入的卡号不存在");
            }else {
                String userName=account.getUserName();
                String tip="*"+userName.substring(1);
                System.out.println("请您输入[" + tip + "]的姓氏");
                String preName=sc.next();
                if(userName.startsWith(preName)){
                    while (true) {
                        System.out.println("请您输入转账金额");
                        double money=sc.nextDouble();
                        if(money>acc.getMoney()){
                            System.out.println("对不起，您的余额不足！您最多可以转账"+acc.getMoney()+"元");
                        }else {
                            if(money>acc.getQuotaMoney()){
                                System.out.println("对不起，您要转账的金额超过最大金额限度，每次最多可以转账"+acc.getQuotaMoney()+"元");
                            }else{
                                acc.setMoney(acc.getMoney()-money);
                                account.setMoney(account.getMoney()+money);
                                System.out.println("转账成功！您的账户余额还有" + acc.getMoney() + "元");
                                return;
                            }
                        }
                    }
                }else {
                    System.out.println("对不起，您的认证信息填写错误");
                }
            }


        }
    }

    //取款方法
    private static void drawMoney(Account acc, Scanner sc) {
        System.out.println("==========取款界面==========");
        System.out.println("取款规则：每次取款必须大于100.0元");
        if(acc.getMoney()<100){
            System.out.println("对不起，您当前余额不足100.0元，请先进行存款");
            return;//退回系统操作页面
        }
        while (true) {
            System.out.println("请输入取款金额");
            double money=sc.nextDouble();
            if(money>acc.getQuotaMoney()){
                System.out.println("对不起！您输入的取款金额超过最大限额,每次最多提取"+acc.getQuotaMoney());
            }else{
                if(money>acc.getMoney()){
                    System.out.println("对不起！您当前账户余额不足,您当前余额为"+acc.getMoney());
                }else{
                    System.out.println("取款"+money+"成功！");
                    acc.setMoney(acc.getMoney()-money);
                    showAccount(acc);
                    return;
                }
            }

        }


    }

    //存款方法
    private static void depositMoney(Account acc,Scanner sc) {
        System.out.println("==========存款界面==========");
        System.out.println("请输入存款金额");
        double money=sc.nextDouble();
        acc.setMoney(acc.getMoney()+money);
        System.out.println("存款成功！");
        showAccount(acc);
    }

    //展示账户信息
    private static void showAccount(Account acc) {
        System.out.println("===========个人页面==========");
        System.out.println("用户名：" + acc.getUserName());
        System.out.println("卡号：" + acc.getCardID());
        System.out.println("余额：" + acc.getMoney());
        System.out.println("限额：" + acc.getQuotaMoney());
    }

    //开户方法
    private static void register(ArrayList<Account> accounts,Scanner sc) {
        System.out.println("==========欢迎您使用系统开户操作==========");
        //1.创建账户对象存储信息
        Account account=new Account();

        //2.用户输入用户名和密码
        System.out.println("请您输入用户名");
        String userName=sc.next();
        account.setUserName(userName);

        while (true) {
            System.out.println("请您输入密码");
            String password=sc.next();
            System.out.println("请您确认密码");
            String okPassword=sc.next();
            if(okPassword.equals(password)){
                account.setPassword(password);
                break;
            }else{
                System.out.println("对不起，您输入的俩次密码不一致，请重新输入");
            }
        }

        System.out.println("请您输入每次提现额度");
        double quotaMoney=sc.nextDouble();
        account.setQuotaMoney(quotaMoney);

        //为账户生成一个随机八位且不重复的卡号
        String cardID=getRandomCardID(accounts);
        account.setCardID(cardID);

        //把所有信息放进集合
        accounts.add(account);
        System.out.println("恭喜您"+userName+"，开户成功！您的卡号是"+account.getCardID()+",请您妥善保管");

    }

    //生成随机八位且不重复的卡号
    private static String getRandomCardID(ArrayList<Account> accounts) {
        Random r=new Random();
        while (true) {
            String cardID="";
            for (int i = 0; i < 8; i++) {
                cardID+=r.nextInt(8);
            }
            Account acc=getAccountByCardID(cardID,accounts);
            if(acc==null){
                return cardID;
            }
        }


    }
    
    //根据卡号查询账户
    private static Account getAccountByCardID(String cardID,ArrayList<Account> accounts){
        for (int i = 0; i < accounts.size(); i++) {
            Account acc=accounts.get(i);
            if(acc.getCardID().equals(cardID)){
                return acc;
            }
        }
        return null;
    }
}
