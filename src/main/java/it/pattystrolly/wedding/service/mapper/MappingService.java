package it.pattystrolly.wedding.service.mapper;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MappingService {

    public static String getResolvedString(String str, final Map<String, String> mapping)throws IOException{
        Pattern p = Pattern.compile("(\\$\\{.[^}]+\\})");
        Matcher m = p.matcher(str);
        while (m.find()) {
            String found = m.group().substring(2, m.group().length() - 1);
            str = str.replace(m.group(), mapping.get(found) != null ? mapping.get(found): "");
        }
        return str;
    }
}