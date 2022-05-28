package com.mycompany.javacross;

import javax.swing.*;
import org.json.simple.parser.JSONParser;
import java.io.*;
import java.awt.event.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
        
public class JavaCrossplatform
{
    public static void main(String[] args){
        JFrame frame = new JFrame("Редактор JSON");
        JPanel panel = new JPanel(null);
        frame.add(panel);
        frame.setSize(640, 480);
        PlaceComponents(panel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    private static Boolean CheckJSON(String json_txt){
        try{
            JSONParser check = new JSONParser();
            check.parse(json_txt);
        }
        catch(Exception e){
            return false;
        }
        
        return true;
    }
    
    private static void PlaceComponents(JPanel panel){
        //Подсказывающий лэйбл
        JLabel urlLabel = new JLabel("Введите ссылку на файл Json");
        urlLabel.setBounds(10,10,180,20);
        panel.add(urlLabel);

        //Строка для ссылки на JSON
        JTextField urlInput = new JTextField();
        urlInput.setBounds(200,10,320,20);
        panel.add(urlInput);

        //Кнопка скачать
        JButton buttonDownload = new JButton("Скачать");
        buttonDownload.setBounds(530,10,90,20);
        panel.add(buttonDownload);
        
        //Поле редактора JSON
        JTextArea jsonRedactor = new JTextArea();
        jsonRedactor.setBounds(10, 40, 610, 370);
        panel.add(jsonRedactor);

        //Обработчик кнопки
        buttonDownload.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String urlLink = urlInput.getText();
                try (BufferedInputStream in = new BufferedInputStream(new 
        URL(urlLink).openStream())){ 
                    byte dataBuffer[] = new byte[1024];
                    int len;
                    jsonRedactor.setText("");
                    while ((len = in.read(dataBuffer, 0, 1024)) != -1) {
                        jsonRedactor.append(new String(dataBuffer, 
                                0, len, StandardCharsets.UTF_8));
                    }
                } 
                catch (IOException error) {
                    jsonRedactor.setText("При загрузки файла произошла ошибка\n"
                            + error.getLocalizedMessage());
                }
            }
        });

        //Кнопка скачать
        JButton buttonSave = new JButton("Сохранить");
        buttonSave.setBounds(270, 420, 100, 20);
        panel.add(buttonSave);

        //Обработчик кнопки скачать
        buttonSave.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JFileChooser fileName = new JFileChooser("Сохранить JSON файл");
                if(CheckJSON(jsonRedactor.getText()))
                {
                    if(fileName.showSaveDialog(null) == 
                            JFileChooser.APPROVE_OPTION)
                    {
                        try(FileWriter fw = 
                                new FileWriter(fileName.getSelectedFile())){
                            fw.write(jsonRedactor.getText());
                            JOptionPane.showMessageDialog(null, 
                                    "Файл успешно сохранён\n");
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(null, 
                                    "Ошибка сохранения файла\n"+
                                            ex.getLocalizedMessage());
                        }
                    }
                }
                else JOptionPane.showMessageDialog(null, 
                                    "Невалидный синтаксис JSON\n");;
            }
        });
    }
}
