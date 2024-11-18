package com.ll;

import org.json.simple.JSONObject;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("== 명언 앱 ==");
        Scanner scanner = new Scanner(System.in);
        //TODO json, txt 파일 관련 읽어오는 과정 수행
        int i = 1;
        Moklok[] arr = new Moklok[300];
        int num;
        JSONObject json = new JSONObject();

        while(true){
            System.out.print("명령) ");
            String command = scanner.nextLine();

            if (command.equals("등록")) {
                System.out.print("명언 : ");
                String saying = scanner.nextLine();
                System.out.print("작가 : ");
                String author = scanner.nextLine();
                System.out.println(i + "번 명언이 등록되었습니다.");
                arr[i] = new Moklok(i, author, saying);
                i++;
            }
            else if(command.equals("종료")) {
                for(int j = i; j > 0; j--){
                    if(arr[j]!=null){
                        // TODO json 파일, txt 파일 관련 수정 필요
                        // json.put("id",arr[j].index);

                    }
                }
                break;
            }
            else if(command.equals("목록")) {
                System.out.println("번호/작가/명언");
                System.out.println("----------------------");
                for (int j = i; j > 0; j--) {
                    if (arr[j] != null) {
                        System.out.println(arr[j].index + " / " + arr[j].author + " / " + arr[j].saying);
                    }
                }
            }
            else if(command.substring(0,2).equals("삭제")){
                num = Integer.parseInt(command.substring(6));
                if(arr[num]!= null){
                    arr[num] = null;
                    System.out.println(num+"번 명언이 삭제되었습니다.");
                }
                else{
                    System.out.println(num+"번 명언은 존재하지 않습니다.");
                }
            }
            else if(command.substring(0,2).equals("수정")){
                num = Integer.parseInt(command.substring(6));
                if(arr[num]!= null){
                    System.out.println("명언(기존) : "+arr[num].saying);
                    System.out.print("명언 : ");
                    arr[num].saying = scanner.nextLine();
                    System.out.println("작가(기존) : "+arr[num].author);
                    System.out.print("작가 : ");
                    arr[num].author = scanner.nextLine();
                }
                else{
                    System.out.println(num+"번 명언은 존재하지 않습니다.");
                }
            }

        }


    }

}