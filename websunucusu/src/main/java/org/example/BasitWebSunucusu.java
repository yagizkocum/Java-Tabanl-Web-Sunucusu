package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class BasitWebSunucusu {

    private static final int PORT = 1989;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Web sunucusu " + PORT + " portunda başlatıldı...");
            System.out.println("Tarayıcıdan erişmek için: http://localhost:" + PORT);

            // Sunucunun sürekli dinlemede kalması için sonsuz döngü
            while (true) {
                // İstemci (tarayıcı) bağlantısını kabul et
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     OutputStream out = clientSocket.getOutputStream()) {

                    // Tarayıcıdan gelen HTTP isteğinin ilk satırını oku (Örn: GET / HTTP/1.1)
                    String requestLine = in.readLine();
                    if (requestLine != null && !requestLine.isEmpty()) {
                        System.out.println("Gelen İstek: " + requestLine);
                    }

                    // Tarayıcının gönderdiği diğer HTTP başlıklarını tüket (boş satıra kadar)
                    String line;
                    while ((line = in.readLine()) != null && !line.isEmpty()) {
                        // Başlık satırları okunuyor
                    }

                    // Gönderilecek HTML içeriği
                    String htmlIcerik = """
                            <!DOCTYPE html>
                            <html lang="tr">
                            <head>
                                <meta charset="UTF-8">
                                <title>Öğrenci Bilgi Sayfası</title>
                            </head>
                            <body style="background-color: #f4f6f9; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 50px;">
                                <div style="max-width: 600px; margin: auto; background-color: #ffffff; padding: 30px; border-radius: 10px; box-shadow: 0 4px 8px rgba(0,0,0,0.1);">
                                    <h1 style="color: #1e3a8a; border-bottom: 2px solid #e2e8f0; padding-bottom: 10px;">Yağız Koçum</h1>
                                    <h2 style="color: #0284c7; font-weight: normal;">Öğrenci No: 1250505066</h2>
                                    <p style="color: #334155; font-size: 16px; line-height: 1.6; text-align: justify;">
                                        Merhaba, ben Bilgisayar Mühendisliği öğrencisiyim. Yazılım geliştirme, 
                                        algoritmalar, nesne yönelimli programlama ve ağ sistemleri üzerine çalışmalar 
                                        yapmaktayım. Boş zamanlarımda sistem programlama ve yeni teknolojiler 
                                        araştırmakla ilgileniyorum.
                                    </p>
                                </div>
                            </body>
                            </html>
                            """;

                    // Türkçe karakterlerin bozulmaması için byte dizisine UTF-8 olarak çevir
                    byte[] bodyBytes = htmlIcerik.getBytes(StandardCharsets.UTF_8);

                    // HTTP Yanıt Başlıkları (Response Headers)
                    String httpResponseHeaders =
                            "HTTP/1.1 200 OK\r\n" +
                                    "Content-Type: text/html; charset=UTF-8\r\n" +
                                    "Content-Length: " + bodyBytes.length + "\r\n" +
                                    "Connection: close\r\n" +
                                    "\r\n"; // Başlık ile gövdeyi ayıran zorunlu boş satır

                    // Önce başlıkları, ardından HTML gövdesini sokete yaz
                    out.write(httpResponseHeaders.getBytes(StandardCharsets.UTF_8));
                    out.write(bodyBytes);
                    out.flush();

                } catch (IOException e) {
                    System.err.println("İstemci bağlantısı işlenirken hata oluştu: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Sunucu " + PORT + " portunda başlatılamadı: " + e.getMessage());
        }
    }
}
