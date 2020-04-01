package com.tamsiree.rxkit.demodata;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVFileGenerator {

    private static final String LINE_SEPERATOR = System
            .getProperty("line.separator");

    private final static Charset charset = StandardCharsets.UTF_8;

    public static void generate(List<HashMap<String, Object>> data,
                                String[] columns,
                                String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }

        for (Map<String, Object> objects : data) {
            List<String> result = Lists.newArrayList();
            for (String column : columns) {
                if (objects.get(column) != null) {
                    result.add(objects.get(column).toString());
                } else {
                    result.add("");
                }
            }

            String lineData = Joiner.on(",").skipNulls().join(result);
            try {
                Files.append(lineData + LINE_SEPERATOR, file, charset);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
