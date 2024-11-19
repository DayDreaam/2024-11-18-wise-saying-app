package com.ll;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.File;

public class Main {
    public static void main(String[] args) {

        System.out.println("== 명언 앱 ==");
        Scanner scanner = new Scanner(System.in);
        int i = 1;
        Moklok[] arr = new Moklok[300];
        int num;

        // 기존 파일 있을 경우 (.txt)
        String txtPath = "src/main/db/wiseSaying/lastId.txt";
        File tfile = new File(txtPath);
        if(tfile.exists()){
            try{
                FileReader reader = new FileReader(txtPath);
                i = reader.read() - '0' + 1;
            }catch (IOException e) {
                e.printStackTrace();
            }
        }

        //기존 파일 있을 경우 (.json)
        File directory = new File("src/main/db/wiseSaying");
        if(directory.exists() && directory.isDirectory()){
            File[] files = directory.listFiles((dir, name) -> name.endsWith(".json"));
            if (files != null && files.length > 0) {
                for (File file : files) {
                    int n = Integer.parseInt(file.getName().substring(0,1));

                    // 파일 내용 읽기
                    try {
                        String content = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
                        arr[n] = parseJsonToMoklok(content);

                    } catch (IOException e) {
                        System.out.println("파일을 읽는 중 오류가 발생했습니다: " + file.getName());
                        e.printStackTrace();
                    }
                }
            }
        }


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
                for(int j = 0; j < i; j++){
                    if(arr[j]!=null) {
                        String json = "{\n" +
                                "  \"id\": " + arr[j].id + ",\n" +
                                "  \"content\": \"" + arr[j].content + "\",\n" +
                                "  \"author\": \"" + arr[j].author + "\"\n" +
                                "}";

                        String filePath = "src/main/db/wiseSaying/" + arr[j].id + ".json";
                        try(FileWriter writer = new FileWriter(filePath)){
                            writer.write(json);
                            writer.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }

                try(FileWriter writer = new FileWriter("src/main/db/wiseSaying/lastId.txt")){
                    writer.write(String.valueOf(--i));
                    writer.close();
                }catch (IOException e){
                    e.printStackTrace();
                }

                break;
            }
            else if(command.equals("목록")) {
                System.out.println("번호/작가/명언");
                System.out.println("----------------------");
                for (int j = i; j > 0; j--) {
                    if (arr[j] != null) {
                        System.out.println(arr[j].id + " / " + arr[j].author + " / " + arr[j].content);
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
                    System.out.println("명언(기존) : "+arr[num].content);
                    System.out.print("명언 : ");
                    arr[num].content = scanner.nextLine();
                    System.out.println("작가(기존) : "+arr[num].author);
                    System.out.print("작가 : ");
                    arr[num].author = scanner.nextLine();
                }
                else{
                    System.out.println(num+"번 명언은 존재하지 않습니다.");
                }
            }
            else if(command.substring(0,2).equals("빌드")){
                String json = "[\n";
                for(int j = 0; j < i; j++){
                    if(arr[j]!=null) {
                        json += "  {\n" +
                                "    \"id\": " + arr[j].id + ",\n" +
                                "    \"content\": \"" + arr[j].content + "\",\n" +
                                "    \"author\": \"" + arr[j].author + "\"\n" +
                                "  }";
                        // 콤마붙이고 다시진행
                        if(j!=i-1){
                            json += ",\n";
                        }
                    }
                }
                json += "\n]";
                String filePath = "src/main/db/wiseSaying/data.json";
                try(FileWriter writer = new FileWriter(filePath)){
                    writer.write(json);
                    writer.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }

        }


    }
    public static Moklok parseJsonToMoklok(String json) {
        try {
            // id 추출
            int id = Integer.parseInt(json.split("\"id\":")[1].split(",")[0].trim());
            // content 추출
            String content = json.split("\"content\": \"")[1].split("\",")[0].trim();
            // author 추출
            String author = json.split("\"author\": \"")[1].split("\"")[0].trim();

            // Person 객체 생성
            return new Moklok(id, author, content);
        } catch (Exception e) {
            System.out.println("JSON 파싱 중 오류 발생: " + e.getMessage());
            return null;
        }
    }
}